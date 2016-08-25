package eu.kudan.ar.arRealityAPI;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityObject {
    private AUGMENTATION_TYPE augmentationType;
    private String name, universe;
    private double lat, lng;
    private int id;

    enum AUGMENTATION_TYPE {
        IMAGE,
        MODEL,
        VIDEO,
        AUDIO,
        TEXT
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

    public AUGMENTATION_TYPE getAugmentationType(){
        return augmentationType;
    }
}
