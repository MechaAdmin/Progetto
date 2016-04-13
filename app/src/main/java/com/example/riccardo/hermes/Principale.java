package com.example.riccardo.hermes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.InputStream;

public class Principale extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView profiloImg;
    private final String imageURL = "http://mechavendor.16mb.com/getImage.php?userName=";
    String username;
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
        TextView nav_user = (TextView)hView.findViewById(R.id.txtUsernameNavBar);
        nav_user.setText(username);

        profiloImg = (ImageView) hView.findViewById(R.id.profiloImg);
        getImage(username);
        final Intent profiloIntent = new Intent(this,Profilo.class);
        profiloImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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




    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Bundle b = new Bundle();
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_esplora) {
            Fragment esplora = new Esplora();
            fragmentManager.beginTransaction().replace(R.id.container, esplora).commit();
        } else if (id == R.id.nav_vendi) {
            b.putString("username",username);
            Fragment vendita = new Vendita();
            vendita.setArguments(b);
            fragmentManager.beginTransaction().replace(R.id.container,vendita).commit();
        } else if (id == R.id.nav_oggettiAcquistati) {

        } else if (id == R.id.nav_oggettiVenduti) {

        } else if (id == R.id.nav_impostazioni) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getImage(String userName) {

        class GetImage extends AsyncTask<String,Void,Bitmap> {


            ImageView bmImage;

            public GetImage(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                bmImage.setImageBitmap(bitmap);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                String url = imageURL+ strings[0];
                Bitmap mIcon = null;
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());

                }
                return mIcon;
            }
        }

        GetImage gi = new GetImage(profiloImg);
        gi.execute(userName);
    }
}
