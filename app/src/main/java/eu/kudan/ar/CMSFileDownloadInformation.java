package eu.kudan.ar;

import org.json.JSONException;
import org.json.JSONObject;

public class CMSFileDownloadInformation
{
    private String fileTitle;
    private int fileId;
    private String downloadSource;
    private double downloadProgress;
    private Boolean isDownloading;
    private Boolean downloadIscomplete;
    private long taskIdentifier;

    private static String url = "http://api.arreality.me/?act=load_asset&uni=";

    public CMSFileDownloadInformation initMarkerWithJSON(JSONObject jsonObject)
    {
        try
        {
            fileId = (int) jsonObject.get("id");
            downloadSource = url + jsonObject.get("universe") +
                    "&fil=" + Integer.toString(fileId) + ".KARMarker";
            fileTitle = Integer.toString(fileId) + ".KARMarker";
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        startInit();

        return this;
    }

    public CMSFileDownloadInformation initVideoWithJSON(JSONObject jsonObject) {
        try {
            fileId = (int) jsonObject.get("id");
            downloadSource = url + jsonObject.get("universe") +
                    "&fil=" + Integer.toString(fileId) + ".mp4";
            fileTitle = Integer.toString(fileId) + ".mp4";

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        startInit();

        return this;
    }

    public CMSFileDownloadInformation initPictureWithJSON(JSONObject jsonObject) {
        try {
            fileId = (int) jsonObject.get("id");
            downloadSource = url + jsonObject.get("universe") +
                    "&fil=" + Integer.toString(fileId) + ".jpg";
            fileTitle = Integer.toString(fileId) + ".jpg";

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        startInit();

        return this;
    }

    public CMSFileDownloadInformation initModelWithJSON(JSONObject jsonObject) {
        try {
            fileId = (int) jsonObject.get("id");
            downloadSource = url + jsonObject.get("universe") +
                    "&fil=" + Integer.toString(fileId) + ".jet";
            fileTitle = Integer.toString(fileId) + ".jet";

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        startInit();

        return this;
    }

    private void startInit(){
        downloadProgress = 0.0;
        isDownloading = false;
        downloadIscomplete = false;
        taskIdentifier = -1;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getDownloadSource() {
        return downloadSource;
    }

    public void setDownloadSource(String downloadSource) {
        this.downloadSource = downloadSource;
    }

    public double getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(double downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public Boolean getIsDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(Boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    public Boolean getDownloadIscomplete() {
        return downloadIscomplete;
    }

    public void setDownloadIscomplete(Boolean downloadIscomplete) {
        this.downloadIscomplete = downloadIscomplete;
    }

    public long getTaskIdentifier () {
        return taskIdentifier;
    }

    public void setTaskIdentifier (long taskIdentifier) {
        this.taskIdentifier = taskIdentifier;
    }
}
