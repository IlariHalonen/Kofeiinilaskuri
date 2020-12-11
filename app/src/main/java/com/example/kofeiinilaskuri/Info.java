package com.example.kofeiinilaskuri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Info extends AppCompatActivity {

    WebView web;

    /**
     * Called onCreate when pressing info button
     * @param savedInstanceState
     * @author Niini
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        web = findViewById(R.id.webView);
        WebSettings webSettings = web.getSettings(); //creates web settings object
        webSettings.setJavaScriptEnabled(true); //enables javascript using web settings object
        web.setWebViewClient(new Callback()); //creating object for an inner class
        web.loadUrl("https://www.terveyskirjasto.fi/terveyskirjasto/tk.koti?p_artikkeli=dlk01123"); //call the url using webview object

    }

    /**
     * Extends for webclient
     */

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        } //to not overwrite, webview should be able to load a page
    }
}