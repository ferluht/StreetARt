package eu.kudan.ar.arRealityAPI;

import java.io.IOException;

/**
 * Created by ferluht on 24.08.2016.
 */
public interface ARRealityDownloaderInterface {
    public void failedToSaveFile(ARRealityAsset arRealityAsset);
    public void cannotDownload(ARRealityAsset arRealityAsset);
    public void successfullyDownloaded(ARRealityAsset arRealityAsset) throws IOException;
    public void onDownloadProgressChanged(ARRealityAsset arRealityAsset);
}
