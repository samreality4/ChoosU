package com.SXG.sam.choosu;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
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
import android.webkit.URLUtil;
import android.widget.Toast;

import com.SXG.sam.choosu.Model.MetaData;
import com.SXG.sam.choosu.SavedDatabase.YelpSavedContract;
import com.SXG.sam.choosu.GoogleService.SignIn;
import com.SXG.sam.choosu.Model.YelpModel;
import com.SXG.sam.choosu.Database.YelpContract;
import com.SXG.sam.choosu.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.SXG.sam.choosu.GoogleService.SignIn.personName;


public class NewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        YelpCursorAdapter.yelpClickListener {

    String[] array;
    String name;
    String url;
    String imageurl;
    private RecyclerView restaurantList;
    private YelpCursorAdapter yelpCursorAdapter;
    ArrayList<YelpModel> cursorList = new ArrayList<>();
    Context context;
    MetaData metaData;
    private static final String PREF_NAME = "prefname";
    private static final String PREF_KEY = "prefkey";
    private AdView mAdView;
    String randomUrl;
    String randomName;
    String randomImageUrl;
    private int clickCount;
    SignIn signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //moveTaskToBack(true);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //check to see if account is already there
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            personName = account.getGivenName();
        } else
            personName = "ChoosU";
        if (personName != null) {
            getSupportActionBar().setTitle(personName);
        }


        context = getApplicationContext();
        metaData = new MetaData();

        //implementing the admob banner block
        mAdView = findViewById(R.id.adView);
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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Collections.shuffle(cursorList);
                restaurantList.setAdapter(yelpCursorAdapter);

                countDownTimer.start();
                //for every click or every call clickCount will increase by 1
                clickCount++;
                if (clickCount < 2) {
                    Toast.makeText(NewActivity.this,
                            "Smash Faster to Proceed!", Toast.LENGTH_SHORT).show();
                } else if (clickCount == 5) {
                    Random random = new Random();
                    int index = random.nextInt(cursorList.size());
                    randomUrl = cursorList.get(index).getUrl();
                    randomName = cursorList.get(index).getName();
                    randomImageUrl = cursorList.get(index).getYelpImageurl();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(randomUrl));
                    context.startActivity(intent);

                    Toast.makeText(NewActivity.this,
                            "Happy Eating", Toast.LENGTH_SHORT).show();

                    ContentValues values = new ContentValues();
                    values.put(YelpSavedContract.YelpEntry.KEY_NAME, randomName);
                    values.put(YelpSavedContract.YelpEntry.KEY_URL, randomUrl);
                    values.put(YelpSavedContract.YelpEntry.KEY_IMAGE_URL, randomImageUrl);
                    getContentResolver().insert(YelpSavedContract.YelpEntry.CONTENT_URI, values);

                }
            }

        });


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);

                //already getting metadata without waiting for it to finish

            }
            getPreview(url);


        }

        getSupportLoaderManager().initLoader(0, null, this);

    }

    public void getPreview(String url) {
        this.url = url;
        new getData().execute();

    }

    @Override
    public void onYelpClickListener(View v, int position) {

    }


    public class getData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String result = null;
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();


                //getImages
                Elements imageElements = doc.select("meta[property=og:image]");
                if (imageElements.size() > 0) {
                    String image = imageElements.attr("content");
                    if (!image.isEmpty()) {
                        result = resolveURL(url, image);

                    }
                }
                if (result.isEmpty()) {
                    String src = doc.select("link[rel=image_src]").attr("href");
                    if (!src.isEmpty()) {
                        result = (resolveURL(url, src));
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();


            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            metaData.setImageUrl(result);
            imageurl = metaData.getImageUrl();

            ContentValues values = new ContentValues();

            values.put(YelpContract.YelpEntry.KEY_NAME, name);
            values.put(YelpContract.YelpEntry.KEY_URL, url);
            if (imageurl != null) {
                values.put(YelpContract.YelpEntry.KEY_IMAGE_URL, imageurl);
            }

            getContentResolver().insert(YelpContract.YelpEntry.CONTENT_URI, values);
            Log.i("values", String.valueOf(values));


        }
    }

    private String resolveURL(String url, String part) {
        if (URLUtil.isValidUrl(part)) {
            return part;
        } else {
            URI base_uri = null;
            try {
                base_uri = new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            base_uri = base_uri.resolve(part);
            return base_uri.toString();
        }
    }


    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            array = sharedText.split("\n\n");
            name = array[0];
            url = array[1];
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
            case R.id.clear_choices:

                getContentResolver().delete(YelpContract.YelpEntry.CONTENT_URI, null, null);
                break;


            case R.id.choos_u:
                return true;

            case R.id.choosen_u:
                Intent intent = new Intent(context, PastActivity.class);
                startActivity(intent);
                Toast.makeText(context, "Past Choosen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Intent intent1 = new Intent(context, SignIn.class);
                startActivity(intent1);
                Toast.makeText(context,
                        "Sign in to have your name displayed on the left corner!",
                        Toast.LENGTH_SHORT).show();
                break;

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
        cursorList.clear();
        restaurantFromCursor(data);
        yelpCursorAdapter = new YelpCursorAdapter(NewActivity.this, cursorList);
        restaurantList = findViewById(R.id.choose_recyclerview);
        restaurantList.setLayoutManager(new GridLayoutManager(context, 2));
        restaurantList.setAdapter(yelpCursorAdapter);
        saveToSharedPreference(cursorList);


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
                    yelpModel.imageurl = cursor.getString(cursor.getColumnIndex(YelpContract.YelpEntry.KEY_IMAGE_URL));
                    yelpModel.idNumber = cursor.getString(cursor.getColumnIndex(YelpContract.YelpEntry._ID));
                    cursorList.add(yelpModel);
                } while (cursor.moveToNext());
            }
        }
        return cursorList;


    }

   /* private void fliCard(){

        getSupportFragmentManager()
                .beginTransaction()

                .setCustomAnimations(
                        R.anim.card_flip_right_in,
                        R.anim.card_flip_right_out,
                        R.anim.card_flip_left_in,
                        R.anim.card_flip_left_out)
                .add(R.id.fragment_container_back, new CardBackFragment())

                .addToBackStack(null)

                .commit();


    }*/

    private void saveToSharedPreference(List<YelpModel> yelpModelList) {
        //setting up sharedpreferences with name & mode
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        //setting up the editor to edit sharepreference
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Gson gson = new Gson();
        //converting list to json
        String newJsonData = gson.toJson(yelpModelList);
        editor.putString(PREF_KEY, newJsonData);
        editor.apply();

    }

    CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            clickCount = 0;

        }
    };

}
