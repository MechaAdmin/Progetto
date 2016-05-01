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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class dettaglio_prodotto extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_prodotto);

        TextView nomeProdotto = (TextView)findViewById(R.id.txtDettaglioNome);
        TextView prezzo = (TextView)findViewById(R.id.txtDettaglioPrezzo);
        TextView descrizione = (TextView)findViewById(R.id.txtDettaglioDescrizione);
        Button contattaVenditore = (Button)findViewById(R.id.btnDettaglioContattaVenditore);
        Button aggiungiPreferiti = (Button)findViewById(R.id.btnPreferiti);
        Button aggiungiCarrello = (Button)findViewById(R.id.btnCarrello);
        ImageView imgProdotto = (ImageView)findViewById(R.id.imgDettaglioImmagine);

        Bundle data = getIntent().getExtras();
        final Prodotto p = data.getParcelable("prodotto");
        nomeProdotto.setText(p.getNome());
        prezzo.setText(p.getPrezzo());
        descrizione.setText(p.getDescrizione());
        final String mail = p.getVenditore();
        Picasso.with(dettaglio_prodotto.this).load(p.getUrlImmagine()).into(imgProdotto);



        contattaVenditore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
                startActivity(intent);
            }
        });
        aggiungiCarrello.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                SingletonCarrello.getInstance().getCarrello().add(p);
            }
        });

    }

}
