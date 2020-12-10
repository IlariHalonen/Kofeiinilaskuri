package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

public class Profiili extends AppCompatActivity {
    private final String AVAIN = "com.example.kofeiinilaskuri.PROFIILI_KEY"; //Key for the memory
    private int prog = 0;
    private String perc;
    private String kofeiiniPv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);
        getExtras(); //void getExtras() is executed in the beginning

        SharedPreferences sp = getSharedPreferences(AVAIN,Context.MODE_PRIVATE);
        String nimi = sp.getString("NIMI", "");
        int ika = sp.getInt("IKA",0);
        TextView naytNimi = (TextView) findViewById(R.id.nameField);
        naytNimi.setText(nimi);

        /*Declaring the UI elements*/
        ProgressBar progBar = (ProgressBar) findViewById(R.id.progress_bar_prof);
        TextView percent = (TextView) findViewById(R.id.percent);
        TextView millig = (TextView) findViewById(R.id.textView3);

        /*Sets the percentage and progressbar*/
        progBar.setProgress(prog);
        percent.setText(perc);
        millig.setText("Tänään nautittu: "+ kofeiiniPv + " mg kofeiinia.");


    }
    /*Bundles all the extras from the intent*/
    public void getExtras(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            prog = extras.getInt("EXTRA_PROG");
            perc = extras.getString("EXTRA_PERC");
            kofeiiniPv = extras.getString("EXTRA_CAF");
        }
    }



}