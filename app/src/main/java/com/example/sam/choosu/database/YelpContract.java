package com.example.sam.choosu.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sam on 3/24/18.
 */

public class YelpContract {
    public static final String AUTHORITY ="com.example.sam.choosu";
    public static final String PATH_TASKS="YelpDB";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/");

    public static final class YelpEntry implements BaseColumns {

        public static final String TABLE_YELP = "Data_YELP";
        public static final String KEY_NAME = "name";
        public static final String KEY_URL = "URL";


        public static String CREATE_YELP_TABLE = "CREATE TABLE" + TABLE_YELP + " (" + _ID
                + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_URL + " TEXT)";

        public static String[] Columns = new String[]{_ID, KEY_NAME, KEY_URL};

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_YELP).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_YELP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_YELP;

        public static Uri buildYelpUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
