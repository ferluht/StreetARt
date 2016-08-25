package eu.kudan.ar.arRealityAPI;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityAsset {
    String downloadUrl, filePath, fileName;
    boolean downloaded;
    int downloadAttempts;

    public ARRealityAsset(String url, String path, String name){
        this.downloadUrl = url;
        this.fileName = name;
        this.filePath = path;
        this.downloaded = false;
        this.downloadAttempts = 0;
    }
}
