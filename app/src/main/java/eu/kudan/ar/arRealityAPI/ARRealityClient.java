package eu.kudan.ar.arRealityAPI;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
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

    //@Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
