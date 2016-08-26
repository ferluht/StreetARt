package eu.kudan.ar.arRealityAPI;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ferluht on 25.08.2016.
 */
public class ARRealityObjectUploader extends AsyncTask<Void, Void, Void> {

    String markerFilePath, objectFilePath, textureFilePath;
    Location currLocation;
    String name;

    public ARRealityObjectUploader(String tMarkerFilePath, String tObjectFilePath,
                                   String tTextureFilePath, Location tCurrentLocation,
                                   String tName){
        this.markerFilePath = tMarkerFilePath;
        this.objectFilePath = tObjectFilePath;
        this.textureFilePath = tTextureFilePath;
        this.currLocation = tCurrentLocation;
        this.name = tName;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // create upload service client
        ARRealityClient service =
                ARRealityServiceGenerator.createService(ARRealityClient.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File marker = new File(markerFilePath);
        // create RequestBody instance from file
        RequestBody requestMarker =
                RequestBody.create(MediaType.parse("multipart/form-data"), marker);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part markerBody =
                MultipartBody.Part.createFormData("marker", marker.getName(), requestMarker);

        File object = new File(objectFilePath);
        // create RequestBody instance from file
        RequestBody requestObject =
                RequestBody.create(MediaType.parse("multipart/form-data"), object);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part objectBody =
                MultipartBody.Part.createFormData("object", object.getName(), requestObject);

        File texture = new File(textureFilePath);
        // create RequestBody instance from file
        RequestBody requestTexture =
                RequestBody.create(MediaType.parse("multipart/form-data"), texture);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part textureBody =
                MultipartBody.Part.createFormData("texture", texture.getName(), requestTexture);



        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.uploadObject(markerBody, objectBody, textureBody, name,
                currLocation.getLatitude(), currLocation.getLongitude(), "model");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

        return null;
    }
}