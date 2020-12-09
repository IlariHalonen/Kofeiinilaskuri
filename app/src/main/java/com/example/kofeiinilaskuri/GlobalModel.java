package com.example.kofeiinilaskuri;

import java.util.ArrayList;

class GlobalModel {
    private static final GlobalModel ourInstance = new GlobalModel();

    private ArrayList<Coffee> kahvi;

    static GlobalModel getInstance() {
        return ourInstance;
    }

    private GlobalModel() {

        kahvi = new ArrayList<Coffee>();

        kahvi.add(new Coffee("Musta kahvi", (float) 0.6, "4056489175803"));
        kahvi.add(new Coffee("Kahvi maidolla", (float)0.4, "070177084813"));
        kahvi.add(new Coffee("Energiajuoma", (float)0.54,"6420101884201"));
        kahvi.add(new Coffee("Maitosuklaa", (float)0.12,"6420101884202"));
        kahvi.add(new Coffee("Tumma suklaa", (float)0.7,"6420101884203"));
        kahvi.add(new Coffee("Musta tee", (float)0.2,"6420101884204"));

    }

    public ArrayList<Coffee> getKahvi() {
        return kahvi;
    }

    public Coffee getKahvi(int i) { return kahvi.get(i);}


    public int getIndex(String barcode) {
        String name = "";
        int result = 0;
        for (int i = 0; i < kahvi.size(); i++) {
            String value = kahvi.get(i).getBarcode();
            if ( value.equals(barcode)) {
                name = kahvi.get(i).getName();
                result = kahvi.indexOf(kahvi.get(i));
            }
        }
        return result;
    }

}
