package eu.kudan.ar.arRealityAPI;

import android.os.AsyncTask;

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
        for (int i = 0; i < arRealityAssets.size(); i++)
            startDownload(arRealityAssets.get(i));

        return null;
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
    public void successfullyDownloaded(ARRealityAsset arRealityAsset) {
        arRealityAssets.remove(arRealityAsset);
        if(arRealityAssets.isEmpty()) arRealityDownloadQueueInterface.allDownloaded();
        //else startDownload(arRealityAsset);
    }
}
