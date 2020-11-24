package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    private int kofeiini = 0;
    private int juoma = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText kofeiininMaara = (EditText) findViewById(R.id.kofeiininMaara);
        EditText juomanMaara = (EditText) findViewById(R.id.juomanMaara);
        Button tallenna = (Button) findViewById(R.id.tallenna);
        Button hae = (Button) findViewById(R.id.hae);
        Button kahvi = (Button) findViewById(R.id.kahviNappi);


        tallenna.setOnClickListener(v -> {
            SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            kofeiini = Integer.parseInt(kofeiininMaara.getText().toString());
            juoma = Integer.parseInt(juomanMaara.getText().toString());
            editor.putInt("kofeiini", kofeiini);
            editor.putInt("maara", juoma);
            editor.apply();
        });

        hae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
                kofeiini = sp.getInt("kofeiini", 0);
                juoma = sp.getInt("maara", 0);
                Update();
            }
        });

        kahvi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kahvia();

            }
        });

    }
    private void Update(){
        TextView kofeiiniYht = (TextView) findViewById(R.id.yhtKofeiini);
        TextView juomaYht = (TextView) findViewById(R.id.yhtMaara);
        kofeiiniYht.setText(String.valueOf(kofeiini));
        juomaYht.setText(String.valueOf(juoma));
    }
    private void kahvia(){
        Intent intent = new Intent(this, kahviLaskuri.class);
        startActivity(intent);
    }


}