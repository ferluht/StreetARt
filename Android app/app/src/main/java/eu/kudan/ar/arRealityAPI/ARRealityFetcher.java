package eu.kudan.ar.arRealityAPI;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import retrofit2.Call;

/**
 * Created by ferluht on 24.08.2016.
 */
public class ARRealityFetcher extends AsyncTask<Void, Void, Void>
        implements ARRealityDownloadQueueInterface{

    private static final int rad = 500;
    private ARRealityClient arRealityClient;
    private List<ARRealityObject> objects;
    private Call<List<ARRealityObject>> call;
    private ARRealityFetcherInterface arRealityFetcherInterface;
    private Location mCurLoc;
    private Context context;

    public ARRealityFetcher(Context context, ARRealityFetcherInterface inListener, Location location) {
        this.arRealityFetcherInterface = inListener;
        this.mCurLoc = location;
        this.context = context;

        ARRealityDBHelper mDatabaseHelper = new ARRealityDBHelper(context);
        SQLiteDatabase mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mDatabaseHelper.dbFlush(mSqLiteDatabase);

        arRealityClient = ARRealityServiceGenerator.createService(ARRealityClient.class);
        call = arRealityClient.objects(mCurLoc.getLatitude(), mCurLoc.getLongitude(), 500);
    }

    public ARRealityFetcher(Context context, ARRealityFetcherInterface inListener, int id){
        this.arRealityFetcherInterface = inListener;
        this.context = context;

        ARRealityDBHelper mDatabaseHelper = new ARRealityDBHelper(context);
        SQLiteDatabase mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mDatabaseHelper.dbFlush(mSqLiteDatabase);

        arRealityClient = ARRealityServiceGenerator.createService(ARRealityClient.class);
        call = arRealityClient.object(id);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            objects = call.execute().body();
        } catch (IOException e) {
            objects = null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        ARRealityDBHelper mDatabaseHelper = new ARRealityDBHelper(context);

        SQLiteDatabase mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        if (objects != null) {
            arRealityFetcherInterface.setUpMapMarkers(objects);

            ARRealityDownloadQueue arRealityDownloadQueue = new ARRealityDownloadQueue(this);

            for (ARRealityObject tempObj : objects) {
                Cursor cursor = mSqLiteDatabase.rawQuery("SELECT * FROM " +
                                ARRealityDBHelper.LOADED_OBJECTS_TABLE +
                                " WHERE " + ARRealityDBHelper.ID_COLUMN + " = ?",
                        new String[]{Integer.toString(tempObj.getId())});

                tempObj.initAssetsWithId();
                List<ARRealityAsset> assets = tempObj.getAssets();
                if (!(cursor.moveToFirst()) || cursor.getCount() == 0)
                    arRealityDownloadQueue.addAssetsToQueue(assets);
                else tempObj.isDownloaded = true;
            }
            arRealityDownloadQueue.execute();
        }
    }

    @Override
    public void allDownloaded() throws IOException {
        ARRealityDBHelper mDatabaseHelper = new ARRealityDBHelper(context);
        SQLiteDatabase sdb;
        sdb = mDatabaseHelper.getReadableDatabase();
        //mDatabaseHelper.dbFlush(sdb);

        for( ARRealityObject object : objects){
            if(!object.isDownloaded) {
                ContentValues values = new ContentValues();
                // Задайте значения для каждого столбца
                values.put(ARRealityDBHelper.ID_COLUMN, object.getId());
                values.put(ARRealityDBHelper.OBJECT_NAME_COLUMN, object.getName());
                values.put(ARRealityDBHelper.OBJECT_TYPE_COLUMN, object.getAugmentationType());
                values.put(ARRealityDBHelper.TEXTURE_TYPE_COLUMN, object.getTextureType());
                // Вставляем данные в таблицу
                sdb.insert(ARRealityDBHelper.LOADED_OBJECTS_TABLE, null, values);
                object.isDownloaded = true;
            }
        }

        arRealityFetcherInterface.objectsUpdated(objects);
    }

    @Override
    public void failedToDownload(ARRealityAsset arRealityAsset) {

    }

    @Override
    public void onProgressChanged(int progress) {
        arRealityFetcherInterface.onDownloadProgressChanged(progress);
    }
}
