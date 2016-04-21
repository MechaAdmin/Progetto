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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Profilo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListAdapter adp;
    ArrayList<Prodotto> listInVendita;
    ArrayList<Prodotto> listVenduti;
    int inizioRigaQuery = 0;
    int numRigheQuery = 3;
    Boolean flag_loading = false;
    String mail;
    String jsonCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
        TextView txtNome = (TextView)findViewById(R.id.txtProfiloNome);
        TextView txtMail = (TextView)findViewById(R.id.txtProfiloMail);
        ImageView imgProfilo = (ImageView)findViewById(R.id.imgProfiloImmagine);
        ListView listViewInVendita = (ListView)findViewById(R.id.listProdottiInVendita);
        listInVendita = new ArrayList<Prodotto>();
        adp = new ListAdapter(this,listInVendita,Profilo.this);
        listViewInVendita.setAdapter(adp);

        Intent intent = getIntent();
        jsonCliente = intent.getStringExtra("json");
        try{
            JSONObject jsonObj = new JSONObject(jsonCliente);
            JSONArray informazioni = jsonObj.getJSONArray("result");
            JSONObject c = informazioni.getJSONObject(0);
            txtNome.setText(c.getString("nome") + " " + c.getString("cognome"));
            txtMail.setText(c.getString("mail"));
            mail = c.getString("mail");
            Picasso.with(Profilo.this).load(c.getString("immagine")).memoryPolicy(MemoryPolicy.NO_CACHE).into(imgProfilo);
        }catch (JSONException e){
            e.printStackTrace();
        }
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        TabHost.TabSpec spec = host.newTabSpec("Prodotti in Vendita");
        spec.setContent(R.id.tabOggettiVendita);
        spec.setIndicator("Prodotti in Vendita");
        host.addTab(spec);
        spec = host.newTabSpec("Prodotti Venduti");
        spec.setContent(R.id.tabOggettiVenduti);
        spec.setIndicator("Prodotti Venduti");
        host.addTab(spec);
        getJson(inizioRigaQuery,numRigheQuery);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_modifica_profilo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuModifica:
                Intent intent = new Intent(Profilo.this,ModificaCliente.class);
                intent.putExtra("json",jsonCliente);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    private void getJson(int i,int f) {
        class GetURLs extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray stringJson = jsonObject.getJSONArray("result");
                    for(int i=0;i< stringJson.length();i++){
                        String prezzo = stringJson.getJSONObject(i).getString("prezzo") + "€";
                        String nome = stringJson.getJSONObject(i).getString("nomeProdotto");
                        String id = stringJson.getJSONObject(i).getString("id");
                        String descrizione = stringJson.getJSONObject(i).getString("descrizione");
                        String urlImmagine = stringJson.getJSONObject(i).getString("immagine");

                        Prodotto p = new Prodotto(nome,prezzo,descrizione,id,urlImmagine,"");
                        adp.add(p);
                    }
                    flag_loading = false;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();


                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute("http://MechaVendor.16mb.com/jsonProdottiInVendita.php?mail="+mail);
        inizioRigaQuery = inizioRigaQuery+numRigheQuery ;
    }

}
