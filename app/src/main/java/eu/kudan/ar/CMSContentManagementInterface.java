package eu.kudan.ar;

import org.json.JSONObject;

public interface CMSContentManagementInterface {
    public void setUpTrackers(CMSTrackable[] trackers);
    public void setUpMapMarkers(JSONObject jsonObject);
    public void cannotDownload();
}
