package com.example.frenchverbs.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class Verbs extends AppCompatActivity {
    EditText answer,meaningInput;
    Button check,help,skip,wrong_dialog_skip,wrong_dialog_try,help_dialog_close;
    TextView skipped,correct,wrong,askedForHelp,question_one,question_two,help_dialog_q1,help_dialog_q2;
    String result,meaning;
    Dialog dg,correct_dialog,wrong_dialog,help_dialog;
    static int correctScore,wrongScore,skippedScore,askedForHelpScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //full screen code from
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_verbs);
        answer = findViewById(R.id.answer);
        meaningInput =findViewById(R.id.meaning);
        check = findViewById(R.id.check);
        help = findViewById(R.id.help);
        skip = findViewById(R.id.skip);
        question_one = findViewById(R.id.question_one);
        question_two = findViewById(R.id.question_two);
        skipped=findViewById(R.id.skip_once);
        correct=findViewById(R.id.correct_answers);
        wrong=findViewById(R.id.incorrect);
        askedForHelp=findViewById(R.id.asked_for_help);
        dg = new Dialog(this);
        dg.setContentView(R.layout.loading_data);

        help_dialog = new Dialog(this);
        help_dialog.setContentView(R.layout.help_dialog);
        help_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        help_dialog.setCancelable(false);
        help_dialog_close = help_dialog.findViewById(R.id.help_dialog_close);
        help_dialog_q1 = help_dialog.findViewById(R.id.help_dialog_q1);
        help_dialog_q2 = help_dialog.findViewById(R.id.help_dialog_q2);
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
                sendRequestAndUpdateData(0);
                skippedScore++;
                skipped.setText(""+skippedScore);
            });
            wrong_dialog.cancel();
        });




        dg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dg.setCancelable(false);
        sendRequestAndUpdateData(0);
        onHelp();
        onCheck();
        onSkip();
    }

    public void onCheck(){
        check.setOnClickListener((view)->{
            if(answer.getText().toString().equals(result) && meaningInput.getText().toString().equals(meaning)){
                answer.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.showSoftInput(answer, InputMethodManager.SHOW_IMPLICIT);
                answer.setText("");
                meaningInput.setText("");
                correctScore++;
                correct.setText(""+correctScore);
                sendRequestAndUpdateData(1);
            }else{
                wrong_dialog.show();
                wrongScore++;
                wrong.setText(""+wrongScore);
            }
        });
    }
    public void onHelp(){
        help.setOnClickListener((view)->{
            runOnUiThread(()->{
                askedForHelpScore++;
                askedForHelp.setText(""+askedForHelpScore);
                help_dialog.show();
                help_dialog_q1.setText(result);
                help_dialog_q2.setText(meaning);
            });
        });
    }
    public void onSkip(){
        skip.setOnClickListener((view)->{
            skippedScore++;
            meaningInput.setText("");
            skipped.setText(""+skippedScore);
            answer.setText("");
            sendRequestAndUpdateData(0);
        });
    }

    public void sendRequestAndUpdateData(int i){
        Thread sendRequest = new Thread(()->{
            try {
                runOnUiThread(()->{
                    if(i==1){
                        correct_dialog.show();
                    }else{
                        dg.show();
                    }
                });
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                    .url("http://192.168.1.40:8000/polls/request/")
                    .addHeader("Content-Type","text/html; charset=utf-8")
                    .build();
                Response response = client.newCall(request).execute();
                String jsonReplay = response.body().string();
                String newJsonReplay = jsonReplay.replaceAll("'","\"");
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
                System.out.println(newJsonReplay);
                JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(newJsonReplay);
                result = jsonObject.get("result").toString();
                meaning = jsonObject.get("meaning").toString();
                String q1 = "conjugue le verbe "+jsonObject.get("verb").toString()+" au "+jsonObject.get("temp").toString()+ " en ";

                if(jsonObject.get("sujet").toString().equals("1s")){
                    q1 = q1 +"1er personne du singulier";
                }else if(jsonObject.get("sujet").toString().equals("2s")){
                    q1 = q1 +"2ème personne du singulier";
                }else if(jsonObject.get("sujet").toString().equals("3s")){
                    q1 = q1 +"3ème personne du singulier";
                }else if(jsonObject.get("sujet").toString().equals("1p")){
                    q1 = q1 +"1er personne du pluriel";
                }else if(jsonObject.get("sujet").toString().equals("2p")){
                    q1 = q1 +"2ème personne du pluriel";
                }else if(jsonObject.get("sujet").toString().equals("3p")){
                    q1 = q1 +"3ème personne du pluriel";
                }
                String finalQ = q1;
                runOnUiThread(()->{
                    question_one.setText(finalQ);
                    question_two.setText("donnez la traduction du verbe "+jsonObject.get("verb").toString()+" en anglais");
                });


            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                runOnUiThread(()->{
                    if(i==1){
                        correct_dialog.cancel();
                    }else{
                        dg.cancel();
                    }
                });
            }
        });
        sendRequest.start();
    }
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//
//        }
//    }
//    private void hideSystemUI() {
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//            View.SYSTEM_UI_FLAG_IMMERSIVE
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM);
//    }

    public void createDialog(){
        Dialog dg = new Dialog(getApplicationContext());
        dg.setContentView(R.layout.loading_data);
        dg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dg.setCancelable(false);
        dg.show();
    }
}

