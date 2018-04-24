package com.example.sam.choosu.SavedDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sam.choosu.Database.YelpContract;

/**
 * Created by sam on 3/26/18.
 */

public class YelpSavedHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "YELP_CHOOSEN_DB";

    public YelpSavedHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(YelpSavedContract.YelpEntry.CREATE_YELP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + YelpSavedContract.YelpEntry.TABLE_YELP);
            onCreate(db);
        }

    }
}
