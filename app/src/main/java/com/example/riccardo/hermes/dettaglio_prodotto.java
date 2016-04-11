package com.example.riccardo.hermes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class dettaglio_prodotto extends AppCompatActivity {
    TextView nomeProdotto;
    TextView prezzo;
    TextView descrizione;
    Button contattaVenditore;
    ImageView imgProdotto;
    JSONArray informazioni;
    String idProdotto;
    private static final String JSON_URL = "http://mechavendor.16mb.com/dettaglioProdotto.php?id=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_prodotto);
        Intent intent = getIntent();
        idProdotto = intent.getStringExtra("id");
        nomeProdotto = (TextView)findViewById(R.id.txtDettaglioNome);
        prezzo = (TextView)findViewById(R.id.txtDettaglioPrezzo);
        descrizione = (TextView)findViewById(R.id.txtDettaglioDescrizione);
        contattaVenditore = (Button)findViewById(R.id.btnDettaglioContattaVenditore);
        imgProdotto = (ImageView)findViewById(R.id.imgDettaglioImmagine);
        OttieniJson(JSON_URL + idProdotto);
    }
    private void OttieniJson(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(dettaglio_prodotto.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                compilaCampi(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
    public void compilaCampi(String s) {
        try {
            JSONObject jsonObj = new JSONObject(s);
            informazioni = jsonObj.getJSONArray("result");
            JSONObject c = informazioni.getJSONObject(0);
            nomeProdotto.setText(c.getString("nomeProdotto"));
            prezzo.setText(c.getString("prezzo")+"€");
            descrizione.setText(c.getString("descrizione"));
            new DownLoadImageTask(imgProdotto).execute(c.getString("immagine"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
