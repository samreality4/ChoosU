package com.example.sam.choosu;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sam.choosu.Model.YelpModel;
import com.example.sam.choosu.database.YelpContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 4/7/18.
 */

public class CardFrontFragment extends Fragment{

    private RecyclerView restaurantList;
    private YelpCursorAdapter yelpCursorAdapter;
    List<YelpModel> cursorList = new ArrayList<>();
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_cardview, container, false);
        //getLoaderManager().initLoader(0, null, this);


        /*yelpCursorAdapter = new YelpCursorAdapter(context, cursorList);
        restaurantList = rootView.findViewById(R.id.choose_recyclerview);
        restaurantList.setLayoutManager(new GridLayoutManager(context, 2));
        restaurantList.setAdapter(yelpCursorAdapter);*/

        return rootView;
    }

    public CardFrontFragment(){}



    }

