package eu.kudan.ar.arRealityAPI;

/**
 * Created by ferluht on 24.08.2016.
 */
public interface ARRealityDownloaderInterface {
    public void failedToSaveFile(ARRealityAsset arRealityAsset);
    public void cannotDownload(ARRealityAsset arRealityAsset);
    public void successfullyDownloaded(ARRealityAsset arRealityAsset);
}
