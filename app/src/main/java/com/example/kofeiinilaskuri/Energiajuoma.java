package com.example.kofeiinilaskuri;

public class Energiajuoma {

    private String nimi;
    private float kofeiininMaara;
    private String viivakoodi;

    public Energiajuoma(String nimi, float kofeiininMaarae, String viivakoodi) {
        this.nimi = nimi;
        this.kofeiininMaara= kofeiininMaara;
        this.viivakoodi = viivakoodi;
    }


    public String getName() {
        return this.nimi;
    }

    public float getCaffeine() {
        return this.kofeiininMaara;
    }

    public String getBarcode() { return this.viivakoodi; }



    @Override
    public String toString() {
        return nimi;
    }


}

