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
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class dettaglio_prodotto extends AppCompatActivity {
    private static final String JSON_URL = "http://mechavendor.16mb.com/dettaglioProdotto.php?id=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_prodotto);
        Intent intent = getIntent();
        TextView nomeProdotto = (TextView)findViewById(R.id.txtDettaglioNome);
        TextView prezzo = (TextView)findViewById(R.id.txtDettaglioPrezzo);
        TextView descrizione = (TextView)findViewById(R.id.txtDettaglioDescrizione);
        Button contattaVenditore = (Button)findViewById(R.id.btnDettaglioContattaVenditore);
        ImageView imgProdotto = (ImageView)findViewById(R.id.imgDettaglioImmagine);
        nomeProdotto.setText(intent.getStringExtra("nome"));
        prezzo.setText(intent.getStringExtra("prezzo"));
        descrizione.setText(intent.getStringExtra("descrizione"));
        Picasso.with(dettaglio_prodotto.this).load(intent.getStringExtra("urlImmagine")).into(imgProdotto);
        contattaVenditore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, "ciao@gmail.com");
                startActivity(intent);
            }
        });
    }

}
