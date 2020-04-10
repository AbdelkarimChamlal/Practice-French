package com.example.frenchverbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frenchverbs.views.Verbs;
import com.example.frenchverbs.views.Words;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Random;



public class MainActivity extends AppCompatActivity {
    Button words,verbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        words = findViewById(R.id.practice_words);
        verbs = findViewById(R.id.practice_verbs);
        onVerb();
        onWord();

    }

    public void onWord(){
        words.setOnClickListener((view)->{
           Intent i = new Intent(this, Words.class);
           startActivity(i);
        });
    }
    public void onVerb(){
        verbs.setOnClickListener((view)->{
            Intent i = new Intent(this, Verbs.class);
            startActivity(i);
        });
    }

}

