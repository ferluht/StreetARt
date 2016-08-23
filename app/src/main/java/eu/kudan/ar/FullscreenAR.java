package eu.kudan.ar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import org.json.JSONObject;

import java.util.ArrayList;

import eu.kudan.kudan.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class FullscreenAR extends ARActivity implements ARImageTrackableListener, ARArbiTrackListener {

    private CMSTrackable[] trackables;
    private ARImageTrackable trackable;
    private ARModelNode modelNode;
    private ARBITRACK_STATE arbitrack_state;

    @Override
    public void didDetect(ARImageTrackable arImageTrackable) {
        ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();
        arArbiTrack.start();
    }

    @Override
    public void didTrack(ARImageTrackable arImageTrackable) {

    }

    @Override
    public void didLose(ARImageTrackable arImageTrackable) {

    }

    @Override
    public void arbiTrackStarted() {
        // Get ArbiTrack instance
        ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();
        // Get model nodes position relative to camera
        Vector3f fullPos = arArbiTrack.getTargetNode().getFullTransform().mult(Vector3f.ZERO);
        // Get models position relative to ArbiTracks world.
        Vector3f posInArbiTrack = arArbiTrack.getWorld().getFullTransform().invert().mult(fullPos);
        // Get models orientation relative to ArbiTracks world.
        Quaternion orInArbiTrack = arArbiTrack.getWorld().getFullOrientation().inverse().mult((modelNode.getFullOrientation()));
        // Add the model node as a child of ArbiTrack
        arArbiTrack.getWorld().addChild(modelNode);
        // Change model nodes position to be relative to the marker nodes world
        modelNode.setPosition(posInArbiTrack);
        // Change model nodes orientation to be relative to the marker nodes world
        modelNode.setOrientation(orInArbiTrack);
    }

    //Tracking enum
    enum ARBITRACK_STATE {
        ARBI_PLACEMENT,
        ARBI_TRACKING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set api key for this package name.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_ar);
        //arbitrack_state  = ARBITRACK_STATE.ARBI_PLACEMENT;
    }

    private void setupTrackables() {
        ARImageTracker tracker = ARImageTracker.getInstance();
        for (CMSTrackable tempTrackable : trackables) {

            ARTrackableSet trackableSet = new ARTrackableSet();
            trackableSet.loadFromPath(tempTrackable.getMarkerFilePath());
            tracker.addTrackableSet(trackableSet);

            if (tempTrackable.getAugmentationType().equals("model")) {
                addModel(trackableSet,tempTrackable);
            }
        }
    }

    private void addModel(ARTrackableSet trackableSet, CMSTrackable tempTrackable) {
        for (ARImageTrackable imageTrackable : trackableSet.getTrackables()) {

            ARModelImporter modelImporter = new ARModelImporter();
            modelImporter.loadFromPath(tempTrackable.getAugmentationFilePath());
            modelNode = (ARModelNode) modelImporter.getNode();

            // Load model texture
            ARTexture2D texture2D = new ARTexture2D();
            texture2D.loadFromPath(tempTrackable.getTextureFilePath());
            //texture2D.loadFromPath(tempTrack.getTextureFilePath());

            // Apply model texture to model texture material
            ARLightMaterial material = new ARLightMaterial();
            material.setTexture(texture2D);
            material.setAmbient(0.8f, 0.8f, 0.8f);

            // Apply texture material to models mesh nodes
            for (ARMeshNode meshNode : modelImporter.getMeshNodes()) {
                meshNode.setMaterial(material);
            }

            modelNode.scaleByUniform(0.25f);


            trackable = new ARImageTrackable(Integer.toString(tempTrackable.getId()));
            trackable.loadFromPath(tempTrackable.getImageNodeFilePath());
            // Initialise image node
            // ARImageNode imageNode = new ARImageNode("texture1.jpg");

            //imageTrackable.getWorld().addChild(modelNode);
            imageTrackable.addListener(this);

            ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();
            arArbiTrack.initialise();

            // Add Activity to ARArbiTrack listeners
            arArbiTrack.addListener(this);

            // Create empty target node
            ARNode targetNode = new ARNode();
            targetNode.setName("targetNode");

            // Add target node to image trackable world
            imageTrackable.getWorld().addChild(targetNode);

            // Set blank node as target node for ArbiTrack
            arArbiTrack.setTargetNode(targetNode);
        }
    }

    public void lockPosition(View view) {

        Button b = (Button)findViewById(R.id.lockButton);
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();

        // If in placement mode start arbi track, hide target node and alter label
        if(arbitrack_state.equals(ARBITRACK_STATE.ARBI_PLACEMENT)) {

            //Start Arbi Track
            arbiTrack.start();

            //Hide target node
            arbiTrack.getTargetNode().setVisible(false);

            //Change enum and label to reflect Arbi Track state
            arbitrack_state = ARBITRACK_STATE.ARBI_TRACKING;
            b.setText("Stop Tracking");


        }

        // If tracking stop tracking, show target node and alter label
        else {

            // Stop Arbi Track
            arbiTrack.stop();

            // Display target node
            arbiTrack.getTargetNode().setVisible(true);

            //Change enum and label to reflect Arbi Track state
            arbitrack_state = ARBITRACK_STATE.ARBI_PLACEMENT;
            b.setText("Start Tracking");

        }

    }

    //@Override
    public void setup() {
        //super.setup();
        JSONObject tempJson = CMSUtilityFunctions.getLocalJSON();
        ArrayList<CMSTrackable> track = CMSUtilityFunctions.loadTrackablesFromJSONObject(tempJson);
        trackables = track.toArray(new CMSTrackable[track.size()]);
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