package eu.kudan.ar.arRealityAPI;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityObject {
    private String name, universe, augmentationType, textureType;
    private double lat, lng;
    private int id;
    private String markerPath;
    public List<String> augmentationPath;
    private Quaternion orInArbiTrack;
    private Vector3f fullPos, posInArbiTrack;
    private List<ARRealityAsset> arRealityAssets;
    public boolean isDownloaded;

    public ARRealityObject(){
        augmentationPath = new ArrayList<String>();
        arRealityAssets = new ArrayList<ARRealityAsset>();
        isDownloaded = false;
    }

    public ARRealityObject(int id, String name, String augmentationType, String textureType){
        this.id = id;
        this.name = name;
        this.textureType = textureType;
        this.augmentationType = augmentationType;
        augmentationPath = new ArrayList<String>();
        arRealityAssets = new ArrayList<ARRealityAsset>();
        isDownloaded = false;
        initAssetsWithId();
    }

    public void initAssetsWithId(){
        arRealityAssets.clear();
        String stringId = Integer.toString(id);

        ARRealityAsset markerAsset = new ARRealityAsset(
                stringId + ".KARMarker",
                stringId
        );
        arRealityAssets.add(markerAsset);
        augmentationPath.add(markerAsset.fullPath);

        ARRealityAsset imageMarkerAsset = new ARRealityAsset(
                stringId + ".jpg",
                stringId
        );
        arRealityAssets.add(imageMarkerAsset);
        augmentationPath.add(imageMarkerAsset.fullPath);

        switch (augmentationType){
            case "image":
                if(textureType.equals("png")) textureType = "mp4";
                ARRealityAsset imageAsset = new ARRealityAsset(
                        stringId + "_texture." + textureType,
                        stringId
                );
                arRealityAssets.add(imageAsset);
                augmentationPath.add(imageAsset.fullPath);
                break;
            case "model":
                ARRealityAsset modelAsset = new ARRealityAsset(
                        stringId + ".jet",
                        stringId
                );
                arRealityAssets.add(modelAsset);
                augmentationPath.add(modelAsset.fullPath);

                ARRealityAsset textureAsset = new ARRealityAsset(
                        stringId + "_texture." + textureType,
                        stringId
                );
                arRealityAssets.add(textureAsset);
                augmentationPath.add(textureAsset.fullPath);
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

    public String getTextureType(){
        return textureType;
    }

    public List<ARRealityAsset> getAssets(){
        return arRealityAssets;
    }
}
