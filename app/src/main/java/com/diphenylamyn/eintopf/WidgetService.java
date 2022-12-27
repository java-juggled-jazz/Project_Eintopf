package com.diphenylamyn.eintopf;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class WidgetService extends Service {
    int widgetId;
    String langId;
    SharedPreferences wordToWidget;
    String sharedWord;
    public static final String APP_PREFS_WORD = "wordToWidget";

    public WidgetService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("MY_SERVICE", "Сервис запущен.");

        SharedPreferences prefsId = getSharedPreferences("widgetId", Context.MODE_MULTI_PROCESS);
        widgetId = prefsId.getInt(Widget.WIDGET_ID, 0);

        SharedPreferences prefs = getSharedPreferences(WidgetConfig.WIDGET_LANG + widgetId, Context.MODE_PRIVATE);
        langId = prefs.getString(WidgetConfig.WIDGET_LANG + widgetId, "en");

        try {
            sharedWord = getWord(langId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordToWidget = getSharedPreferences(APP_PREFS_WORD + widgetId, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor spEditor = wordToWidget.edit();
        spEditor.putString(APP_PREFS_WORD + widgetId, sharedWord);
        spEditor.apply();
        if (sharedWord != null) {
            Log.d("MY_SERVICE", sharedWord + " " + langId + " " + widgetId);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("MY_SERVICE", "Сервис остановлен.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String getWord(String langId) throws IOException, NullPointerException {
        String list_dir = null;
        switch (langId) {
            case "en": list_dir = "en_words";
                 break;
            case "de": list_dir = "de_words";
                 break;
        }
        Random rnd = new Random();
        FileInputStream fin = openFileInput(list_dir);
        byte[] bytes = new byte[fin.available()];
        fin.read(bytes);
        String list = new String (bytes);
        String[] words = list.split("\\n");
        int cnt = rnd.nextInt(words.length);
        Log.d("MY_SERVICE", "Слово выбрано");
        return words[cnt];
    }
}