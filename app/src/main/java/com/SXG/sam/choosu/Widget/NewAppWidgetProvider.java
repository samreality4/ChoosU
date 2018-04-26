package com.SXG.sam.choosu.Widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.SXG.sam.choosu.R;

public class NewAppWidgetProvider extends android.appwidget.AppWidgetProvider{
    private static final String PREF_NAME = "prefname";
    private static final String RESTAURANT_NAME = "restaurantname";

    private String restaurantName;


    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.
                getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(),
                NewAppWidgetProvider.class);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
}

public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);

    Intent intent = new Intent(context, NewAppRemoteViewsService.class);
    //SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    views.setRemoteAdapter(R.id.list_view, intent);

    views.setEmptyView(R.id.list_view, R.id.empty_view);


    appWidgetManager.updateAppWidget(appWidgetId, views);
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
}


}
