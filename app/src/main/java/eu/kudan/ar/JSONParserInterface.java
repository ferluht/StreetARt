package eu.kudan.ar;

import org.json.JSONObject;

public interface JSONParserInterface {
    public void jsonFinishedDownloading(JSONObject jsonObject);
    public void couldNotDownloadJSON();
}
