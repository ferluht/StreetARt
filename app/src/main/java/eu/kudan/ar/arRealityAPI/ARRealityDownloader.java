package eu.kudan.ar.arRealityAPI;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.kudan.ar.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityDownloader extends AsyncTask <Void, Void, Void> {

    private ARRealityDownloaderInterface arRealityDownloaderInterface;
    private ARRealityAsset arRealityAsset;

    /*private void download(){

    }*/

    public ARRealityDownloader(ARRealityDownloaderInterface inListener, ARRealityAsset tempAsset){
        this.arRealityDownloaderInterface = inListener;
        this.arRealityAsset = tempAsset;
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File thisAsset = Environment.getExternalStoragePublicDirectory(arRealityAsset.filePath + File.separator + arRealityAsset.fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(thisAsset);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    //Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final ARRealityClient arRealityClient = ServiceGenerator.createService(ARRealityClient.class);

        Call<ResponseBody> call = arRealityClient.downloadFile(arRealityAsset.downloadUrl);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.d(TAG, "server contacted and has file");

                    arRealityAsset.downloaded = writeResponseBodyToDisk(response.body());

                    if(arRealityAsset.downloaded) arRealityDownloaderInterface.successfullyDownloaded(arRealityAsset);
                    else arRealityDownloaderInterface.failedToSaveFile(arRealityAsset);
                    //Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    arRealityDownloaderInterface.cannotDownload(arRealityAsset);
                    //Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e(TAG, "error");
            }
        });

        return null;
    }
}
