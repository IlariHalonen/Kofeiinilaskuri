package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

public class Profiili extends AppCompatActivity {
    private final String AVAIN = "com.example.kofeiinilaskuri.PROFIILI_KEY";
    private int prog = 0;
    private String perc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);
        getExtras();

        SharedPreferences sp = getSharedPreferences(AVAIN,Context.MODE_PRIVATE);
        String nimi = sp.getString("NIMI", "");
        int ika = sp.getInt("IKA",0);
        TextView naytNimi = (TextView) findViewById(R.id.nameField);
        naytNimi.setText(nimi);

        ProgressBar progBar = (ProgressBar) findViewById(R.id.progress_bar_prof);
        TextView percent = (TextView) findViewById(R.id.percent);

        Kayttaja kayttaja = new Kayttaja(ika, nimi);
        progBar.setProgress(prog);
        percent.setText(perc);




    }
    public void getExtras(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            prog = extras.getInt("EXTRA_PROG");
            perc = extras.getString("EXTRA_PERC");
        }
    }



}