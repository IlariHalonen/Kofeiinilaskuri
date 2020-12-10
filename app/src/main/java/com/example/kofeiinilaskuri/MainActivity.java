package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    /**
     *Variables
     */

    private int day = 0; //contains all the caffeine during the day//
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
    private final String AVAIN = "com.example.kofeiinilaskuri.PROFIILI_KEY";
    private String liite = "ml";
    private TextView scannedName;
    private Spinner kahviSpinner;

    /**
     * Called onCreate
     * @param savedInstanceState
     */


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Checks Connections between private values and UI elements
         */
        checkFirstTime();

        EditText juomanMaara = (EditText) findViewById(R.id.juomanMaara);
        Button tallenna = (Button) findViewById(R.id.tallenna);
        Button scanner = (Button) findViewById(R.id.scanner);
        ImageView profileButton = (ImageView) findViewById(R.id.profileIcon);
        ImageView infoButton = (ImageView) findViewById(R.id.infoIcon);
        kahviSpinner = findViewById(R.id.kahviSpinner);
        scannedName = (TextView) findViewById(R.id.scannedName);

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

        /**
         * Called when the "Save" button is clicked
         */

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();

            }
        });
        /**
         * Called when Info icon is pressed
         */

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfo(); //Info activity opens

            }
        });
        /**
         * Called when Tallenna button is pressed
         */

        tallenna.setOnClickListener(v -> {
            //creating the storage for data
            SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            /**
             * If barcode value receives data
             */
            if (barcode != "") {

                int kofeiiniIndex = GlobalModel.getInstance().getIndex(barcode); //receives the index of product with matched barcode
                kofeiiniFloat = GlobalModel.getInstance().getKahvi(kofeiiniIndex).getCaffeine(); //receives the index of caffeine of selected product
                //receiving today's date
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
                juoma = Integer.parseInt(juomanMaara.getText().toString()); //receives portion size
                kofeiini = kofeiiniCalculation(kofeiiniFloat, juoma); //receives amount of caffeine
                day = day + kofeiini; //calculates total amount of consumed caffeine
                //saving data
                editor.putInt("kofeiini", kofeiini);
                editor.putInt("allKofeiini", day);
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
                day = day + kofeiini;
                editor.putInt("kofeiini", kofeiini);
                editor.putInt("allKofeiini", day);
                editor.putInt("maara", juoma);
                editor.putString("currentDay", currentday);
                editor.apply();
                Update();
            }

            juomanMaara.setText(""); //clears the input field
            Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show(); //Toast message
        });

        /**
         * This method is called when item is selected from the Spinner
         */

        kahviSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int kahviSelected = (int)parent.getItemIdAtPosition(position); //receiving the index of selected product
                kahviIndex = kahviSelected;
                juomanMaara.setHint(tarkistaLiite(kahviIndex));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Called when scanner button is pressed
         */
        scanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setScanner(); //barcode reader mode opens

            }
        });
    }

    /**
     * This method is called when onResume
     */


    @Override
    public void onResume() {
        Calendar calendar = Calendar.getInstance(); //creating the calendar for receiving today's date
        Date date = calendar.getTime(); //receiving today´s day and time
        String currentday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()); //simple date format
        super.onResume();
        if (barcode != ""){
            String drinksunnimi = GlobalModel.getInstance().checkBarcode(barcode);
            float kofeiininMaara = GlobalModel.getInstance().getKahvi(GlobalModel.getInstance().getIndex(barcode)).getCaffeine();
            scannedName.setText("Viimeksi skannattu: " + drinksunnimi +"\n" + " Kofeiinia: " + kofeiininMaara + "mg/100ml");
            kahviSpinner.setSelection(GlobalModel.getInstance().getIndex(barcode));
        }
        if (checkDay(currentday)) { //if is equal
            grabValues(); //save the input date to the existing stack
        } else {
            clearData();//if the date is not equal - clear data
        }

    }

    /**
     * This method updates main page information when called
     */

    private void Update() {
        TextView kofeiiniYht = (TextView) findViewById(R.id.yhtKofeiini);
        TextView juomaYht = (TextView) findViewById(R.id.yhtMaara);
        gOrMl();
        kofeiiniYht.setText(String.valueOf(kofeiini) + " mg");
        juomaYht.setText(String.valueOf(juoma) + liite);
        progrBar.setProgress(procent(day));
        text.setText(procent(day) + "%");
        if (day > 400){
            showToast();
        }
    }

    /**
     * This method is called when Int day get value of 400
     * Shows alert toast
     */
    private void showToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.toast_layout));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration((Toast.LENGTH_SHORT));
        toast.setView(layout);
        toast.show();
    }

    /**
     * This method starts new activity (barcode scanner)
     */
    private void setScanner() {
        Intent intent = new Intent(this, barcodeReader.class);
        startActivity(intent);
    }

    /**
     * This method starts new activity when profile icon is pressed
     */
    private void openProfile(){
        int prog = progrBar.getProgress();
        String perc = text.getText().toString();
        Intent intent = new Intent(this, Profiili.class);
        intent.putExtra("EXTRA_PROG",prog);
        intent.putExtra("EXTRA_PERC",perc);
        intent.putExtra("EXTRA_CAF",String.valueOf(day));
        startActivity(intent);
    }

    /**
     * This method starts new activity (webview) when info icon is pressed
     */
    private void openInfo(){
        Intent intent = new Intent(this, Info.class);
        startActivity(intent);
    }

