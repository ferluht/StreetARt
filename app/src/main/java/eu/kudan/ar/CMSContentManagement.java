package eu.kudan.ar;

import android.content.Context;
import android.location.Location;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CMSContentManagement implements CMSDownloadManagerInterface, JSONParserInterface {

    private static final boolean useOwnServer = true;

    private JSONObject downloadedJSON;
    private JSONObject localJSON;
    ArrayList<CMSFileDownloadInformation> fileDownloadInformation;
    ArrayList<CMSTrackable> trackables;
    private CMSContentManagementInterface contentManagementInferace;
    private Context context;
    private CMSDownloadManager downloadsManager;
    private JSONParser jsonParser;
    private Location mCurrentLocation;


    // Sets up listeners
    public CMSContentManagement(CMSContentManagementInterface inListener, Context inContext, Location location) {
        this.contentManagementInferace = inListener;
        this.context = inContext;
        downloadsManager = new CMSDownloadManager (this);
        jsonParser = new JSONParser (this);
        mCurrentLocation = location;
    }


    // Downloads JSON file
    public void getObjectsNear(){
        double lat = mCurrentLocation.getLatitude();
        double lng = mCurrentLocation.getLongitude();
        jsonParser.execute("http://api.arreality.me/?act=get_obj_near&lat=" + Double.toString(lat) + "&lng=" + Double.toString(lng) + "&rad=500");
    }

    // Creates file download information from downloaded json, if the file has been updated
    private void addFileDownloadInformationFromJSON() {
        try {
            fileDownloadInformation = new ArrayList<CMSFileDownloadInformation>();
            if (downloadedJSON != null) {
                if (localJSON == null) {
                    JSONArray tempJSONArray = (JSONArray) downloadedJSON.get("results");
                    for (int i = 0; i < tempJSONArray.length(); i++) {
                        JSONObject tempJSON = (JSONObject) tempJSONArray.get(i);
                        addFileDownloadInformation(tempJSON);

                    }
                } else {
                    JSONArray tempJSONArray = (JSONArray) downloadedJSON.get("results");
                    for (int i = 0; i < tempJSONArray.length(); i++) {
                        JSONObject tempJSON = (JSONObject) tempJSONArray.get(i);
                        /////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//////////
                        if (filesMissingFromDirectory(tempJSON)) {
                            addFileDownloadInformation(tempJSON);
                        }
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addFileDownloadInformation (JSONObject tempJSON) {
        try {
            CMSFileDownloadInformation markerFileDownloadinInformation = new CMSFileDownloadInformation();
            fileDownloadInformation.add(markerFileDownloadinInformation.initMarkerWithJSON(tempJSON));

            if (tempJSON.get("augmentationType").equals("picture")) {
                CMSFileDownloadInformation pictureFileDownloadinInformation = new CMSFileDownloadInformation();

                fileDownloadInformation.add(pictureFileDownloadinInformation.initPictureWithJSON(tempJSON));
            }

            if (tempJSON.get("augmentationType").equals("model")) {
                CMSFileDownloadInformation modelFileDownloadinInformation = new CMSFileDownloadInformation();
                CMSFileDownloadInformation textureFileDownloadinInformation = new CMSFileDownloadInformation();

                fileDownloadInformation.add(modelFileDownloadinInformation.initModelWithJSON(tempJSON));
                fileDownloadInformation.add(textureFileDownloadinInformation.initPictureWithJSON(tempJSON));
            }

            if (tempJSON.get("augmentationType").equals("video")) {
                CMSFileDownloadInformation videoFileDownloadinInformation = new CMSFileDownloadInformation();

                fileDownloadInformation.add(videoFileDownloadinInformation.initVideoWithJSON(tempJSON));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Saves downloaded JSON to local file
    private void saveLocalJSON() {
        try {
            File checkFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + MainActivity.packageName + "/data.json");
            if (checkFile.exists()) {
                checkFile.delete();
            }
            checkFile.createNewFile();
            FileWriter file = new FileWriter (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS +  "/" + MainActivity.packageName + "/data.json"));
            file.write(downloadedJSON.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean filesMissingFromDirectory(JSONObject tempJSON) {
        try {
            File markerFile = Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() + "/Assets/"+tempJSON.getString("id")+"/" + tempJSON.getString("markerFileName"));
            File completedFile =Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() + "/Assets/"+tempJSON.getString("id")+"/" + "completed.txt");

            if (!markerFile.exists()) {
                return true;
            }
            if (!completedFile.exists()) {
                return true;
            }
            if (tempJSON.getString("augmentationType").equals("video")) {
                File videoFile = Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() + "/Assets/"+tempJSON.getString("id")+"/" + tempJSON.getString("augmentationFileName"));
                if(!videoFile.exists()) {
                    return true;
                }
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public void finishedDownload() {
        if (fileDownloadInformation.size() > 0 ) {
            saveLocalJSON();
        }
        localJSON = CMSUtilityFunctions.getLocalJSON();
        trackables = CMSUtilityFunctions.loadTrackablesFromJSONObject(localJSON);
        CMSTrackable[] trackablesArray = new CMSTrackable[trackables.size()];
        trackables.toArray(trackablesArray);
        contentManagementInferace.setUpTrackers(trackablesArray);
    }


    @Override
    public void couldNotDownloadFile() {
        contentManagementInferace.cannotDownload();
    }


    @Override
    public void jsonFinishedDownloading(JSONObject jsonObject) {
        downloadedJSON = new JSONObject();
        downloadedJSON = jsonObject;
        localJSON = CMSUtilityFunctions.getLocalJSON();
        contentManagementInferace.setUpMapMarkers(downloadedJSON);
        addFileDownloadInformationFromJSON();

        if (fileDownloadInformation.size() > 0 ) {
            downloadsManager.downloadTrackables(fileDownloadInformation, context, downloadedJSON);
        }
        else {
            finishedDownload();
        }
    }


    @Override
    public void couldNotDownloadJSON() {
        contentManagementInferace.cannotDownload();
    }
}
