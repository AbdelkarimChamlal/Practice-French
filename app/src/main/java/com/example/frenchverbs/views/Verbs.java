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

public class Verbs extends AppCompatActivity {
    EditText answer,meaningInput;
    Button check,help,skip;
    TextView verb,temp,subject,skipped,correct,wrong,askedForHelp;
    String result,meaning;
    static int correctScore,wrongScore,skippedScore,askedForHelpScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbs);
        answer = findViewById(R.id.answer);
        meaningInput =findViewById(R.id.meaning);
        check = findViewById(R.id.check);
        help = findViewById(R.id.help);
        verb = findViewById(R.id.verb);
        temp = findViewById(R.id.temp);
        subject = findViewById(R.id.subject);
        skip = findViewById(R.id.skip);
        skipped=findViewById(R.id.skip_once);
        correct=findViewById(R.id.correct_answers);
        wrong=findViewById(R.id.incorrect);
        askedForHelp=findViewById(R.id.asked_for_help);
        sendRequestAndUpdateData();
        onHelp();
        onCheck();
        onSkip();
    }

    public void onCheck(){
        check.setOnClickListener((view)->{
            if(answer.getText().toString().equals(result) && meaningInput.getText().toString().equals(meaning)){
                Toast.makeText(getApplicationContext(),"Correct ^^",Toast.LENGTH_SHORT).show();
                answer.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.showSoftInput(answer, InputMethodManager.SHOW_IMPLICIT);
                answer.setText("");
                meaningInput.setText("");
                correctScore++;
                correct.setText("Correct : "+correctScore);
                sendRequestAndUpdateData();
            }else{
                Toast.makeText(getApplicationContext(),"Wrong :'( ",Toast.LENGTH_SHORT).show();
                wrongScore++;
                wrong.setText("Wrong : "+wrongScore);
            }
        });
    }
    public void onHelp(){
        help.setOnClickListener((view)->{
            askedForHelpScore++;
            askedForHelp.setText("Help : "+askedForHelpScore);
            Toast.makeText(getApplicationContext(),"Conjugation  : "+result +"\n Meaning : "+meaning,Toast.LENGTH_SHORT).show();
        });
    }
    public void onSkip(){
        skip.setOnClickListener((view)->{
            skippedScore++;
            meaningInput.setText("");
            skipped.setText("Skipped : "+skippedScore);
            answer.setText("");
            sendRequestAndUpdateData();
        });
    }

    public void sendRequestAndUpdateData(){
        Thread sendRequest = new Thread(()->{
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                    .url("http://192.168.43.174:8000/polls/request/")
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
                verb.setText(jsonObject.get("verb").toString());
                temp.setText(jsonObject.get("temp").toString());
                if(jsonObject.get("sujet").toString().equals("1s")){
                    subject.setText("je");
                }else if(jsonObject.get("sujet").toString().equals("2s")){
                    subject.setText("tu");
                }else if(jsonObject.get("sujet").toString().equals("3s")){
                    subject.setText("elle/il/on");
                }else if(jsonObject.get("sujet").toString().equals("1p")){
                    subject.setText("nous");
                }else if(jsonObject.get("sujet").toString().equals("2p")){
                    subject.setText("vous");
                }else if(jsonObject.get("sujet").toString().equals("3p")){
                    subject.setText("elles/ils");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sendRequest.start();
    }
}

