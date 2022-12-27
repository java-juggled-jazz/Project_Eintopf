package com.diphenylamyn.eintopf;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddNewWord extends AppCompatActivity {

    String lang = null;
    String de_words_file = "de_words";
    String en_words_file = "en_words";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        RadioGroup radGrp = findViewById(R.id.lang_selector);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                TextView choose = findViewById(R.id.choose);
                switch (i) {
                    case R.id.lang_select_en:
                        choose.setText(R.string.en_selected);
                        lang = "en";
                        break;
                    case R.id.lang_select_de:
                        choose.setText(R.string.de_selected);
                        lang = "de";
                        break;
                    default:
                        lang = "none";
                }
            }
        });
    }

    public void enterWord(View view) {

        FileOutputStream fos = null;
        try {
            EditText textBoxWord = findViewById(R.id.word_enter);
            String new_word = textBoxWord.getText().toString();
            EditText textBoxTranslate = findViewById(R.id.translation);
            String translation = textBoxTranslate.getText().toString();
            String stringToDict = new_word + "|" + translation + "\n";
            if(new_word.isEmpty()) {
                Toast.makeText(this, R.string.no_word_warn, Toast.LENGTH_LONG).show();
            } else if (lang == null) {
                Toast.makeText(this, getString(R.string.choose_lang), Toast.LENGTH_LONG).show();
            } else if (translation.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_translation), Toast.LENGTH_LONG).show();
            } else {
                switch (lang) {
                    case "en":
                        fos = openFileOutput(en_words_file, MODE_APPEND);
                        break;
                    case "de":
                        fos = openFileOutput(de_words_file, MODE_APPEND);
                        break;
                }
                fos.write(stringToDict.getBytes());
                textBoxWord.setText("");
                textBoxTranslate.setText("");
                Toast.makeText(this, getString(R.string.done), Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if(fos!=null) {
                    fos.close();
                }
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addCancel(View view) {
        finish();
        /*Intent MainActivityAct = new Intent(AddNewWord.this, MainActivity.class);
        startActivity(MainActivityAct);*/
    }
}
