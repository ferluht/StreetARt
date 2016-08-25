package eu.kudan.ar.arRealityAPI;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityObject {
    private String name, universe, augmentationType;
    private double lat, lng;
    private int id;
    private String markerPath;
    private List<String> augmentationPath;
    private Quaternion orInArbiTrack;
    private Vector3f fullPos, posInArbiTrack;
    private List<ARRealityAsset> arRealityAssets;

    public ARRealityObject(){
        augmentationPath = new ArrayList<String>();
        arRealityAssets = new ArrayList<ARRealityAsset>();
    }

    public void initWithFetchedData(){
        arRealityAssets.clear();
        String stringId = Integer.toString(id);
        switch (augmentationType){
            case "image":
                arRealityAssets.add(new ARRealityAsset(
                        stringId + ".jpg",
                        stringId
                ));
                break;
            case "model":
                arRealityAssets.add(new ARRealityAsset(
                        stringId + ".jpg",
                        stringId
                ));
                arRealityAssets.add(new ARRealityAsset(
                        stringId + ".jet",
                        stringId
                ));
                arRealityAssets.add(new ARRealityAsset(
                        stringId + "_texture" + ".jpg",
                        stringId
                ));
                arRealityAssets.add(new ARRealityAsset(
                        stringId + ".KARMarker",
                        stringId
                ));
                break;
            case "video":

                break;
            case "audio":

                break;
            case "text":

                break;
        }
    }

    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getAugmentationType(){
        return augmentationType;
    }

    public List<ARRealityAsset> getAssets(){
        return arRealityAssets;
    }
}
