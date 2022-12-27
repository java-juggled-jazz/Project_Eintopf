package com.diphenylamyn.eintopf;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import java.util.Arrays;

public class Widget extends AppWidgetProvider {
    final static String WIDGET_ID = "widgetId";
    final static String LOG_TAG = "WidgetLogs";

    @Override
    public void onEnabled(Context context) { super.onEnabled(context); }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int widgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId);
        }
        Log.d(LOG_TAG, "onUpdate" + Arrays.toString(appWidgetIds));
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        Intent serviceIntent = new Intent(context, WidgetService.class);
        SharedPreferences prefsId = context.getSharedPreferences(WIDGET_ID, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefsId.edit();
        editor.putInt(WIDGET_ID, widgetId);
        editor.apply();

        SharedPreferences prefs = context.getSharedPreferences(WidgetService.APP_PREFS_WORD + widgetId, Context.MODE_MULTI_PROCESS);
        String word = prefs.getString(WidgetService.APP_PREFS_WORD + widgetId, "");
        word = word.replace(" | ", "\n");
        context.startService(serviceIntent);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Log.d(LOG_TAG, "Нажато");
        remoteViews.setTextViewText(R.id.appwidget_text, word);
        Intent updateIntent = new Intent(context, Widget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {widgetId});
        serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, widgetId, updateIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_updBtn, pIntent);
        context.stopService(serviceIntent);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
        Log.d(LOG_TAG, "Updated " + word + " " + widgetId);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences.Editor editor = context.getSharedPreferences(WidgetService.APP_PREFS_WORD, Context.MODE_MULTI_PROCESS).edit();
        for (int widgetId : appWidgetIds) {
            editor.remove(WidgetService.APP_PREFS_WORD + widgetId);
        }
        editor.apply();
        Log.d(LOG_TAG, "onDeleted" + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled;");
    }
}