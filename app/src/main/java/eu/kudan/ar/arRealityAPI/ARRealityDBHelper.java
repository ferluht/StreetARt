package eu.kudan.ar.arRealityAPI;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ferluht on 28/08/2016.
 */
public class ARRealityDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "localreality.db";
    private static final int DATABASE_VERSION = 1;

    public static final String LOADED_OBJECTS_TABLE = "loaded_objects";
    public static final String MY_OBJECTS_TABLE = "my_objects";

    public static final String OBJECT_NAME_COLUMN = "name";
    public static final String OBJECT_TYPE_COLUMN = "type";
    public static final String ID_COLUMN = "id";
    public static final String TEXTURE_TYPE_COLUMN = "ttype";

    private static final String LOADED_OBJECTS_CREATE_SCRIPT = "create table "
            + LOADED_OBJECTS_TABLE + " ("
            + ID_COLUMN + " integer primary key, "
            + OBJECT_NAME_COLUMN + " text not null, "
            + OBJECT_TYPE_COLUMN + " text not null, "
            + TEXTURE_TYPE_COLUMN + " text not null);";

    private static final String MY_OBJECTS_CREATE_SCRIPT = "create table "
            + MY_OBJECTS_TABLE + " ("
            + ID_COLUMN + " integer primary key, "
            + OBJECT_NAME_COLUMN + " text not null, "
            + OBJECT_TYPE_COLUMN + " text not null, "
            + TEXTURE_TYPE_COLUMN + " text not null);";

    private static final String SQL_DELETE_LOADED =
            "DROP TABLE IF EXISTS " + LOADED_OBJECTS_TABLE;

    private static final String SQL_DELETE_MY =
            "DROP TABLE IF EXISTS " + MY_OBJECTS_TABLE;

    public ARRealityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ARRealityDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(LOADED_OBJECTS_CREATE_SCRIPT);
        sqLiteDatabase.execSQL(MY_OBJECTS_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void dbFlush(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(SQL_DELETE_LOADED);
        sqLiteDatabase.execSQL(SQL_DELETE_MY);
        sqLiteDatabase.execSQL(LOADED_OBJECTS_CREATE_SCRIPT);
        sqLiteDatabase.execSQL(MY_OBJECTS_CREATE_SCRIPT);
    }
}
