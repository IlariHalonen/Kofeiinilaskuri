package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

public class Profiili extends AppCompatActivity {
    private final String AVAIN = "com.example.kofeiinilaskuri.PROFIILI_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);

        SharedPreferences sp = getSharedPreferences(AVAIN,Context.MODE_PRIVATE);
        String nimi = sp.getString("NIMI", "");
        int ika = sp.getInt("IKA",0);
        TextView naytNimi = (TextView) findViewById(R.id.nameField);
        naytNimi.setText(nimi);

        Kayttaja kayttaja = new Kayttaja(ika, nimi);



    }

}