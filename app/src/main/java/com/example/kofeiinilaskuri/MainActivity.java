package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int day = 0;
    private int kofeiini = 0;
    private float kofeiiniFloat = 0.0f;
    private int juoma = 0;
    private ProgressBar progrBar;
    private TextView text;
    private int kahviIndex;
    private String barcode = barcodeReader.barcodeData;
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
    private String dayOfTheWeek = currentday;
    private final String AVAIN = "com.example.kofeiinilaskuri.PROFIILI_KEY";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkFirstTime();

        EditText juomanMaara = (EditText) findViewById(R.id.juomanMaara);
        Button tallenna = (Button) findViewById(R.id.tallenna);
        Button scanner = (Button) findViewById(R.id.scanner);
        ImageView profileButton = (ImageView) findViewById(R.id.profileIcon);

        Spinner kahviSpinner = findViewById(R.id.kahviSpinner);
        kahviSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                GlobalModel.getInstance().getKahvi()
        ));

        progrBar = (ProgressBar) findViewById(R.id.progress_bar);
        text = (TextView) findViewById(R.id.text_view_progress);

        if(checkDay(dayOfTheWeek)) {
           grabValues();
        } else {
            clearData();
        }


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();

            }
        });

        tallenna.setOnClickListener(v -> {
            SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
                if( barcode != "") {

                    int kofeiiniIndex = GlobalModel.getInstance().getIndex(barcode);
                    Calendar calendar = Calendar.getInstance();
                    Date date = calendar.getTime();
                    String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
                    kofeiiniFloat = GlobalModel.getInstance().getKahvi(kofeiiniIndex).getCaffeine();
                    juoma = Integer.parseInt(juomanMaara.getText().toString());
                    kofeiini = kofeiiniCalculation(kofeiiniFloat, juoma);
                    day = day + kofeiini;
                    editor.putInt("kofeiini",kofeiini);
                    editor.putInt("allKofeiini", day);
                    editor.putInt("maara", juoma);
                    editor.putString("currentDay", currentday);
                    editor.apply();
                    barcode = "";
                    Update();

                } else {
                    Calendar calendar = Calendar.getInstance();
                    Date date = calendar.getTime();
                    String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
                    dayOfTheWeek = currentday;
                    kofeiiniFloat = GlobalModel.getInstance().getKahvi(kahviIndex).getCaffeine();
                    juoma = Integer.parseInt(juomanMaara.getText().toString());
                    kofeiini = kofeiiniCalculation(kofeiiniFloat, juoma);
                    day = day + kofeiini;
                    editor.putInt("kofeiini",kofeiini);
                    editor.putInt("allKofeiini", day);
                    editor.putInt("maara", juoma);
                    editor.putString("currentDay", currentday);
                    editor.apply();
                    Update();
                }

            juomanMaara.setText("");
            Toast.makeText(getApplicationContext(),"Saved!",Toast.LENGTH_SHORT).show();
        });

        kahviSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int kahviSelected = (int)parent.getItemIdAtPosition(position);
                kahviIndex = kahviSelected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setScanner();

            }
        });
    }

    private void Update() {
        TextView kofeiiniYht = (TextView) findViewById(R.id.yhtKofeiini);
        TextView juomaYht = (TextView) findViewById(R.id.yhtMaara);
        kofeiiniYht.setText(String.valueOf(kofeiini) + " mg.");
        juomaYht.setText(String.valueOf(juoma) + " g/ml.");
        progrBar.setProgress(procent(day));
        text.setText(procent(day) + "%");
    }

    private void setScanner() {
        Intent intent = new Intent(this, barcodeReader.class);
        startActivity(intent);
    }
    private void openProfile(){
        Intent intent = new Intent(this, Profiili.class);
        startActivity(intent);
    }

    private int getObjectIndex() {
        int index = 0;
        return index;
    }

    private int kofeiiniCalculation(float f, int a){
        int kofeiini = (int)(a * f);
        return kofeiini;
    }

    private int procent(int a){
        a = a*100/400;
        return a;
    }

    private void grabValues() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        kofeiini = sp.getInt("kofeiini", 0);
        juoma = sp.getInt("maara", 0);
        day = sp.getInt("allKofeiini",0);
        Update();
    }

    private boolean checkDay(String a) {
        boolean result = false;
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        if(a.equals(sp.getString("currentDay", ""))) {
            result = true;
        }
        return result;
    }

    private void clearData() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        sp.edit().remove("currentDay");
        sp.edit().remove("kofeiini");
        sp.edit().remove("maara");
        sp.edit().remove("allKofeiini");
    }

    private void checkFirstTime(){
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        boolean first = sp.getBoolean("FIRST_TIME", false);
        if (!first){
            Intent intent = new Intent(this, UserConfig.class);
            startActivity(intent);
        }

    }

}