package eu.kudan.ar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jme3.math.Vector3f;

import eu.kudan.kudan.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class FullscreenAR extends ARActivity implements ARImageTrackableListener {

    private CMSTrackable[] trackables;

    public void onCreate(Bundle savedInstanceState) {
        // set api key for this package name.
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("GAWQE-F9AQU-2G87F-8HKED-Q7BTG-TY29G-RV85A-XN3ZP-A9KGM-E8LB6-VC2XW-VTKAK-ANJLG-2P8NX-UZMAH-Q");
        super.onCreate(savedInstanceState);
    }

    // Adds Trackables to ARImageTracker
    private void setupTrackables() {
        ARImageTracker tracker = ARImageTracker.getInstance();
        for (CMSTrackable tempTrackable : trackables) {

            ARTrackableSet trackableSet = new ARTrackableSet();
            trackableSet.loadFromPath(tempTrackable.getMarkerFilePath());
            tracker.addTrackableSet(trackableSet);

            if (tempTrackable.getAugmentationType().equals("video")) {
                addVideo(trackableSet,tempTrackable);
            }
            else {
                addText(trackableSet, tempTrackable);
            }
        }
    }

    // Adds video nodes to trackable set
    private void addVideo(ARTrackableSet trackableSet, CMSTrackable tempTrackable) {
        for (ARImageTrackable imageTrackable : trackableSet.getTrackables()) {

            ARVideoTexture videoTexture = new ARVideoTexture();
            videoTexture.loadFromPath(tempTrackable.getAugmentationFilePath());
            ARVideoNode videoNode = new ARVideoNode(videoTexture);
            videoNode.rotateByDegrees(tempTrackable.getAugmentationRoatation(),0,0,1);
            float scale;

            if (tempTrackable.getAugmentationRoatation() == 90) {
                scale = imageTrackable.getWidth() / videoNode.getVideoTexture().getHeight();
            }
            else {
                scale = imageTrackable.getWidth() / videoNode.getVideoTexture().getWidth();
            }

            videoNode.scaleByUniform(scale);
            imageTrackable.getWorld().addChild(videoNode);
            imageTrackable.addListener(this);
        }
    }

    private void addText(ARTrackableSet trackableSet, CMSTrackable tempTrackable) {
        for (ARImageTrackable imageTrackable : trackableSet.getTrackables()) {
            imageTrackable.getWorld().setName("text");
            imageTrackable.addListener(this);
        }
    }

    @Override
    public void didDetect(ARImageTrackable arImageTrackable) {
        if (arImageTrackable.getWorld().getName().equals("text")) {

            final ARImageTrackable ar = arImageTrackable;
            this.runOnUiThread(new Runnable() {
                public void run() {
                    TextView tv = (TextView) findViewById(R.id.albumTitle);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(ar.getName());
                }
            });
        }
    }


    @Override
    public void didTrack(ARImageTrackable arImageTrackable) {

    }


    @Override
    public void didLose(ARImageTrackable arImageTrackable) {
        final ARImageTrackable ar = arImageTrackable;
        this.runOnUiThread(new Runnable() {
            public void run() {
                TextView tv = (TextView) findViewById(R.id.albumTitle);
                tv.setVisibility(View.GONE);
                tv.setText(ar.getName());
            }
        });
    }

    @Override
    public void setup() {
        super.setup();
        Intent in = getIntent();
        Parcelable[] parcelables = in.getExtras().getParcelableArray("trackables");
        trackables = new CMSTrackable[parcelables.length];

        for (int i = 0; i < parcelables.length; i++) {
            trackables[i] = (CMSTrackable)parcelables[i];
        }

        setupTrackables();
    }

    /*public void setup() {
        ARImageTrackable trackable = new ARImageTrackable("komus");
        trackable.loadFromAsset("komus.jpg");

        // Get instance of image tracker manager
        ARImageTracker trackableManager = ARImageTracker.getInstance();

        // Add image trackable to image tracker manager
        trackableManager.addTrackable(trackable);

        // Import model
        ARModelImporter modelImporter = new ARModelImporter();
        modelImporter.loadFromAsset("technostart.jet");
        ARModelNode modelNode = (ARModelNode)modelImporter.getNode();

        // Load model texture
        ARTexture2D texture2D = new ARTexture2D();
        texture2D.loadFromAsset("texture1.jpg");

        ARLightMaterial material = new ARLightMaterial();
        material.setTexture(texture2D);
        material.setAmbient(0.8f, 0.8f, 0.8f);
        // Apply model texture to model texture material
        //ARColourMaterial material = new ARColourMaterial();
        //material.setTexture(texture2D);
        //material.setColour(new Vector3f(0.8f, 0.8f, 0.8f));

        // Apply texture material to models mesh nodes
        for(ARMeshNode meshNode : modelImporter.getMeshNodes()){
            meshNode.setMaterial(material);
        }


        modelNode.rotateByDegrees(90,1,0,0);
        modelNode.scaleByUniform(0.25f);

        // Add model node to image trackable
        trackable.getWorld().addChild(modelNode);
        modelNode.setVisible(true);
    }*/
}