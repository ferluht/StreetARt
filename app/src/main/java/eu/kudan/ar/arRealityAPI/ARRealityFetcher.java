package eu.kudan.ar.arRealityAPI;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.IOException;
import java.util.List;

import eu.kudan.ar.ServiceGenerator;
import retrofit2.Call;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityFetcher extends AsyncTask<Void, Void, Void>
        implements ARRealityDownloadQueueInterface{

    private static final int rad = 500;
    private ARRealityClient arRealityClient;
    private List<ARRealityObject> objects;
    private Call<List<ARRealityObject>> call;
    private ARRealityFetcherInterface arRealityFetcherInterface;
    private Location mCurLoc;

    public ARRealityFetcher(ARRealityFetcherInterface inListener, Location location) {
        this.arRealityFetcherInterface = inListener;
        this.mCurLoc = location;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        arRealityClient = ServiceGenerator.createService(ARRealityClient.class);
        call = arRealityClient.objects(mCurLoc.getLatitude(), mCurLoc.getLongitude(), 500);

        try {
            objects = call.execute().body();
        } catch (IOException e) {
            objects = null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        arRealityFetcherInterface.setUpMapMarkers(objects);
        ARRealityDownloadQueue arRealityDownloadQueue = new ARRealityDownloadQueue(this);
        for (int i = 0; i < objects.size(); i++) {
            ARRealityObject tempObj = objects.get(i);
            arRealityDownloadQueue.addAssetToQueue(new ARRealityAsset(
                    "http://api.arreality.me/download/" + Integer.toString(tempObj.id) + ".jpg",
                    Environment.DIRECTORY_DOWNLOADS,
                    Integer.toString(tempObj.id) + ".jpg"
            ));
        }
        arRealityDownloadQueue.execute();
    }

    @Override
    public void allDownloaded() {

    }

    @Override
    public void failedToDownload(ARRealityAsset arRealityAsset) {

    }
}
