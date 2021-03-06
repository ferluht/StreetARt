package eu.kudan.ar.arRealityAPI;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by ferluht on 24.08.2016.
 */
public interface ARRealityClient {
    @GET("/objects")
    Call<List<ARRealityObject>> objects(
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("rad") double rad
    );

    @GET("/object")
    Call<List<ARRealityObject>> object(
            @Query("id") int id
    );

    //@Streaming
    @GET("/download/{file}")
    Call<ResponseBody> downloadFile(
            @Path("file") String fileName
    );

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImage(
            @Part MultipartBody.Part marker,
            @Part MultipartBody.Part image,
            @Query("name") String name,
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("type") String type,
            @Query("ttype") String ttype
    );

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadObject(
            @Part MultipartBody.Part marker,
            @Part MultipartBody.Part object,
            @Part MultipartBody.Part texture,
            @Query("name") String name,
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("type") String type,
            @Query("ttype") String ttype
    );
}
