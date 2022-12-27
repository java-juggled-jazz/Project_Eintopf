package com.diphenylamyn.eintopf;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WidgetConfig extends AppCompatActivity {
    public int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    String langId = null;
    final String LOG_TAG = "ConfigLog";
    public final static String WIDGET_LANG = "widgetLang";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Config started");

        SharedPreferences prefs = getSharedPreferences(Widget.WIDGET_ID, Context.MODE_MULTI_PROCESS);
        widgetId = prefs.getInt(Widget.WIDGET_ID, 0);

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        setResult(RESULT_CANCELED, resultValue);
        setContentView(R.layout.widget_config);
        RadioGroup radGrp = findViewById(R.id.configRg);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.configTextviewEn:
                        langId = "en";
                        break;
                    case R.id.configTextviewDe:
                        langId = "de";
                        break;
                    default: break;
                }
            }
        });
    }

    public void applyConfig(View v) throws NumberFormatException {
        SharedPreferences prefs = getSharedPreferences(WIDGET_LANG + widgetId, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(WIDGET_LANG + widgetId, langId);
        editor.apply();
        Log.d(LOG_TAG, "Finished" + widgetId + " " + langId);
        Intent serviceIntent = new Intent(this, WidgetService.class);
        this.startService(serviceIntent);
        this.stopService(serviceIntent);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}