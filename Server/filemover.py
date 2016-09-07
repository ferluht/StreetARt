from os import listdir, remove, rename
from os.path import isfile, join
import shutil

assetsPath = 'sync/_assets_ready'
defPath = 'def/'

while(True):
    assets = [f for f in listdir(assetsPath) if isfile(join(assetsPath, f))]
    if assets:
        for fil in assets:
            name = fil.rsplit('.')[0]
            extension = fil.rsplit('.', 1)[1]
            if extension == "armodel":
                shutil.move(assetsPath + '/' + fil, defPath + '/' + name + '.jet')
                print name+'.jet' + " moved\r"
            else:
                shutil.move(assetsPath + '/' + fil, defPath + '/' + fil)
                print fil + " moved\r"