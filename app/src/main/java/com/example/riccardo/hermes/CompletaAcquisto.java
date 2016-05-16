package com.example.riccardo.hermes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CompletaAcquisto extends AppCompatActivity {
    float tot;
    @Override
    @SuppressLint("JavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        tot  = intent.getFloatExtra("totale",0);
        setContentView(R.layout.activity_completa_acquisto);
        WebView browser;
        browser=(WebView)findViewById(R.id.webViewPagamento);
        //Enable Javascript
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebViewClient(new WebViewClient());
        browser.getSettings().setDomStorageEnabled(true);
        //Inject WebAppInterface methods into Web page by having Interface name 'Android'
        browser.addJavascriptInterface(new WebAppInterface(this), "Android");
        //Load URL inside WebView
        browser.loadUrl("http://mechavendor.16mb.com/completaAcquisto.html");
    }

    public class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }
        public void getData(String data) {
            if(data.equals("ok")){
                //fai query
            }else{

            }
        }
        @JavascriptInterface
        public float getTotale(){
            return tot;
        }
    }
}
