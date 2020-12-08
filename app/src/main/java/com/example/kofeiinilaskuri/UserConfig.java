package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.controls.templates.TemperatureControlTemplate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class UserConfig extends AppCompatActivity {
    private final String AVAIN = "com.example.kofeiinilaskuri.PROFIILI_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        boolean first = sp.getBoolean("FIRST_TIME", true);
        if (!first){
            finish();
        }

        Button namiska = (Button) findViewById(R.id.button_valmis);
        namiska.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isEmpty()){
                    loadMain();
                    finish();
                }
            }
        });

    }

    private void popup(){
        Toast.makeText(getApplicationContext(), "Nimeä tai ikää ei voi jättää tyhjäksi", Toast.LENGTH_SHORT).show();

    }

    private boolean isEmpty(){
        EditText kayttajanNimi = (EditText) findViewById(R.id.kayttajanNimi);
        EditText kayttajanIka = (EditText) findViewById(R.id.kayttajanIka);
        if (kayttajanIka.getText().toString().trim().length() == 0 || kayttajanNimi.getText().toString().trim().length() == 0){
            popup();
            return true;
        }
        return false;

    }
    private void loadMain(){
        EditText kayttajanNimi = (EditText) findViewById(R.id.kayttajanNimi);
        EditText kayttajanIka = (EditText) findViewById(R.id.kayttajanIka);

        SharedPreferences sp = getSharedPreferences(AVAIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String nimi = kayttajanNimi.getText().toString();
        int ika =  Integer.parseInt(kayttajanIka.getText().toString());
        editor.putString("NIMI", nimi);
        editor.putInt("IKA",ika);
        editor.putBoolean("FIRST_TIME", false);
        editor.commit();
    }
}