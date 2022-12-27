package com.diphenylamyn.eintopf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ListOfWords extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_words);
        TextView listTitle = findViewById(R.id.list_title);
        TextView wordsList = findViewById(R.id.word_list);
        Button reset = findViewById(R.id.reset);
        String list_dir = null;
        String src_dir = null;
        String lang_code = getIntent().getStringExtra("lang_code");
        switch (lang_code) {
            case "en":
                listTitle.setText(getString(R.string.title_text_en));
                src_dir = "en_words_src";
                list_dir = "en_words";
                break;
            case "de":
                listTitle.setText(getString(R.string.title_text_de));
                src_dir = "de_words_src";
                list_dir = "de_words";
            default:
                break;
        }
        String dict = null;
        try {
            InputStream fis = openFileInput(list_dir);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            dict = new String (bytes);
            wordsList.setText(dict);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String finalList_dir = list_dir;
        String finalSrc_dir = src_dir;
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile(finalList_dir);
                String dict = "";
                try {
                    InputStream is = getAssets().open(finalSrc_dir);
                    int size = is.available();
                    byte[] buf = new byte[size];
                    is.read(buf);
                    dict = new String(buf);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(finalList_dir, MODE_APPEND);
                    fos.write(dict.getBytes());
                    wordsList.setText(dict);
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    public void goBack(View view) {
        finish();
    }
}
