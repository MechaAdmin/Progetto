package com.example.riccardo.hermes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

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
        @JavascriptInterface
        public void getData(String data) {
            if(data.equals("ok")){
                ArrayList<Prodotto> carrello = SingletonCarrello.getInstance().getCarrello();
                for(int i = 0;i<carrello.size();i++){
                    inserisciProdottoVenduto(carrello.get(i).getId(),Principale.mailCliente);
                }
            }else{
                Toast.makeText(getApplicationContext(),"Errore",Toast.LENGTH_LONG);
            }
        }
        @JavascriptInterface
        public float getTotale(){
            return tot;
        }
    }

    private void inserisciProdottoVenduto(String id,String acquirente){
        class RegisterUser extends AsyncTask<String, Void, String> {
            RequestHandler ruc = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                SingletonCarrello.getInstance().getCarrello().clear();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("id",params[0]);
                data.put("acquirente",params[1]);
                String result = ruc.sendPostRequest("http://mechavendor.16mb.com/inserisciProdottoVenduto.php",data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(id,acquirente);



    }
}
