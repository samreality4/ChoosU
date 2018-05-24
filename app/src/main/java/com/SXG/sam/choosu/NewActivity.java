package com.SXG.sam.choosu;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SXG.sam.choosu.Model.MetaData;
import com.SXG.sam.choosu.SavedDatabase.YelpSavedContract;
import com.SXG.sam.choosu.GoogleService.SignIn;
import com.SXG.sam.choosu.Model.YelpModel;
import com.SXG.sam.choosu.Database.YelpContract;
import com.SXG.sam.choosu.R;
import com.SXG.sam.choosu.Widget.NewAppWidgetProvider;
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
        YelpCursorAdapter.yelpClickListener,SensorEventListener {

    private static final int LIST_ID = 100;
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
    String SAVED_LAYOUT_MANAGER;
    Parcelable listState;
    Button button;
    private SensorManager sensorManager;
    private long lastUpdate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //moveTaskToBack(true);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

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

        button = findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYelp(v);
            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cursorList.isEmpty()){
                Collections.shuffle(cursorList);
                restaurantList.setAdapter(yelpCursorAdapter);

                countDownTimer.start();
                //for every click or every call clickCount will increase by 1
                clickCount++;
                if (clickCount < 2) {
                    Toast.makeText(NewActivity.this,
                            R.string.Smash_Toast, Toast.LENGTH_SHORT).show();
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
                            R.string.happy_toast, Toast.LENGTH_SHORT).show();

                    ContentValues values = new ContentValues();

                    values.put(YelpSavedContract.YelpEntry.KEY_NAME, randomName);
                    values.put(YelpSavedContract.YelpEntry.KEY_URL, randomUrl);
                    values.put(YelpSavedContract.YelpEntry.KEY_IMAGE_URL, randomImageUrl);
                    getContentResolver().insert(YelpSavedContract.YelpEntry.CONTENT_URI, values);
                    }

                }
            }

        });


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (getString(R.string.text_plain).equals(type)) {
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

    public void openYelp(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://m.yelp.com"));
        context.startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccerlerometer(event);
        }

    }

    private void getAccerlerometer(SensorEvent event){
        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if(accelationSquareRoot >=2){
            if(actualTime - lastUpdate < 200){
                return;

            }

            lastUpdate = actualTime;
            if(!cursorList.isEmpty()){
                Collections.shuffle(cursorList);
                restaurantList.setAdapter(yelpCursorAdapter);
                countDownTimer.start();
                //for every click or every call clickCount will increase by 1
                clickCount++;
                if (clickCount < 5) {
                    Toast.makeText(NewActivity.this,
                            R.string.shakeitmore, Toast.LENGTH_SHORT).show();
                } else if (clickCount == 10) {
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
                            R.string.happy_toast, Toast.LENGTH_SHORT).show();

                    ContentValues values2 = new ContentValues();

                    values2.put(YelpSavedContract.YelpEntry.KEY_NAME, randomName);
                    values2.put(YelpSavedContract.YelpEntry.KEY_URL, randomUrl);
                    values2.put(YelpSavedContract.YelpEntry.KEY_IMAGE_URL, randomImageUrl);
                    getContentResolver().insert(YelpSavedContract.YelpEntry.CONTENT_URI, values2);
                }

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

            button.setVisibility(View.GONE);

            ContentValues values = new ContentValues();

            values.put(YelpContract.YelpEntry.KEY_NAME, name);
            values.put(YelpContract.YelpEntry.KEY_URL, url);
            if (imageurl != null) {
                values.put(YelpContract.YelpEntry.KEY_IMAGE_URL, imageurl);
            }

            getContentResolver().insert(YelpContract.YelpEntry.CONTENT_URI, values);
            Log.i(getString(R.string.tag_values), String.valueOf(values));
            NewAppWidgetProvider.toBroadCast(context);



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
                NewAppWidgetProvider.toBroadCast(context);
                button.setVisibility(View.VISIBLE);
                break;


            case R.id.choos_u:
                return true;

            case R.id.choosen_u:
                finish();
                Intent intent = new Intent(context, PastActivity.class);
                startActivity(intent);
                Toast.makeText(context, R.string.Past_Choosen, Toast.LENGTH_SHORT).show();
                break;
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




        //getSupportLoaderManager().restartLoader(0, null, this);

        /*if(listState !=null){
        restaurantList.getLayoutManager().onRestoreInstanceState(listState);}*/


    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //getSupportLoaderManager().restartLoader(LIST_ID, null, this);

    }

   /* void stopLoader(int id){
        getSupportLoaderManager().destroyLoader(id);
    }*/

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

    /*protected void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        listState = restaurantList.getLayoutManager().onSaveInstanceState();
        state.putParcelable(SAVED_LAYOUT_MANAGER, listState);
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle state){
        super.onRestoreInstanceState(state);
        listState=state.getParcelable(SAVED_LAYOUT_MANAGER);
    }*/

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("prefsfilename", MODE_PRIVATE);
        SharedPreferences.Editor presEditor = sharedPreferences.edit();
        presEditor.putInt(getString(R.string.visibility), button.getVisibility());
        presEditor.apply();
        sensorManager.unregisterListener(this);

    }

    @Override
    protected void onResume(){
     super.onResume();
     SharedPreferences sharedPreferences = getSharedPreferences("prefsfilename", MODE_PRIVATE);
        if(sharedPreferences != null){
            int visibility = sharedPreferences.getInt(getString(R.string.visibility), 0);
            button.setVisibility(visibility);}
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
    }

}
