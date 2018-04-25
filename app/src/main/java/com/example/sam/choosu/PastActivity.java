package com.example.sam.choosu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sam.choosu.Database.YelpContract;
import com.example.sam.choosu.GoogleService.SignIn;
import com.example.sam.choosu.Model.YelpModel;
import com.example.sam.choosu.SavedDatabase.YelpSavedContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import static com.example.sam.choosu.GoogleService.SignIn.personName;

public class PastActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
YelpCursorAdapter.yelpClickListener{
    Context context;
    private RecyclerView restaurantList;
    private YelpCursorAdapter yelpCursorAdapter;
    ArrayList<YelpModel> cursorList = new ArrayList<>();
    private AdView mAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        context = getApplicationContext();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account !=null){
            personName = account.getGivenName();}else
            personName = "ChoosU";
        if(personName !=null){
            getSupportActionBar().setTitle(personName);}

        mAdView = findViewById(R.id.past_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        getSupportLoaderManager().initLoader(0, null, this);
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
            case R.id.clear_choices:

                getContentResolver().delete(YelpSavedContract.YelpEntry.CONTENT_URI, null, null);


            case R.id.choos_u:
                Intent intent = new Intent(context, NewActivity.class);
                startActivity(intent);
                Toast.makeText(context, "New Choices", Toast.LENGTH_SHORT).show();

            case R.id.choosen_u:
                return true;

            case R.id.profile:
                Intent intent1 = new Intent(context, SignIn.class);
                startActivity(intent1);


        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                YelpSavedContract.YelpEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorList.clear();
        restaurantFromCursor(data);
        yelpCursorAdapter = new YelpCursorAdapter(PastActivity.this, cursorList);
        restaurantList = findViewById(R.id.choosen_recyclerview);
        restaurantList.setLayoutManager(new LinearLayoutManager(context));
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
                    yelpModel.name = cursor.getString(cursor.getColumnIndex(YelpSavedContract.YelpEntry.KEY_NAME));
                    yelpModel.url = cursor.getString(cursor.getColumnIndex(YelpSavedContract.YelpEntry.KEY_URL));
                    yelpModel.imageurl = cursor.getString(cursor.getColumnIndex(YelpSavedContract.YelpEntry.KEY_IMAGE_URL));
                    yelpModel.idNumber = cursor.getString(cursor.getColumnIndex(YelpSavedContract.YelpEntry._ID));
                    cursorList.add(yelpModel);
                } while (cursor.moveToNext());
            }
        }
        return cursorList;


    }

    @Override
    public void onYelpClickListener(View v, int position) {

    }


}