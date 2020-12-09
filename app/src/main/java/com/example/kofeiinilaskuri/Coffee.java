package com.example.kofeiinilaskuri;

public class Coffee {

    private String name;
    private float caffeine;
    private String barcode;

    public Coffee(String name, float caffeine, String barcode) {
        this.name = name;
        this.caffeine = caffeine;
        this.barcode = barcode;
    }


    public String getName() {
        return this.name;
    }

    public float getCaffeine() {
        return this.caffeine;
    }

    public String getBarcode() {
        return this.barcode;
    }



    @Override
    public String toString() {
        return name;
    }


}
