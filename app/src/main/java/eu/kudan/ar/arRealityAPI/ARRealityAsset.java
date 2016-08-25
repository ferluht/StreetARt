package eu.kudan.ar.arRealityAPI;

import java.io.File;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityAsset {
    String filePath, fileName;
    boolean downloaded;
    int downloadAttempts;

    public ARRealityAsset(String name, String subfolder){
        this.fileName = name;
        this.filePath = ARRealityServiceGenerator.API_ASSETS_ABSOLUTE_PATH
                + File.separator + subfolder;
        this.downloaded = false;
        this.downloadAttempts = 0;
    }
}
