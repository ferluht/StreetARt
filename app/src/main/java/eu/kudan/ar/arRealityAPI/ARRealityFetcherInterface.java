package eu.kudan.ar.arRealityAPI;

import java.util.List;

/**
 * Created by ferluht on 24.08.2016.
 */
public interface ARRealityFetcherInterface {
    public void setUpMapMarkers(List<ARRealityObject> arRealityObjects);
    public void objectsUpdated(List<ARRealityObject> arRealityObjects);
}
