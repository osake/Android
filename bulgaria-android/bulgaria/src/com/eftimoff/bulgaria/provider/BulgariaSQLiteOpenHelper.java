package com.eftimoff.bulgaria.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.eftimoff.bulgaria.BuildConfig;
import com.eftimoff.bulgaria.provider.sight.SightColumns;

public class BulgariaSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = BulgariaSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "bulgaria.db";
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;
    private final BulgariaSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    private static final String SQL_CREATE_TABLE_SIGHT = "CREATE TABLE IF NOT EXISTS "
            + SightColumns.TABLE_NAME + " ( "
            + SightColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SightColumns.LABEL + " TEXT, "
            + SightColumns.DESCRIPTION + " TEXT, "
            + SightColumns.CATEGORY + " TEXT, "
            + SightColumns.REGION + " TEXT, "
            + SightColumns.SHOWNOTIFICATIONS + " INTEGER DEFAULT '1', "
            + SightColumns.LIKE + " INTEGER DEFAULT '1', "
            + SightColumns.THUMBNAIL + " TEXT, "
            + SightColumns.IMG1 + " TEXT, "
            + SightColumns.IMG2 + " TEXT, "
            + SightColumns.IMG3 + " TEXT, "
            + SightColumns.IMG4 + " TEXT, "
            + SightColumns.IMG5 + " TEXT, "
            + SightColumns.COORDINATEX + " REAL, "
            + SightColumns.COORDINATEY + " REAL "
            + " );";

    // @formatter:on

    public static BulgariaSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */

    private static BulgariaSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new BulgariaSQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    private BulgariaSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        mOpenHelperCallbacks = new BulgariaSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static BulgariaSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new BulgariaSQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private BulgariaSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new BulgariaSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_SIGHT);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
