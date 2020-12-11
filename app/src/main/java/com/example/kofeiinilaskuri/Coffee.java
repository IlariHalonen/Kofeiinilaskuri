package com.example.kofeiinilaskuri;

public class Coffee {


    private String name;
    private float caffeine;
    private String barcode;

    /**
     *
     * @author Niini, Miikka
     * @param name
     * @param caffeine
     * @param barcode
     */

    public Coffee(String name, float caffeine, String barcode) {
        this.name = name;
        this.caffeine = caffeine;
        this.barcode = barcode;
    }

    /**
     * This method gets name of the coffee
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method gets caffeine amount
     * @return caffeine
     */

    public float getCaffeine() {
        return this.caffeine;
    }

    /**
     * This method gets barcode
     * @return barcode
     */

    public String getBarcode() {
        return this.barcode;
    }

    /**
     * This method returns name toString
     * @return name
     */
    @Override
    public String toString() {
        return name;
    }


}
