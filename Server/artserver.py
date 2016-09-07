#!flask/bin/python
from flask import Flask, request, send_from_directory, redirect, url_for, safe_join, Response, stream_with_context
from datadescription import Object, User, Universe
from playhouse.shortcuts import model_to_dict
import json
import os
import os.path
import time
from werkzeug import secure_filename
from gevent.pywsgi import WSGIServer
from gevent import monkey
from shutil import copyfile

monkey.patch_all()

CHUNK_SIZE = 1024*1024  # bytes

Object.create_table(True)
Universe.create_table(True)
User.create_table(True)

app = Flask(__name__, static_folder='')
ALLOWED_EXTENSIONS = set(['txt','jpg','png','obj','fbx'])

@app.route('/')
def root():
    return app.send_static_file('obj_loader/index.html')

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/objects')
def objects():
    lat = float(request.args.get('lat'))
    lng = float(request.args.get('lng'))
    radius = float(request.args.get('rad'))/111000.0
    json_data = json.dumps([model_to_dict(curr_obj) 
                    for curr_obj in Object.select().where(
                        (Object.lat - lat < radius) & 
                        (Object.lat - lat > -radius) & 
                        (Object.lng - lng < radius) & 
                        (Object.lng - lng > -radius) )])
    return json_data
    # return '{ "results": ' + json_data + '}'
    
@app.route('/object')
def object():
    id = int(request.args.get('id'))
    objects = Object.select().where( Object.id == id)
    if not objects:
        return '[]'
    obj = objects[0]
        
    json_data = json.dumps( [model_to_dict(obj)])
    return json_data
    
@app.route('/download/<path:filename>')
def download(filename):

    filepath = safe_join(app.static_folder + 'def', filename)
    
    def generate(file):
        yield ''
        f = open(file, 'rb')
        while True:
            piece = f.read(CHUNK_SIZE)
            print file
            if not piece:
                break
            yield piece
        f.close()
        
    resp = Response(stream_with_context(generate(filepath)))
    resp.headers["Content-Disposition"] = "attachment; filename="+filename
    return resp
    # return send_from_directory(app.static_folder + 'def/', filename, as_attachment=True)
                               
@app.route('/<path:filename>')
def download_js(filename):
    return send_from_directory(app.static_folder + 'obj_loader/', filename)

@app.route("/upload", methods=['GET', 'POST'])
def upload():
    if request.method == 'POST':
        lat = float(request.args.get('lat'))
        lng = float(request.args.get('lng'))
    
        name = str(request.args.get('name'))
        type = str(request.args.get('type'))
        textureType = str(request.args.get('ttype'))
    
        currObj = Object.create(name=name, augmentationType=type, textureType=textureType, lat=lat, lng=lng)
        id = str(currObj.id)
    
        if type == 'image':
            marker = request.files['marker']
            image = request.files['image']
        
            if marker and allowed_file(marker.filename):
                marker.save(os.path.join(app.static_folder + 'def/', id + '.' + marker.filename.rsplit('.', 1)[1]))
                copyfile((app.static_folder + 'def/' + id + '.' + marker.filename.rsplit('.', 1)[1]), app.static_folder + 'sync/_uploads_temp/' + id + '.' + marker.filename.rsplit('.', 1)[1])
            
            if image.filename.rsplit('.', 1)[1] == 'png':
                image.save(os.path.join(app.static_folder + 'sync/_uploads_temp/', id + '_texture.' + image.filename.rsplit('.', 1)[1]))
            
            if image.filename.rsplit('.', 1)[1] == 'jpg':
                image.save(os.path.join(app.static_folder + 'def/', id + '_texture.' + image.filename.rsplit('.', 1)[1]))
        
        if type == 'model':
            marker = request.files['marker']
            object = request.files['object']
            texture = request.files['texture']
        
            if marker and allowed_file(marker.filename):
                marker.save(os.path.join(app.static_folder + 'def/', id + '.' + marker.filename.rsplit('.', 1)[1]))
                copyfile((app.static_folder + 'def/' + id + '.' + marker.filename.rsplit('.', 1)[1]), app.static_folder + 'sync/_uploads_temp/' + id + '.' + marker.filename.rsplit('.', 1)[1])
            
            if object and allowed_file(object.filename):
                object.save(os.path.join(app.static_folder + 'sync/_uploads_temp/', id + '.' + object.filename.rsplit('.', 1)[1]))
                
            if texture and allowed_file(texture.filename):
                texture.save(os.path.join(app.static_folder + 'def/', id + '_texture.' + texture.filename.rsplit('.', 1)[1]))
        
    return '200'
	
def main():
    "Start gevent WSGI server"
    # use gevent WSGI server instead of the Flask
    http = WSGIServer(('', 80), app.wsgi_app)
    # TODO gracefully handle shutdown
    http.serve_forever()
    
if __name__ == '__main__':
    main() # app.run(host="0.0.0.0", port=int("80"), debug=True)