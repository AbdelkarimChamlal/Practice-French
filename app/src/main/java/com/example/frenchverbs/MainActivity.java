package com.example.frenchverbs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Random;



//this is just testing.
public class MainActivity extends AppCompatActivity {
    EditText input;
    Button check,help;
    String result;
    TextView verb,temp,sujet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.replay);
        check = findViewById(R.id.submit);
        help  = findViewById(R.id.help);
        verb  = findViewById(R.id.verb);
        temp  = findViewById(R.id.temp);
        sujet = findViewById(R.id.sujet);
        doTheJob();
        onCheck();
        onHelp();
    }
    public void onCheck(){
        check.setOnClickListener((view)->{
            if(input.getText().toString().equals(result)){
                Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();
                doTheJob();
            }else{
                Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onHelp(){
        help.setOnClickListener((view)->{
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        });
    }
    public void doTheJob(){
        Thread everything = new Thread(()->{
            Random rand = new Random();
            int tmp = rand.nextInt(4);
            if(tmp==0){
                temp.setText("Futur");
            }else if(tmp==1){
                temp.setText("Présent");
            }else if(tmp==2){
                temp.setText("Imparfait");
            }else if(tmp==3){
                temp.setText("Passé Simple");
            }
            sujet.setText("Ils/elles");
            verb.setText("Avoir");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.43.174:8000/polls/avoir/"+tmp+"/3p")
                    .build();

            try {
                result = client.newCall(request).execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        everything.start();
    }
}
