package com.example.riccardo.hermes;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
public  class Profilo{
//public class Profilo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    GetProdotti getProdotti;
//    private CustomList customList;
//    ListView listaProdottiInVendita;
//    String username;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profilo);
//        TextView txtNome = (TextView)findViewById(R.id.txtProfiloNome);
//        TextView txtMail = (TextView)findViewById(R.id.txtProfiloMail);
//        ImageView imgProfilo = (ImageView)findViewById(R.id.imgProfiloImmagine);
//        listaProdottiInVendita = (ListView)findViewById(R.id.listProdottiInVendita);
//        Intent intent = getIntent();
//        String jsonCliente = intent.getStringExtra("json");
//        try{
//            JSONObject jsonObj = new JSONObject(jsonCliente);
//            JSONArray informazioni = jsonObj.getJSONArray("result");
//            JSONObject c = informazioni.getJSONObject(0);
//            txtNome.setText(c.getString("nome") + " " + c.getString("cognome"));
//            txtMail.setText(c.getString("mail"));
//            username = c.getString("username");
//            new DownLoadImageTask(imgProfilo).execute(c.getString("immagine"));
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//        TabHost host = (TabHost)findViewById(R.id.tabHost);
//        host.setup();
//        TabHost.TabSpec spec = host.newTabSpec("Prodotti in Vendita");
//        spec.setContent(R.id.tabOggettiVendita);
//        spec.setIndicator("Prodotti in Vendita");
//        host.addTab(spec);
//        spec = host.newTabSpec("Prodotti Venduti");
//        spec.setContent(R.id.tabOggettiVenduti);
//        spec.setIndicator("Prodotti Venduti");
//        host.addTab(spec);
//        getJson();
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_modifica_profilo, menu);
//        return true;
//    }
//    public boolean onNavigationItemSelected(MenuItem item) {
//        return true;
//    }
//    private void getDati(){
//        class GetImages extends AsyncTask<Void,Void,Void>{
//            ProgressDialog dialog;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                dialog = ProgressDialog.show(Profilo.this,"Download Prodotti","Attendi...",false,false);
//            }
//
//            @Override
//            protected void onPostExecute(Void v) {
//                super.onPostExecute(v);
//                customList = new CustomList(Profilo.this,GetProdotti.id,GetProdotti.nomeProdotto,GetProdotti.prezzo,GetProdotti.bitmaps);
//                listaProdottiInVendita.setAdapter(customList);
//                dialog.dismiss();
//            }
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    getProdotti.getDati();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }
//        GetImages getImages = new GetImages();
//        getImages.execute();
//    }
//    private void getJson() {
//        class GetURLs extends AsyncTask<String,Void,String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//            }
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                getProdotti = new GetProdotti(s);
//                getDati();
//            }
//
//            @Override
//            protected String doInBackground(String... strings) {
//                BufferedReader bufferedReader = null;
//                try {
//                    URL url = new URL(strings[0]);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//
//                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//                    String json;
//                    while((json = bufferedReader.readLine())!= null){
//                        sb.append(json+"\n");
//                    }
//
//                    return sb.toString().trim();
//
//                }catch(Exception e){
//                    return null;
//                }
//            }
//        }
//        GetURLs gu = new GetURLs();
//        gu.execute("http://MechaVendor.16mb.com/jsonProdottiInVendita.php?username="+username);
//    }
//    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView imageView;
//
//        public DownLoadImageTask(ImageView imageView) {
//            this.imageView = imageView;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urlOfImage = urls[0];
//            Bitmap logo = null;
//            try {
//                InputStream is = new URL(urlOfImage).openStream();
//                logo = BitmapFactory.decodeStream(is);
//            } catch (Exception e) { // Catch the download exception
//                e.printStackTrace();
//            }
//            return logo;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            imageView.setImageBitmap(result);
//        }
//    }
}
