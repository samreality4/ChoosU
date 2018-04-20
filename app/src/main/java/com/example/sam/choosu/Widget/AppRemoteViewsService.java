package com.example.sam.choosu.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.sam.choosu.Model.YelpModel;
import com.example.sam.choosu.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AppRemoteViewsService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppRemoteViewsFactory(this.getApplicationContext());
    }

    class AppRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private static final String PREF_NAME = "prefname";
        private static final String PREF_KEY = "prefkey";

        private Context context;
        private List<YelpModel> yelpModelList;

        public AppRemoteViewsFactory(Context context) {
            this.context = context;

        }
        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String json = preferences.getString(PREF_KEY, null);
            Type type = new TypeToken<List<YelpModel>>(){}.getType();
            Gson gson = new Gson();

            List<YelpModel> yelpModels = gson.fromJson(json, type);

            {
                yelpModelList = yelpModels;
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (yelpModelList !=null ){
                return yelpModelList.size();
            }else
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.id.widget_item);

            rv.setTextViewText(R.id.item,
                    String.valueOf(yelpModelList.get(position).getName()));
            rv.setTextViewText(R.id.position,
                    String.valueOf(yelpModelList.get(position)) + "1");
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
          return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
