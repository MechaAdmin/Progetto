package com.example.riccardo.hermes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Profilo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListAdapter adp;
    ListAdapterVenduto adpVenduti;
    ArrayList<Prodotto> listInVendita;
    ArrayList<ProdottoVenduto> listVenduti;
    Boolean toolbar = true;
    String mail;
    String jsonCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setto il layout
        setContentView(R.layout.activity_profilo);

        //Recupero i vari oggetti del layout
        TextView txtNome = (TextView)findViewById(R.id.txtProfiloNome);
        TextView txtMail = (TextView)findViewById(R.id.txtProfiloMail);
        ImageView imgProfilo = (ImageView)findViewById(R.id.imgProfiloImmagine);
        ListView listViewInVendita = (ListView)findViewById(R.id.listProdottiInVendita);
        ListView listViewVenduti = (ListView)findViewById(R.id.listProdottiVenduti);


        //Creo un nuovo adapter che si poggia sulla lista listInVendita
        //e aggiungo l'adapter alla listView
        listInVendita = new ArrayList<Prodotto>();
        listVenduti = new ArrayList<ProdottoVenduto>();
        adpVenduti = new ListAdapterVenduto(this,listVenduti,Profilo.this);
        adp = new ListAdapter(this,listInVendita,Profilo.this);
        listViewInVendita.setAdapter(adp);
        listViewVenduti.setAdapter(adpVenduti);

        //recupero il json passato da Principale
        Intent intent = getIntent();
        jsonCliente = intent.getStringExtra("json");

        //se non sono il propreitario del profilo setto la variabile a false
        if(intent.getStringExtra("toolbar") != null){
            toolbar = false;
        }

        try{
            //utilizzo il json per recuperare nome, cognome, mail ed immagine dell'utente
            //e utilizo questi dati per riempire i vari campi dell'activity
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

        //Recupero la TabHost dal layout
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Creo un oggetto della classe TabHost.TabSpec che viene utilizzato
        //per creare i due tab, indicando con setcontent() la View da utilizzare come content del tab
        //e con setIndicator() la label titolo della tab.
        //Infine con addTab() aggiungo le TabSpec al TabHost
        TabHost.TabSpec spec = host.newTabSpec("Prodotti in Vendita");
        spec.setContent(R.id.tabOggettiVendita);
        spec.setIndicator("Prodotti in Vendita");
        host.addTab(spec);
        spec = host.newTabSpec("Prodotti Venduti");
        spec.setContent(R.id.tabOggettiVenduti);
        spec.setIndicator("Prodotti Venduti");
        host.addTab(spec);

        getJson();
        getJsonVenduti();
        //creo un listener per la lista
        listViewInVendita.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {
                if(!toolbar){
                    //se NON SONO il proprietario del profilo lancio dettaglio_prodotto
                    Prodotto p  =(Prodotto)parent.getAdapter().getItem(position);
                    Intent intent = new Intent(Profilo.this, dettaglio_prodotto.class);
                    intent.putExtra("prodotto",p);
                    startActivity(intent);
                }else{
                    //se SONO il proprietario del profilo lancio modifica_prodotto
                    Prodotto p  =(Prodotto)parent.getAdapter().getItem(position);
                    Intent intent = new Intent(Profilo.this, modifica_prodotto.class);
                    intent.putExtra("prodotto",p);
                    startActivity(intent);
                }

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //se sono il proprietario del profilo aggiungo il menù toolbar_modifica_profilo
        if(toolbar){
            getMenuInflater().inflate(R.menu.toolbar_modifica_profilo, menu);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //se clicco sull'item menuModifica del menù lancio ModificaCliente
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

    private void getJson() {
        class GetURLs extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    //creo l'oggetto jsonObject a partire da s e l'array stringJson
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray stringJson = jsonObject.getJSONArray("result");

                    //per ogni elemento dell'array
                    for(int i=0;i< stringJson.length();i++){
                        //ricavo i dati, creo un nuovo prodotto a partire da essi
                        //e aggiungo il prodotto all'adapter della listView
                        String prezzo = stringJson.getJSONObject(i).getString("prezzo");
                        String nome = stringJson.getJSONObject(i).getString("nomeProdotto");
                        String id = stringJson.getJSONObject(i).getString("id");
                        String descrizione = stringJson.getJSONObject(i).getString("descrizione");
                        String urlImmagine = stringJson.getJSONObject(i).getString("immagine");
                        String venditore = stringJson.getJSONObject(i).getString("venditore");
                        String categoria = stringJson.getJSONObject(i).getString("categoria");
                        Prodotto p = new Prodotto(nome,prezzo,descrizione,id,urlImmagine,venditore,categoria);
                        adp.add(p);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    //creo la connessione passando come paramentro l'URL della pagina php
                    //che è stato passato alla classe dalla chiamata execute e viene ricavato con strings[0]
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //ricavo la risposta della pagina php e la appendo man mano che leggo le righe
                    //all'oggetto StringBuilder sb, infine torno la stringa ricevuta
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
        //Creo un oggetto della classe appena creata e lancio il metodo execute su esso
        GetURLs gu = new GetURLs();
        gu.execute("http://MechaVendor.16mb.com/jsonProdottiInVendita.php?mail="+mail);
    }
    private void getJsonVenduti() {
        class GetURLs extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    //creo l'oggetto jsonObject a partire da s e l'array stringJson
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray stringJson = jsonObject.getJSONArray("result");

                    //per ogni elemento dell'array
                    for(int i=0;i< stringJson.length();i++){
                        //ricavo i dati, creo un nuovo prodotto a partire da essi
                        //e aggiungo il prodotto all'adapter della listView
                        String prezzo = stringJson.getJSONObject(i).getString("prezzo");
                        String nome = stringJson.getJSONObject(i).getString("nomeProdotto");
                        String id = stringJson.getJSONObject(i).getString("id");
                        String descrizione = stringJson.getJSONObject(i).getString("descrizione");
                        String urlImmagine = stringJson.getJSONObject(i).getString("immagine");
                        String venditore = stringJson.getJSONObject(i).getString("venditore");
                        String categoria = stringJson.getJSONObject(i).getString("categoria");
                        String acquirente = stringJson.getJSONObject(i).getString("acquirente");
                        String data = stringJson.getJSONObject(i).getString("dataAcquisto");
                        ProdottoVenduto p = new ProdottoVenduto(nome,prezzo,descrizione,id,urlImmagine,venditore,categoria,acquirente,data);
                        adpVenduti.add(p);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    //creo la connessione passando come paramentro l'URL della pagina php
                    //che è stato passato alla classe dalla chiamata execute e viene ricavato con strings[0]
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //ricavo la risposta della pagina php e la appendo man mano che leggo le righe
                    //all'oggetto StringBuilder sb, infine torno la stringa ricevuta
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
        //Creo un oggetto della classe appena creata e lancio il metodo execute su esso
        GetURLs gu = new GetURLs();
        gu.execute("http://MechaVendor.16mb.com/jsonProdottiVenduti.php?mail="+mail);
    }

}
