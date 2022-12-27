package com.diphenylamyn.eintopf;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_TEXT = "widget_text";
    public final static String WIDGET_COLOR = "widget_color_";
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent ResulValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] files_cat = fileList();
        List<String> cat = Arrays.asList(files_cat);
        if (!cat.contains("en_words")) {
            dictMaker("en_words_src", "en_words");
        }
        if (!cat.contains("de_words")) {
            dictMaker("de_words_src", "de_words");
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        ResulValue = new Intent();
        ResulValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_CANCELED, ResulValue);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(WIDGET_TEXT + widgetID, "text");
        editor.putInt(WIDGET_COLOR + widgetID, 1);
        editor.apply();
        setResult(RESULT_OK, ResulValue);
    }
    public void addNewWord(View view) {
        Intent addNewWordAct = new Intent(MainActivity.this, AddNewWord.class);
        startActivity(addNewWordAct);
    }

    public void showListEn(View view) {
        Intent showListAct = new Intent(this, ListOfWords.class);
        showListAct.putExtra("lang_code", "en");
        startActivity(showListAct);
    }

    public void showListDe(View view) {
        Intent showListAct = new Intent(MainActivity.this, ListOfWords.class);
        showListAct.putExtra("lang_code", "de");
        startActivity(showListAct);
    }

    public void creditsClick(View view) {
        Snackbar.make(view, "By Kamil \"Diphenylamyn\"", Snackbar.LENGTH_LONG).show();
    }

    public void dictMaker(String src_dir, String dst_dir) {
        String dict = "";
        try {
            InputStream is = getAssets().open(src_dir);
            int size = is.available();
            byte[] buf = new byte[size];
            is.read(buf);
            dict = new String(buf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(dst_dir, MODE_APPEND);
            fos.write(dict.getBytes());
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}