package eu.kudan.ar.arRealityAPI;

/**
 * Created by ferluht on 24.08.2016.
 */
public interface ARRealityDownloadQueueInterface {
    public void allDownloaded();
    public void failedToDownload(ARRealityAsset arRealityAsset);
}