//    private int getObjectIndex() {
//        int index = 0;
//        return index;
//    }

    /**
     * calculates the exact value of caffeine in portion
     * @param f
     * @param a
     * @return Calculation
     */
    private int kofeiiniCalculation(float f, int a) {
        int kofeiini = (int) (a * f);
        return kofeiini;
    }

    /**
     * This method calculates the percent of consumed caffeine
     * @param a
     * @return Percent of caffeine doze
     */
    private int procent(int a) {
        a = a * 100 / 400; //400 mg is the maximum recommended doze
        return a;
    }

    /**
     * This method Grabs values from shared preferences
     */
    private void grabValues() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        kofeiini = sp.getInt("kofeiini", 0);
        juoma = sp.getInt("maara", 0);
        day = sp.getInt("allKofeiini", 0);
        Update();
    }

    /**
     * if today's date is equal to the earlier saved date - returns true
     * @param a
     * @return Current day
     */
    private boolean checkDay(String a) {
        boolean result = false;
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String currentday = sp.getString("currentDay", "");
        if (a.equals(currentday)) {
            result = true;
        }
        return result;
    }

    /**
     * This method clears the data
     */
    private void clearData() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        sp.edit().remove("currentDay");
        sp.edit().remove("kofeiini");
        sp.edit().remove("maara");
        sp.edit().remove("allKofeiini");
    }

    /**
     * This is called when opening the app first time to save shared preferences
     */
    private void checkFirstTime(){
        SharedPreferences sp = getSharedPreferences(AVAIN,Context.MODE_PRIVATE);
        boolean first = sp.getBoolean("FIRST_TIME", true);
        if (first){
            Intent intent = new Intent(this, UserConfig.class);
            startActivity(intent);
        }




    }

    /**
     * This method checks if the product contains the word "suklaa" and changes the prefix accordingly
     */
    public void gOrMl(){
        String index = GlobalModel.getInstance().getKahvi(kahviIndex).getName();
        if(index.contains("suklaa")){
            liite = " g";
        } else if(index.contains("tabletti")) {
            liite = " kpl";
        } else {
            liite = " ml";
        }


    }

    /**
     * This method checks the products index and changes the prefix accordingly
     * @param index
     * @return ml, g or kpl
     */
    public String tarkistaLiite(int index){
        if (index == 6 || index == 7){
            return "g";
        } else if(index == 10) {
            return "kpl";
        } else {
            return "ml";
        }
    }


}
