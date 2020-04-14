package com.example.frenchverbs.views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    TextView skipped,correct,wrong,askedForHelp,help_dialog_q1;
    Dialog loading_dialog,help_dialog,correct_dialog,wrong_dialog;
    Button wrong_dialog_skip,wrong_dialog_try,help_dialog_close;
    static int correctScore,wrongScore,skippedScore,askedForHelpScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_words);
        word = findViewById(R.id.word);
        word_submit = findViewById(R.id.word_submit);
        help = findViewById(R.id.help_word);
        skip = findViewById(R.id.skip_word);
        input = findViewById(R.id.answer);
        skipped=findViewById(R.id.skip_once);
        correct=findViewById(R.id.correct_answers);
        wrong=findViewById(R.id.incorrect);
        askedForHelp=findViewById(R.id.asked_for_help);



        //dialogs

        loading_dialog = new Dialog(this);
        loading_dialog.setContentView(R.layout.loading_data);
        help_dialog = new Dialog(this);
        help_dialog.setContentView(R.layout.help_dialog_words);
        help_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        help_dialog.setCancelable(false);
        help_dialog_close = help_dialog.findViewById(R.id.help_dialog_close);
        help_dialog_q1 = help_dialog.findViewById(R.id.help_dialog_q1);
        help_dialog_close.setOnClickListener((v)->{
            runOnUiThread(()->{
                help_dialog.cancel();
            });
        });



        correct_dialog = new Dialog(this);
        correct_dialog.setContentView(R.layout.correct_dialog);
        correct_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        correct_dialog.setCancelable(false);

        wrong_dialog = new Dialog(this);
        wrong_dialog.setContentView(R.layout.wrong_dialog);
        wrong_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        wrong_dialog.setCancelable(false);
        wrong_dialog_try = wrong_dialog.findViewById(R.id.wrong_dialog_try);
        wrong_dialog_try.setOnClickListener((view)->{
            wrong_dialog.cancel();
        });
        wrong_dialog_skip = wrong_dialog.findViewById(R.id.wrong_dialog_skip);
        wrong_dialog_skip.setOnClickListener((view)->{
            runOnUiThread(()->{
                updateData(0);
                skippedScore++;
                skipped.setText(""+skippedScore);
            });
            wrong_dialog.cancel();
        });
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.setCancelable(false);
        onWord();
        onHelp();
        onSkip();
        updateData(0);
    }
    public void updateData(int i){
        runOnUiThread(()->{
            if(i==1){
                correct_dialog.show();
            }else{
                loading_dialog.show();
            }
        });
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
                String temp ="Donnez la traduction du mot ou phrase suivant \""+jsonObject.get("mot").toString()+"\" en englais";
                runOnUiThread(()->{
                    word.setText(temp);
                });
                result = jsonObject.get("word").toString();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                runOnUiThread(()->{
                    if(i==1){
                        Handler handler = new Handler();
                        handler.postDelayed(()->{
                            correct_dialog.cancel();
                        },500);
                    }else{
                        loading_dialog.cancel();
                    }
                });
            }
        });
        sendRequest.start();
    }

    public void onWord(){
        word_submit.setOnClickListener((view -> {
            if(input.getText().toString().equals(result)){
                input.setText("");
                correctScore++;
                correct.setText(""+correctScore);
                updateData(1);
            }else{
                wrong_dialog.show();
                wrongScore++;
                wrong.setText(""+wrongScore);            }
        }));
    }
    public void onHelp(){
        help.setOnClickListener((view)->{
            runOnUiThread(()->{
                askedForHelpScore++;
                askedForHelp.setText(""+askedForHelpScore);
                help_dialog.show();
                help_dialog_q1.setText(result);
            });
        });
    }
    public void onSkip(){
        skip.setOnClickListener((view)->{
            skippedScore++;
            skipped.setText(""+skippedScore);
            input.setText("");
            updateData(0);
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();

        }
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
