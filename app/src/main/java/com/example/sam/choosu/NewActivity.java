package com.example.sam.choosu;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sam.choosu.Model.YelpModel;
import com.example.sam.choosu.database.YelpContract;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class NewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String [] array;
    String name;
    String url;
    private RecyclerView restaurantList;
    private YelpCursorAdapter yelpCursorAdapter;
    List<YelpModel> list = new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }

        ContentValues values = new ContentValues();
        values.put(YelpContract.YelpEntry.KEY_NAME, name);
        values.put(YelpContract.YelpEntry.KEY_URL, url);


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
        list.clear();
        restaurantFromCursor(data);
        yelpCursorAdapter = new YelpCursorAdapter(NewActivity.this, list);
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

                    list.add(yelpModel);
                } while (cursor.moveToNext());
            }
        }
        return list;


    }
}
