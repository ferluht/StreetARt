package eu.kudan.ar;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
    JSONParserInterface, CMSContentManagementInterface {

    private GoogleMap map;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker marker;
    private boolean flag = true;

    private double lat, lng;

    private JSONParser jsonParser;

    private CMSContentManagement contentManager = new CMSContentManagement(this,this);

    @Override
    public void setUpTrackers(CMSTrackable[] trackers) {
        //Intent intent = new Intent(MapsActivity.this, CMSARView.class);
        //intent.putExtra("trackables", trackers);
        //startActivity(intent);
    }


    @Override
    public void cannotDownload() {
        //displayCantDownload();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
/*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

            buildGoogleApiClient();
            mGoogleApiClient.connect();

        }

        if (map == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap retMap) {

        map = retMap;

        setUpMap();

    }

    public void setUpMap(){
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this,"onConnected", Toast.LENGTH_SHORT).show();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //mLocationRequest.setSmallestDisplacement(0.1F);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            // Show rationale and request permission.
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        //remove previous current location Marker
        /*if (marker != null){
            marker.remove();
        }*/

        /*marker = map.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
                .title("My Location").icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 17));*/

    }

    public void getObjectsNear(View view){
        //fileDownloadInformation = new ArrayList<CMSFileDownloadInformation>();
        double lat = mLastLocation.getLatitude();
        double lng = mLastLocation.getLongitude();
        new JSONParser(this).execute("http://api.arreality.me/?act=get_obj_near&lat=" + Double.toString(lat) + "&lng=" + Double.toString(lng) + "&rad=5000");
    }

    @Override
    public void jsonFinishedDownloading(JSONObject jsonObject) {
        try{
            JSONArray tempJSONArray = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < tempJSONArray.length(); i++) {
                JSONObject tempJSON = (JSONObject) tempJSONArray.get(i);
                double lat = (double) tempJSON.get("lat");
                double lng = (double) tempJSON.get("lng");
                String name = (String) tempJSON.get("name");
                map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                                .title(name));
                //addFileDownloadInformation(tempJSON);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        downloadedJSON= new JSONObject();
        downloadedJSON = jsonObject;
        localJSON = CMSUtilityFunctions.getLocalJSON();
        addFileDownloadInformationFromJSON();

        if (fileDownloadInformation.size() > 0 ) {
            downloadsManager.downloadTrackables(fileDownloadInformation, context, downloadedJSON);
        }
        else {
            finishedDownload();
        }*/
    }

    @Override
    public void couldNotDownloadJSON() {
        //contentManagementInferace.cannotDownload();
    }
}
