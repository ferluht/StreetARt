package eu.kudan.ar.arRealityAPI;

import java.io.IOException;

/**
 * Created by ferluht on 24.08.2016.
 */
public interface ARRealityDownloadQueueInterface {
    public void allDownloaded() throws IOException;
    public void failedToDownload(ARRealityAsset arRealityAsset);
    public void onProgressChanged(int progress);
}
