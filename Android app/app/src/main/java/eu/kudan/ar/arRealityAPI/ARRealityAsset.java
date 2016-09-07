package eu.kudan.ar.arRealityAPI;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityAsset {
    String filePath, fileName, fullPath;
    boolean downloaded;
    int percentDownloaded;
    int downloadAttempts;

    public ARRealityAsset(String name, String subfolder){
        this.fileName = name;
        this.filePath = ARRealityServiceGenerator.API_ASSETS_ABSOLUTE_PATH
                + File.separator + subfolder;
        this.fullPath = filePath + File.separator + fileName;
        this.downloaded = false;
        this.downloadAttempts = 0;
        this.percentDownloaded = 0;
    }


}
