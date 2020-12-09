package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /**
     *
     */

    private int allKofeiini = 0; //contains all the caffeine during the day//
    private int kofeiini = 0; //contains caffeine value in the drink
    private float kofeiiniFloat = 0.0f; //caffeine in one g/ml in the selected product
    private int juoma = 0; // g/ml in portion
    private ProgressBar progrBar; //percentage circle
    private TextView text; //text in the percentage circle
    private int kahviIndex; //index of the selected product from the Spinner
    private String barcode = barcodeReader.barcodeData; //String value received from the barcode reader
    Calendar calendar = Calendar.getInstance(); //creating the calendar for receiving today's date
    Date date = calendar.getTime(); //receiving today´s day and time
    String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()); //simple date format
    private String dayOfTheWeek = currentday; //contains today´s date

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connections between private values and UI elements
        EditText juomanMaara = (EditText) findViewById(R.id.juomanMaara);
        Button tallenna = (Button) findViewById(R.id.tallenna);
        Button scanner = (Button) findViewById(R.id.scanner);

        Spinner kahviSpinner = findViewById(R.id.kahviSpinner);

        //Connection between ArrayList with data and UI element (dropdown list of items with caffeine)
        kahviSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                GlobalModel.getInstance().getKahvi()
        ));

        //Connections between private values and UI elements
        progrBar = (ProgressBar) findViewById(R.id.progress_bar);
        text = (TextView) findViewById(R.id.text_view_progress);

        /*checking if the today´s date is equal to the earlier saved date*/
        if (checkDay(dayOfTheWeek)) { //if is equal
            grabValues(); //collect data
        } else {
            clearData(); //if the date is not equal - clear data
        }

        /*when the "Save" button is clicked*/
        tallenna.setOnClickListener(v -> {
            //creating the storage for data
            SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            //if barcode value receives data
            if (barcode != "") {

                int kofeiiniIndex = GlobalModel.getInstance().getIndex(barcode); //receives the index of product with matched barcode

                //receiving today's date
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());

                kofeiiniFloat = GlobalModel.getInstance().getKahvi(kofeiiniIndex).getCaffeine(); //receives the index of caffeine of selected product
                juoma = Integer.parseInt(juomanMaara.getText().toString()); //receives portion size
                kofeiini = kofeiiniCalculation(kofeiiniFloat, juoma); //receives amount of caffeine
                allKofeiini = allKofeiini + kofeiini; //calculates total amount of consumed caffeine

                //saving data
                editor.putInt("kofeiini", kofeiini);
                editor.putInt("allKofeiini", allKofeiini);
                editor.putInt("maara", juoma);
                editor.putString("currentDay", currentday);
                editor.apply();

                barcode = ""; //cleans barcode
                Update(); //updating the state

                //saving information from the Spinner
            } else {
                Calendar calendar = Calendar.getInstance();
//                Date date = calendar.getTime();
//                String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
//                dayOfTheWeek = currentday;
                kofeiiniFloat = GlobalModel.getInstance().getKahvi(kahviIndex).getCaffeine();
                juoma = Integer.parseInt(juomanMaara.getText().toString());
                kofeiini = kofeiiniCalculation(kofeiiniFloat, juoma);
                allKofeiini = allKofeiini + kofeiini;
                editor.putInt("kofeiini", kofeiini);
                editor.putInt("allKofeiini", allKofeiini);
                editor.putInt("maara", juoma);
                editor.putString("currentDay", currentday);
                editor.apply();
                Update();
            }

            juomanMaara.setText(""); //clears the input field
            Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show(); //Toast message
        });

        //item is selected from the Spinner

        kahviSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int kahviSelected = (int) parent.getItemIdAtPosition(position);
                kahviIndex = kahviSelected; //receiving the index of selected product
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Scanner button is pressed
        scanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setScanner(); //barcode reader mode opens

            }
        });
    }


    @Override
    public void onResume() {
        Calendar calendar = Calendar.getInstance(); //creating the calendar for receiving today's date
        Date date = calendar.getTime(); //receiving today´s day and time
        String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()); //simple date format
        super.onResume();
        if (checkDay(currentday)) { //if is equal
            grabValues(); //save the input date to the existing stack
        } else {
            clearData();//if the date is not equal - clear data
        }

    }

    /*Main page information updating*/

    private void Update() {
        TextView kofeiiniYht = (TextView) findViewById(R.id.yhtKofeiini);
        TextView juomaYht = (TextView) findViewById(R.id.yhtMaara);
        kofeiiniYht.setText(String.valueOf(kofeiini) + " mg.");
        juomaYht.setText(String.valueOf(juoma) + " g/ml.");
        progrBar.setProgress(procent(allKofeiini));
        text.setText(procent(allKofeiini) + "%");
    }

    /*starts new activity (barcode scanner)*/
    private void setScanner() {
        Intent intent = new Intent(this, barcodeReader.class);
        startActivity(intent);
    }

//    private int getObjectIndex() {
//        int index = 0;
//        return index;
//    }

    /*calculates the exact value of caffeine in portion*/
    private int kofeiiniCalculation(float f, int a) {
        int kofeiini = (int) (a * f);
        return kofeiini;
    }

    /*calculates the percent of consumed caffeine*/
    private int procent(int a) {
        a = a * 100 / 400; //400 mg is the maximum recommended doze
        return a;
    }

    /*saving data*/
    private void grabValues() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        kofeiini = sp.getInt("kofeiini", 0);
        juoma = sp.getInt("maara", 0);
        allKofeiini = sp.getInt("allKofeiini", 0);
        Update();
    }

    /*if today's date is equal to the earlier saved date - returns true*/
    private boolean checkDay(String a) {
        boolean result = false;
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String currentday = sp.getString("currentDay", "");
        if (a.equals(currentday)) {
            result = true;
        }
        return result;
    }

    /*clears the data*/
    private void clearData() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        sp.edit().remove("currentDay");
        sp.edit().remove("kofeiini");
        sp.edit().remove("maara");
        sp.edit().remove("allKofeiini");
    }

}