package com.example.ilya.kudansmaple;

import android.os.Bundle;
import android.util.Log;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARTrackableSet;
import eu.kudan.kudan.ARVideoNode;
import eu.kudan.kudan.ARVideoTexture;


public class MainActivity extends ARActivity implements ARImageTrackableListener{

    public void onCreate(Bundle savedInstanceState) {
        // set api key for this package name.
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("GAWAE-FBVCC-XA8ST-GQVZV-93PQB-X7SBD-P6V4W-6RS9C-CQRLH-78YEU-385XP-T6MCG-2CNWB-YK8SR-8UUQ");
        super.onCreate(savedInstanceState);
    }

    public void setup() {
        // create a trackable from a bundled image.
        ARImageTrackable wavesTrackable = new ARImageTrackable("waves");
        wavesTrackable.loadFromAsset("waves.png");

        // create video texture.
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromAsset("waves.mp4");
        ARVideoNode videoNode = new ARVideoNode(videoTexture);

        // add video to the waves trackable.
        wavesTrackable.getWorld().addChild(videoNode);

        // load a set of trackables from a bundled file.
        ARTrackableSet trackableSet = new ARTrackableSet();
        trackableSet.loadFromAsset("demo.KARMarker");

        ARImageTracker tracker = ARImageTracker.getInstance();

        // add our trackables to the tracker.
        tracker.addTrackableSet(trackableSet);
        tracker.addTrackable(wavesTrackable);

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
