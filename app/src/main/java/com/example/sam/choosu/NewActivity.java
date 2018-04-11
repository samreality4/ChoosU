package com.example.sam.choosu;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sam.choosu.Library.MetaData;
import com.example.sam.choosu.Library.ResponseListener;
import com.example.sam.choosu.Library.UrlPreview;
import com.example.sam.choosu.Library.UrlPreviewView;
import com.example.sam.choosu.Model.YelpModel;
import com.example.sam.choosu.database.YelpContract;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    String [] array;
    String name;
    String url;
    String imageurl;
    private RecyclerView restaurantList;
    private YelpCursorAdapter yelpCursorAdapter;
    List<YelpModel> cursorList = new ArrayList<>();
    Context context;
    MetaData metaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = getApplicationContext();




            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fliCard();

                    Collections.shuffle(cursorList);
                    restaurantList.swapAdapter(yelpCursorAdapter, false);
                }
            });

            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();


            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    handleSendText(intent);

                    UrlPreview urlPreview = new UrlPreview(new ResponseListener() {
                        @Override
                        public void onData(MetaData metaData) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }

                    });
                    urlPreview.getPreview(url);
                    //already getting metadata without waiting for it to finish
                    MetaData metaData = new MetaData();
                    imageurl = metaData.getImageUrl();



                    ContentValues values = new ContentValues();

                    values.put(YelpContract.YelpEntry.KEY_NAME, name);
                    values.put(YelpContract.YelpEntry.KEY_URL, url);
                    if(imageurl!=null){
                    values.put(YelpContract.YelpEntry.KEY_IMAGE_URL, imageurl);}

                    getContentResolver().insert(YelpContract.YelpEntry.CONTENT_URI, values);
                    Log.i("values", String.valueOf(values));
                }
            }
            getSupportLoaderManager().initLoader(0, null, this);

              /*CardFrontFragment cardFrontFragment = new CardFrontFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout_container, cardFrontFragment)
                    .commit();*/


    }




    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            array = sharedText.split("\n\n");
            name = array [0];
            url = array [1];
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.choos_u:
                return true;

            case R.id.choosen_u:
                return true;


        }

        return super.onOptionsItemSelected(item);

    }


   @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                YelpContract.YelpEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        restaurantFromCursor(data);
        yelpCursorAdapter = new YelpCursorAdapter(NewActivity.this, cursorList);
        restaurantList = findViewById(R.id.choose_recyclerview);
        restaurantList.setLayoutManager(new GridLayoutManager(context, 2));
        restaurantList.setAdapter(yelpCursorAdapter);


    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private List<YelpModel> restaurantFromCursor(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    YelpModel yelpModel = new YelpModel();
                    yelpModel.name = cursor.getString(cursor.getColumnIndex(YelpContract.YelpEntry.KEY_NAME));
                    yelpModel.url = cursor.getString(cursor.getColumnIndex(YelpContract.YelpEntry.KEY_URL));
                    //yelpModel.imageurl = cursor.getString(cursor.getColumnIndex(YelpContract.YelpEntry.KEY_IMAGE_URL));
                    cursorList.add(yelpModel);
                } while (cursor.moveToNext());
            }
        }
        return cursorList;


    }

    private void fliCard(){

        getSupportFragmentManager()
                .beginTransaction()

                .setCustomAnimations(
                        R.anim.card_flip_right_in,
                        R.anim.card_flip_right_out,
                        R.anim.card_flip_left_in,
                        R.anim.card_flip_left_out)
                .add(R.id.empty_container, new CardBackFragment())

                .addToBackStack(null)

                .commit();


    }


}
