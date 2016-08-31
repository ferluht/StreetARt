package eu.kudan.ar.arRealityAPI;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityDownloadQueue extends AsyncTask<Void, Void, Void>
        implements ARRealityDownloaderInterface {

    private ARRealityDownloadQueueInterface arRealityDownloadQueueInterface;
    private List<ARRealityAsset> arRealityAssets;
    private boolean successfull = false;
    private static final int MAX_DOWNLOAD_ATTEMPTS = 3;
    private int overallProgress = 0;
    private int downloadProgress[];
    private boolean isEmpty = true;

    public ARRealityDownloadQueue(ARRealityDownloadQueueInterface inListener){
        this.arRealityDownloadQueueInterface = inListener;
        this.arRealityAssets = new ArrayList<ARRealityAsset>();
    }

    public void addAssetsToQueue(List<ARRealityAsset> tempAssets){
        for(ARRealityAsset tempAsset : tempAssets)
            arRealityAssets.add(tempAsset);
    }

    private void startDownload(ARRealityAsset tempAsset){
        (new ARRealityDownloader(this, tempAsset)).execute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        downloadProgress = new int[arRealityAssets.size()];

        for (int i = 0; i < arRealityAssets.size(); i++) {
            isEmpty = false;
            downloadProgress[i] = 0;
            startDownload(arRealityAssets.get(i));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (isEmpty) {
            try {
                arRealityDownloadQueueInterface.allDownloaded();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void failedToSaveFile(ARRealityAsset arRealityAsset) {
        if(arRealityAsset.downloadAttempts < MAX_DOWNLOAD_ATTEMPTS) {
            arRealityAsset.downloadAttempts++;
            startDownload(arRealityAsset);
        } else{
            arRealityDownloadQueueInterface.failedToDownload(arRealityAsset);
        }
    }

    @Override
    public void cannotDownload(ARRealityAsset arRealityAsset) {
        arRealityDownloadQueueInterface.failedToDownload(arRealityAsset);
    }

    @Override
    public void successfullyDownloaded(ARRealityAsset arRealityAsset) throws IOException {
        arRealityAssets.remove(arRealityAsset);
        overallProgress += 100.0/(float)arRealityAssets.size();
        arRealityDownloadQueueInterface.onProgressChanged(overallProgress);
        if(arRealityAssets.isEmpty()) arRealityDownloadQueueInterface.allDownloaded();
        //else startDownload(arRealityAsset);
    }

    @Override
    public void onDownloadProgressChanged(ARRealityAsset arRealityAsset) {
        int index = arRealityAssets.indexOf(arRealityAsset);
        overallProgress -= downloadProgress[index];
        downloadProgress[index] = arRealityAsset.percentDownloaded;
        overallProgress += downloadProgress[index];
        arRealityDownloadQueueInterface.onProgressChanged(overallProgress);
    }
}
