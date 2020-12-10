package com.example.kofeiinilaskuri;

/**
 * UserConfig.java is only used on the first start.
 *
 * @author Ilari
 * @Version 1.6
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class UserConfig extends AppCompatActivity {
    private final String AVAIN = "com.example.kofeiinilaskuri.PROFIILI_KEY"; //Key for the memory

    /**
     * Called onCreate
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);
        SharedPreferences sp = getSharedPreferences(AVAIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("FIRST_TIME", false).commit();

        Button namiska = (Button) findViewById(R.id.button_valmis);


        /**
         * This method executes all the methods when the button is pressed
         */
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

    /**
     * Called when leaving a profile field empty
     */
    private void popup(){
        Toast.makeText(getApplicationContext(), "Nimeä tai ikää ei voi jättää tyhjäksi", Toast.LENGTH_SHORT).show();

    }

    /**
     * This method Checks if one of the fields is empty
     * @return If the fields are empty or not
     */
    private boolean isEmpty(){
        EditText kayttajanNimi = (EditText) findViewById(R.id.kayttajanNimi);
        EditText kayttajanIka = (EditText) findViewById(R.id.kayttajanIka);
        if (kayttajanIka.getText().toString().trim().length() == 0 || kayttajanNimi.getText().toString().trim().length() == 0){
            popup();
            return true;
        }
        return false;

    }

    /**
     * This method loads the MainActivity ans saves all the users data
     */
    private void loadMain(){
        EditText kayttajanNimi = (EditText) findViewById(R.id.kayttajanNimi);
        EditText kayttajanIka = (EditText) findViewById(R.id.kayttajanIka);
        Switch MN = (Switch) findViewById(R.id.miesNainen);
        boolean SwitchState = MN.isChecked();
        SharedPreferences sp = getSharedPreferences(AVAIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String nimi = kayttajanNimi.getText().toString();
        int ika =  Integer.parseInt(kayttajanIka.getText().toString());
        editor.putBoolean("SUKUPUOLI", SwitchState);
        editor.putString("NIMI", nimi);
        editor.putInt("IKA",ika);
        editor.putBoolean("FIRST_TIME", false);
        editor.commit();
    }
}