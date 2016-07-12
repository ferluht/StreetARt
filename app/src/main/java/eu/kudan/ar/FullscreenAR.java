package eu.kudan.ar;

import android.os.Bundle;
import android.util.Log;

import eu.kudan.kudan.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class FullscreenAR extends ARActivity {
    public void onCreate(Bundle savedInstanceState) {
        // set api key for this package name.
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("GAWQE-F9AQU-2G87F-8HKED-Q7BTG-TY29G-RV85A-XN3ZP-A9KGM-E8LB6-VC2XW-VTKAK-ANJLG-2P8NX-UZMAH-Q");
        super.onCreate(savedInstanceState);
    }

    public void setup() {
        ARImageTrackable trackable = new ARImageTrackable("test");
        trackable.loadFromAsset("lego.jpg");

<<<<<<< HEAD
        // Get instance of image tracker manager
        ARImageTracker trackableManager = ARImageTracker.getInstance();
=======
        // load a set of trackables from a bundled file.
        //ARTrackableSet trackableSet = new ARTrackableSet();
        //trackableSet.loadFromAsset("TV.KARMarker");

        ARImageTrackable trackable = new ARImageTrackable("TV");
        trackable.loadFromAsset("Markers/TV.jpg");
>>>>>>> origin/master

        // Add image trackable to image tracker manager
        trackableManager.addTrackable(trackable);

<<<<<<< HEAD
        // Import model
        ARModelImporter modelImporter = new ARModelImporter();
        modelImporter.loadFromAsset("testmodel.jet");
        ARModelNode modelNode = (ARModelNode)modelImporter.getNode();

        // Load model texture
        ARTexture2D texture2D = new ARTexture2D();
        texture2D.loadFromAsset("texture.jpg");

        // Apply model texture to model texture material
        ARLightMaterial material = new ARLightMaterial();
        material.setTexture(texture2D);
        material.setAmbient(0.8f, 0.8f, 0.8f);

        // Apply texture material to models mesh nodes
        for(ARMeshNode meshNode : modelImporter.getMeshNodes()){
            meshNode.setMaterial(material);
        }
=======
        // add our trackables to the tracker.
        //tracker.addTrackableSet(trackableSet);
        tracker.addTrackable(trackable);

        // create an image node. а картинку вмеоо обьекта можешь? это и есть картинка. ты имеешь в виде бэтмана? д
        ARImageTrackable tvTrackable = tracker.findTrackable("TV");//tvTrackable == null
        ARModelImporter importer = new ARModelImporter();
        importer.loadFromAsset("output.jet");

        ARModelNode imageNode = (ARModelNode) importer.getNode();
        //ARImageNode imageNode = new ARImageNode("BatmanLegoMovie.png");

        ARLightMaterial material = new ARLightMaterial();
        material.setAmbient(0.8f, 0.8f, 0.8f);

        for (ARMeshNode meshNode: importer.getMeshNodes()){
            meshNode.setMaterial(material);
        }

        // make it smaller.
        //imageNode.scaleBy(0.5f, 0.5f, 0.5f);

        // add it to the lego trackable.
        tvTrackable.getWorld().addChild(imageNode);
    }

    @Override
    public void didDetect(ARImageTrackable trackable) {
        Log.i("KudanSamples", "detected " + trackable.getName());
    }
>>>>>>> origin/master


        modelNode.rotateByDegrees(90,1,0,0);
        modelNode.scaleByUniform(0.25f);

        // Add model node to image trackable
        trackable.getWorld().addChild(modelNode);
        modelNode.setVisible(true);
    }
}