package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

public class kahviLaskuri extends AppCompatActivity {

    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kahvi_laskuri);

        Intent intent = getIntent();

        NumberPicker np = findViewById(R.id.numberPicker);
        textView = findViewById(R.id.kahvilaskuri);

        np.setMinValue(0);
        np.setMaxValue(50);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                textView.setText("Juotuja kuppeja: " + newVal);
            }
        });
    }
}