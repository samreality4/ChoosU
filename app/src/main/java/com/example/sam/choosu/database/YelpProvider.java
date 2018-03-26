package com.example.sam.choosu.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by sam on 3/26/18.
 */

public class YelpProvider extends ContentProvider{
    private SQLiteOpenHelper mSQLiteOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int RESTAURANTS = 0;
    private static final int RESTAURANTS_WITH_ID = 200;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = YelpContract.AUTHORITY;

        matcher.addURI(authority, YelpContract.YelpEntry.TABLE_YELP, RESTAURANTS);
        matcher.addURI(authority, YelpContract.YelpEntry.TABLE_YELP, RESTAURANTS_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        mSQLiteOpenHelper = new YelpHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mSQLiteOpenHelper.getReadableDatabase();
        Cursor mCursor;

        switch (sUriMatcher. match(uri)) {
            case RESTAURANTS:{
                mCursor = db.query(YelpContract.YelpEntry.TABLE_YELP,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new IllegalArgumentException("uri not recoginized!");
        }

        mCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return mCursor

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case RESTAURANTS: {
                return YelpContract.YelpEntry.CONTENT_DIR_TYPE;
            }
            case RESTAURANTS_WITH_ID: {
                case YelpContract.YelpEntry.CONTENT_ITEM_TYPE ;

            }
            default: {
                throw new IllegalArgumentException("unknow URI man");
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case RESTAURANTS:
                _id = db.insert((YelpContract.YelpEntry.TABLE_YELP), null, values);
                if (_id > 0) {
                    returnUri = YelpContract.YelpEntry.buildYelpUri(_id);
                    db.close();
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into" + uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException("unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mSQLiteOpenHelper.getWritableDatabase();
        int rows = 0;
        switch (sUriMatcher.match(uri)) {
            case RESTAURANTS:
                db.delete(YelpContract.YelpEntry.TABLE_YELP, selection, selectionArgs);
                break;

                default:
                    throw new UnsupportedOperationException("unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return -1;
    }
}
