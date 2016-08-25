package eu.kudan.ar.arRealityAPI;

import android.os.Environment;

import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityServiceGenerator {

    public static final String API_BASE_URL = "http://api.arreality.me";
    public static final String API_ASSETS_RELATIVE_PATH = ".ARReality/Assets";
    public static final String API_ASSETS_ABSOLUTE_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + File.separator + API_ASSETS_RELATIVE_PATH;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
