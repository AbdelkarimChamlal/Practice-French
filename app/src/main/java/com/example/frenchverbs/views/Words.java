package com.example.frenchverbs.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frenchverbs.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Words extends AppCompatActivity {
    TextView word;
    Button word_submit,help,skip;
    EditText input;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        word = findViewById(R.id.word);
        word_submit = findViewById(R.id.check_word);
        help = findViewById(R.id.help_word);
        skip = findViewById(R.id.skip_word);
        input = findViewById(R.id.word_submit);
        onWord();
        onHelp();
        onSkip();
        updateData();
    }
    public void updateData(){
        Thread sendRequest = new Thread(()->{
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                    .url("http://192.168.1.40:8000/polls/word/")
                    .addHeader("Content-Type","text/html; charset=utf-8")
                    .build();
                Response response = client.newCall(request).execute();
                String newJsonReplay = response.body().string();
                newJsonReplay = newJsonReplay.replaceAll("Ã©","é");
                newJsonReplay = newJsonReplay.replaceAll("Ãª","ê");
                newJsonReplay = newJsonReplay.replaceAll("Ã¨","è");
                newJsonReplay = newJsonReplay.replaceAll("Å“","œ");
                newJsonReplay = newJsonReplay.replaceAll("Ã","à");
                newJsonReplay = newJsonReplay.replaceAll("Ã¡","á");
                newJsonReplay = newJsonReplay.replaceAll("Ã¢","â");
                newJsonReplay = newJsonReplay.replaceAll("Ã§","ç");
                newJsonReplay = newJsonReplay.replaceAll("ã©","é");
                newJsonReplay = newJsonReplay.replaceAll("ãª","ê");
                newJsonReplay = newJsonReplay.replaceAll("ã¨","è");
                newJsonReplay = newJsonReplay.replaceAll("ã“","œ");
                newJsonReplay = newJsonReplay.replaceAll("ã","à");
                newJsonReplay = newJsonReplay.replaceAll("ã¡","á");
                newJsonReplay = newJsonReplay.replaceAll("ã¢","â");
                newJsonReplay = newJsonReplay.replaceAll("ã§","ç");
                JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(newJsonReplay);
                word.setText(jsonObject.get("word").toString());
                result = jsonObject.get("mot").toString();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sendRequest.start();
    }

    public void onWord(){
        word_submit.setOnClickListener((view -> {
            if(input.getText().toString().equals(result)){
                Toast.makeText(getApplicationContext(),"Correct ^^",Toast.LENGTH_SHORT).show();
                input.setText("");
                updateData();
            }else{
                Toast.makeText(getApplicationContext(),"Wrong :'( ",Toast.LENGTH_SHORT).show();
            }
        }));
    }
    public void onHelp(){
        help.setOnClickListener((view)->{
            Toast.makeText(getApplicationContext(),"Le Mot  : "+result,Toast.LENGTH_LONG).show();
        });
    }
    public void onSkip(){
        skip.setOnClickListener((view)->{
            input.setText("");
            updateData();
        });
    }
}
