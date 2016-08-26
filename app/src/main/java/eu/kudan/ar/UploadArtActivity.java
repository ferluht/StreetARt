package eu.kudan.ar;

import android.*;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.kudan.ar.arRealityAPI.ARRealityImageUploader;
import eu.kudan.ar.arRealityAPI.ARRealityObjectUploader;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class UploadArtActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        PageFragmentInterface, LocationListener {

    static final String TAG = "myLogs";
    static final int PAGE_COUNT = 10;

    private ViewPager pager = null;
    private MainPagerAdapter pagerAdapter = null;

    private Location currentLocation;
    private LocationRequest mLocationRequest;

    private GoogleApiClient mGoogleApiClient;

    String augmentationType;

    static final int SELECT_MARKER = 1;
    static final int SELECT_OBJECT = 2;
    static final int SELECT_TEXTURE = 3;

    private String markerFilePath, imageFilePath, objectFilePath, textureFilePath;

    List<String> objectExtensions = Arrays.asList("obj", "fbx");
    List<String> imageExtensions = Arrays.asList("jpg", "png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_art);

        Bundle extras = getIntent().getExtras();
        Boolean welcome = false;
        if (extras != null) {
            welcome = extras.getBoolean("NO_EXISTING_OBJECTS");
            //The key argument here must match that used in the other activity
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MainPagerAdapter();
        pager.setAdapter(pagerAdapter);

        // Create an initial view to display; must be a subclass of FrameLayout.
        LayoutInflater inflater = this.getLayoutInflater();


        if (welcome) {
            FrameLayout welcome_view = (FrameLayout) inflater.inflate(R.layout.welcome_view, null);
            pagerAdapter.addView(welcome_view, 0);
        } else {
            FrameLayout pick_marker_view = (FrameLayout) inflater.inflate(R.layout.pick_marker_view, null);
            pagerAdapter.addView(pick_marker_view, 0);
        }
    }

    private String getPathFromUri(Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_MARKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }
                Uri tUri = data.getData();

                markerFilePath = getPathFromUri(tUri);

                ImageButton ib = (ImageButton) findViewById(R.id.markerUploadButton);
                ib.setBackgroundResource(R.drawable.button_frame);
                ib.setImageURI(tUri);
                ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ib.setPadding(40, 40, 40, 40);

                //TextView tv = (TextView) findViewById(R.id.)

                LayoutInflater inflater = this.getLayoutInflater();
                FrameLayout pick_object_view = (FrameLayout) inflater.inflate(R.layout.pick_object_view, null);
                addView(pick_object_view);
                setCurrentPage(pick_object_view);
                //new ARRealityUploader(this, selectedImage).execute();
            }
        }

        if (requestCode == SELECT_TEXTURE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }
                Uri tUri = data.getData();

                textureFilePath = getPathFromUri(tUri);

                ImageButton ib = (ImageButton) findViewById(R.id.textureUploadButton);
                ib.setBackgroundResource(R.drawable.button_frame);
                ib.setImageURI(tUri);
                ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ib.setPadding(40, 40, 40, 40);

                LayoutInflater inflater = this.getLayoutInflater();
                FrameLayout upload_view = (FrameLayout) inflater.inflate(R.layout.set_name_and_upload_view, null);
                addView(upload_view);
                setCurrentPage(upload_view);
            }
        }

        if (requestCode == SELECT_OBJECT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                Uri tUri = data.getData();

                String tFilePath = getPathFromUri(tUri);

                String extension = tFilePath.substring(tFilePath.lastIndexOf(".") + 1);

                if (imageExtensions.contains(extension)) {
                    ImageButton ib = (ImageButton) findViewById(R.id.objectUploadButton);
                    ib.setBackgroundResource(R.drawable.button_frame);
                    ib.setImageURI(tUri);
                    ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    ib.setPadding(40, 40, 40, 40);

                    imageFilePath = tFilePath;
                    augmentationType = "image";

                    LayoutInflater inflater = this.getLayoutInflater();
                    FrameLayout upload_view = (FrameLayout) inflater.inflate(R.layout.set_name_and_upload_view, null);
                    addView(upload_view);
                    setCurrentPage(upload_view);
                }

                if (objectExtensions.contains(extension)) {
                    ImageButton ib = (ImageButton) findViewById(R.id.objectUploadButton);
                    ib.setBackgroundResource(R.drawable.button_frame);
                    ib.setImageResource(R.drawable.object_icon);
                    ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    ib.setPadding(40, 40, 40, 40);

                    objectFilePath = tFilePath;
                    augmentationType = "model";

                    LayoutInflater inflater = this.getLayoutInflater();
                    FrameLayout pick_object_view = (FrameLayout) inflater.inflate(R.layout.pick_texture_view, null);
                    addView(pick_object_view);
                    setCurrentPage(pick_object_view);
                }
            }
        }
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addView(View newPage) {
        int pageIndex = pagerAdapter.addView(newPage);
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem(pageIndex, true);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView(View defunctPage) {
        int pageIndex = pagerAdapter.removeView(pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem(pageIndex);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage() {
        return pagerAdapter.getView(pager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage(View pageToShow) {
        pager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
    }

    public void StartWizard(View view) {
        LayoutInflater inflater = this.getLayoutInflater();
        FrameLayout pick_marker_view = (FrameLayout) inflater.inflate(R.layout.pick_marker_view, null);
        addView(pick_marker_view);
        View toRemove = getCurrentPage();
        setCurrentPage(pick_marker_view);
    }

    public void onMarkerUploadClick(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("*/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("*/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, SELECT_MARKER);
    }

    public void onObjectUploadClick(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("*/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("*/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Object");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, SELECT_OBJECT);
    }

    public void onTextureUploadClick(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, SELECT_TEXTURE);
    }

    public void OnUploadClick(View view) {

        EditText editText = (EditText) findViewById(R.id.nameText);

        switch (augmentationType) {
            case "image":
                new ARRealityImageUploader(markerFilePath, imageFilePath,
                        currentLocation, editText.getText().toString()).execute();
                break;

            case "model":
                new ARRealityObjectUploader(markerFilePath, objectFilePath,
                        textureFilePath, currentLocation, editText.getText().toString()).execute();
                break;

            default:
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //mLocationRequest.setSmallestDisplacement(0.1F);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        } else {
            // Show rationale and request permission.
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    public class MainPagerAdapter extends PagerAdapter
    {
        // This holds all the currently displayable views, in order from left to right.
        private ArrayList<View> views = new ArrayList<View>();

        //-----------------------------------------------------------------------------
        // Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
        // page should be displayed, from left-to-right.  If the page no longer exists,
        // return POSITION_NONE.
        @Override
        public int getItemPosition (Object object)
        {
            int index = views.indexOf (object);
            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }

        //-----------------------------------------------------------------------------
        // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
        // to add the page to the container, which is normally the ViewPager itself.  Since
        // all our pages are persistent, we simply retrieve it from our "views" ArrayList.
        @Override
        public Object instantiateItem (ViewGroup container, int position)
        {
            View v = views.get (position);
            container.addView (v);
            return v;
        }

        //-----------------------------------------------------------------------------
        // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
        // is our job to remove the page from the container, which is normally the
        // ViewPager itself.  Since all our pages are persistent, we do nothing to the
        // contents of our "views" ArrayList.
        @Override
        public void destroyItem (ViewGroup container, int position, Object object)
        {
            container.removeView (views.get (position));
        }

        //-----------------------------------------------------------------------------
        // Used by ViewPager; can be used by app as well.
        // Returns the total number of pages that the ViewPage can display.  This must
        // never be 0.
        @Override
        public int getCount ()
        {
            return views.size();
        }

        //-----------------------------------------------------------------------------
        // Used by ViewPager.
        @Override
        public boolean isViewFromObject (View view, Object object)
        {
            return view == object;
        }

        //-----------------------------------------------------------------------------
        // Add "view" to right end of "views".
        // Returns the position of the new view.
        // The app should call this to add pages; not used by ViewPager.
        public int addView (View v)
        {
            return addView (v, views.size());
        }

        //-----------------------------------------------------------------------------
        // Add "view" at "position" to "views".
        // Returns position of new view.
        // The app should call this to add pages; not used by ViewPager.
        public int addView (View v, int position)
        {
            views.add (position, v);
            notifyDataSetChanged();
            return position;
        }

        //-----------------------------------------------------------------------------
        // Removes "view" from "views".
        // Retuns position of removed view.
        // The app should call this to remove pages; not used by ViewPager.
        public int removeView (ViewPager pager, View v)
        {
            return removeView (pager, views.indexOf (v));
        }

        //-----------------------------------------------------------------------------
        // Removes the "view" at "position" from "views".
        // Retuns position of removed view.
        // The app should call this to remove pages; not used by ViewPager.
        public int removeView (ViewPager pager, int position)
        {
            // ViewPager doesn't have a delete method; the closest is to set the adapter
            // again.  When doing so, it deletes all its views.  Then we can delete the view
            // from from the adapter and finally set the adapter to the pager again.  Note
            // that we set the adapter to null before removing the view from "views" - that's
            // because while ViewPager deletes all its views, it will call destroyItem which
            // will in turn cause a null pointer ref.
            pager.setAdapter (null);
            views.remove (position);
            pager.setAdapter (this);
            notifyDataSetChanged();

            return position;
        }

        //-----------------------------------------------------------------------------
        // Returns the "view" at "position".
        // The app should call this to retrieve a view; not used by ViewPager.
        public View getView (int position)
        {
            return views.get (position);
        }

        // Other relevant methods:

        // finishUpdate - called by the ViewPager - we don't care about what pages the
        // pager is displaying so we don't use this method.
    }

}
