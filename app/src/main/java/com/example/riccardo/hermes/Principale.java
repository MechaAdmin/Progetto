package com.example.riccardo.hermes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private final String infoCliente = "http://mechavendor.16mb.com/getCliente.php?username=";
    String username;
    String JSONcliente;
    String mailCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);
        Intent intent = getIntent();
        username = intent.getStringExtra(Login.USER_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.txtUsernameNavBar);
        nav_user.setText(username);

        profiloImg = (ImageView) hView.findViewById(R.id.profiloImg);
        OttieniJson(infoCliente + username);
        final Intent profiloIntent = new Intent(this, Profilo.class);
        profiloImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloIntent.putExtra("json",JSONcliente);
                startActivity(profiloIntent);
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        Fragment esplora = new Esplora();
        fragmentManager.beginTransaction().replace(R.id.container, esplora).commit();
    }

    @Override
    public void onBackPressed() {
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
        Bundle b = new Bundle();
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_esplora) {
            Fragment esplora = new Esplora();
            fragmentManager.beginTransaction().replace(R.id.container, esplora).commit();
        } else if (id == R.id.nav_vendi) {
            b.putString("mail", mailCliente);
            Fragment vendita = new Vendita();
            vendita.setArguments(b);
            fragmentManager.beginTransaction().replace(R.id.container, vendita).commit();
        } else if (id == R.id.nav_oggettiAcquistati) {

        } else if (id == R.id.nav_oggettiVenduti) {

        } else if (id == R.id.nav_impostazioni) {

        }else if (id == R.id.nav_preferiti) {
            Fragment preferiti = new Preferiti();
            fragmentManager.beginTransaction().replace(R.id.container, preferiti).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void OttieniJson(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
                JSONcliente=s;
                try {

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray informazioni = jsonObj.getJSONArray("result");
                    JSONObject c = informazioni.getJSONObject(0);
                    mailCliente = c.getString("mail");
                    Picasso.with(Principale.this).invalidate(c.getString("immagine"));
                    Picasso.with(Principale.this).load(c.getString("immagine")).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(profiloImg);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }

   public void onRestart() {
       super.onRestart();
       OttieniJson(infoCliente + username);
   }
}