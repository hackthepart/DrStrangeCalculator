package com.example.android.calculator;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Lenovo on 12-Jan-18.
 */

public class NumberSystemWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        int n=appWidgetIds.length;

        for(int i=0;i<n;++i){
            Intent updateIntent=new Intent(context,NumberSystemWidgetConfigure.class);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
            PendingIntent updatePendingIntent=PendingIntent.getActivity(context,0,updateIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.layout_widget);
            remoteViews.setOnClickPendingIntent(R.id.btOpenConvert,updatePendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i],remoteViews);

        }

    }
}