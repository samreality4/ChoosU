package com.SXG.sam.choosu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.SXG.sam.choosu.R;
import com.squareup.picasso.Picasso;


public class FirstStartActivity extends AppCompatActivity {
    ImageView imageView;
    Context context;
    TextView textView;
    private int clickCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean alreadyRan = sharedPreferences.getBoolean(getString(R.string.already_ran), false);
        if (!alreadyRan) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.already_ran), Boolean.TRUE);
            editor.apply();

        } else {
            Intent intent = new Intent(this, NewActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.first_time_layout);
        imageView = findViewById(R.id.first_image);
        textView = findViewById(R.id.first_text);

        FloatingActionButton firstFab = findViewById(R.id.first_fab);
        firstFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            clickCount++;
            if(clickCount == 1){
                Picasso.get()
                        .load(R.drawable.newpageone)
                        .fit()
                        .into(imageView);
                textView.setText("You can go directly the Yelp add by clicking the add button.");
                    } else if (clickCount == 2) {
                Picasso.get()
                        .load(R.drawable.page_one)
                        .fit()
                        .into(imageView);
                textView.setText(R.string.welcome_page_shareicon);
                        } else if (clickCount == 3) {
                Picasso.get()
                        .load(R.drawable.page_two)
                        .fit()
                        .into(imageView);
                textView.setText(R.string.welcome_page_sharemenu);
                            } else if (clickCount == 4) {
                Picasso.get()
                        .load(R.drawable.newpagethree)
                        .fit()
                        .into(imageView);
                textView.setText("Shake your phone to let this app choose for you!");
                                } else if (clickCount == 5) {
                                    Intent intent = new Intent(context, NewActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }

        });

    }
}