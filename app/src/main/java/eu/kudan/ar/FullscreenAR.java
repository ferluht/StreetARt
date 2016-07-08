package eu.kudan.ar;

import android.os.Bundle;
import android.util.Log;

import eu.kudan.kudan.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class FullscreenAR extends ARActivity implements ARImageTrackableListener {
    public void onCreate(Bundle savedInstanceState) {
        // set api key for this package name.
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("GAWQE-F9AQU-2G87F-8HKED-Q7BTG-TY29G-RV85A-XN3ZP-A9KGM-E8LB6-VC2XW-VTKAK-ANJLG-2P8NX-UZMAH-Q");
        super.onCreate(savedInstanceState);
    }

    public void setup() {

        // load a set of trackables from a bundled file.
        ARTrackableSet trackableSet = new ARTrackableSet();
        trackableSet.loadFromAsset("test.KARMarker");

        ARImageTracker tracker = ARImageTracker.getInstance();

        // add our trackables to the tracker.
        tracker.addTrackableSet(trackableSet);

        // create an image node.
        ARImageTrackable legoTrackable = tracker.findTrackable("lego");
        ARImageNode imageNode = new ARImageNode("BatmanLegoMovie.png");

        // make it smaller.
        imageNode.scaleBy(0.5f, 0.5f, 0.5f);

        // add it to the lego trackable.
        legoTrackable.getWorld().addChild(imageNode);
    }

    @Override
    public void didDetect(ARImageTrackable trackable) {
        Log.i("KudanSamples", "detected " + trackable.getName());
    }


    @Override
    public void didTrack(ARImageTrackable trackable) {
//		Log.i("KudanSamples", "tracked");
    }

    @Override
    public void didLose(ARImageTrackable trackable) {
        Log.i("KudanSamples", "lost " + trackable.getName());
    }
}