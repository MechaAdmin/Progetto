package com.example.riccardo.hermes;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class dettaglio_acquistato extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_acquistati);

        TextView nomeProdotto = (TextView) findViewById(R.id.txtDettaglioNomeAcquistato);
        TextView prezzo = (TextView) findViewById(R.id.txtDettaglioPrezzoAcquistato);
        TextView descrizione = (TextView) findViewById(R.id.txtDettaglioDescrizioneAcquistato);
        TextView dataAcquisto = (TextView) findViewById(R.id.txtDettaglioDataAcquistato);
        ImageView imgProdotto = (ImageView) findViewById(R.id.imgDettaglioImmagineAcquistato);

        Bundle data = getIntent().getExtras();
        final ProdottoVenduto p = data.getParcelable("prodottoAcquistato");
        nomeProdotto.setText(p.getNome());
        prezzo.setText(p.getPrezzo() + "€");
        descrizione.setText(p.getDescrizione());
        dataAcquisto.setText(p.getDataVendita());

        Picasso.with(this).load(p.getUrlImmagine()).fit().centerCrop().into(imgProdotto);

    }
}
