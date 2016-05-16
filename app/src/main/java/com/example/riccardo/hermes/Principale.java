package com.example.riccardo.hermes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Principale extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView profiloImg;
    private final String PRINCIPALE_URL = "http://mechavendor.16mb.com/getCliente.php?username=";
    static String username;
    String JSONcliente;
    static String mailCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setto il layout
        setContentView(R.layout.activity_principale);

        // recupero l'username passato da Login
        Intent intent = getIntent();
        if(intent.getStringExtra(Login.USER_NAME) != null){
            username = intent.getStringExtra(Login.USER_NAME);
        }

        // recupero la toolbar e la uso come una ActionBar utilizzando il metodo setSupportActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fatto da Android
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //recupero la mia navigationView che servirà da menù e setto l'activity come listner
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //recupero la View alla posizione 0 della mia NavigationView
        //e la uso per settare l'username e l'immagine dell'utente
        //nelle view predisposte
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.txtUsernameNavBar);
        nav_user.setText(username);
        profiloImg = (ImageView) hView.findViewById(R.id.profiloImg);


        OttieniJson(PRINCIPALE_URL + username);

        profiloImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //al momento del click sull'immagine del profilo
                //faccio partire l'activity Profilo passandogli il JSON del cliente
                Intent profiloIntent = new Intent(Principale.this, Profilo.class);
                profiloIntent.putExtra("json",JSONcliente);
                startActivity(profiloIntent);
            }
        });

        //faccio partire il fragment esplora
        FragmentManager fragmentManager = getFragmentManager();
        Fragment esplora = new Esplora();
        fragmentManager.beginTransaction().replace(R.id.container, esplora).commit();
    }

    @Override
    public void onBackPressed() {
        // recupero il drawer e se è aperto lo chiudo
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        //gestisco il click nel menù recuperando l'id dell'item cliccato
        int id = item.getItemId();

        // recupero il fragment manager
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_esplora) {
            //avvio Esplora
            Fragment esplora = new Esplora();
            fragmentManager.beginTransaction().replace(R.id.container, esplora).commit();
        } else if (id == R.id.nav_vendi) {
            //Avvio vendita passandogli la mail dell'utente come argomento
            Bundle b = new Bundle();
            b.putString("mail", mailCliente);
            Fragment vendita = new Vendita();
            vendita.setArguments(b);
            fragmentManager.beginTransaction().replace(R.id.container, vendita).commit();
        } else if (id == R.id.nav_oggettiAcquistati) {
            Fragment prodottiAcquistati = new ProdottiAcquistati();
            fragmentManager.beginTransaction().replace(R.id.container, prodottiAcquistati).commit();

        } else if (id == R.id.nav_oggettiVenduti) {

        } else if (id == R.id.nav_impostazioni) {

        }else if (id == R.id.nav_preferiti) {
            //Avvio Preferiti
            Fragment preferiti = new Preferiti();
            fragmentManager.beginTransaction().replace(R.id.container, preferiti).commit();
        } else if (id == R.id.nav_carrello){
            //Avvio Carrello
            Fragment carrello = new Carrello();
            fragmentManager.beginTransaction().replace(R.id.container, carrello).commit();
        } else if (id == R.id.nav_logout){
            //Pulisco le SharedPreferences e torno a Login
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Principale.this);
            prefs.edit().remove("username").commit();
            Intent intent = new Intent(Principale.this,Login.class);
            startActivity(intent);
            finish();
        }

        //Chiudo il drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OttieniJson(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                //recupero l'URL e l'username da utilizzare per la chiamata php
                String uri = params[0];

                try {
                    //Apro una connessione
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //Creo un oggetto StringBuilder per recupera la risposta dalla pagina php
                    StringBuilder sb = new StringBuilder();

                    //attraverso l'oggetto BufferedReader ricevo l'intera risposta dalla pagina php
                    // utilizzando il metodo getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    //fino a che il bufferedReader non è finito
                    while ((json = bufferedReader.readLine()) != null) {
                        //appendo a sb ogni riga letta con il metodo readline() dal bufferedReader
                        sb.append(json + "\n");
                    }
                    // infine torno sb
                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONcliente=s;
                try {
                    //ricordando che s è il nostro sb
                    // cioè la risposta della pagina php
                    // creo un oggetto JSON a partire da essa
                    JSONObject jsonObj = new JSONObject(s);

                    //recupero l'Array result che contiene le informazioni sul cliente
                    JSONArray informazioni = jsonObj.getJSONArray("result");

                    //recupero l'elemento del'array che contiene la mail e attraverso il metodo
                    //getString() recuper la mail del cliente
                    JSONObject c = informazioni.getJSONObject(0);
                    mailCliente = c.getString("mail");

                    //abbiamo utilizzato la libreria picasso per inserire la foto nella imageView
                    Picasso.with(Principale.this).invalidate(c.getString("immagine"));
                    Picasso.with(Principale.this).load(c.getString("immagine")).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(profiloImg);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        // Creo un oggetto della classe appena creata e lo faccio partire con execute
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }

}