package com.SXG.sam.choosu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.SXG.sam.choosu.SavedDatabase.YelpSavedContract;
import com.SXG.sam.choosu.GoogleService.SignIn;
import com.SXG.sam.choosu.Model.YelpModel;
import com.SXG.sam.choosu.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import static com.SXG.sam.choosu.GoogleService.SignIn.personName;

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
            personName = getString(R.string.ChoosU);
        if(personName !=null){
            getSupportActionBar().setTitle(personName);}

        mAdView = findViewById(R.id.past_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });


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
                break;

            case R.id.choos_u:
                finish();
                Intent intent = new Intent(context, NewActivity.class);
                startActivity(intent);
                Toast.makeText(context, R.string.New_Choices, Toast.LENGTH_SHORT).show();
                break;

            case R.id.choosen_u:
                return true;

            case R.id.profile:
                finish();
                Intent intent1 = new Intent(context, SignIn.class);
                startActivity(intent1);
                Toast.makeText(context,
                        R.string.Sign_in_hint,
                        Toast.LENGTH_SHORT).show();
                break;


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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        restaurantList.setLayoutManager(mLayoutManager);
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