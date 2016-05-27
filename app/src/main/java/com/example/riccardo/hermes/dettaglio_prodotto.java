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

public class dettaglio_prodotto extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_prodotto);

        TextView nomeProdotto = (TextView)findViewById(R.id.txtDettaglioNome);
        TextView prezzo = (TextView)findViewById(R.id.txtDettaglioPrezzo);
        TextView descrizione = (TextView)findViewById(R.id.txtDettaglioDescrizione);
        ImageButton contattaVenditore = (ImageButton)findViewById(R.id.btnDettaglioContattaVenditore);
        ImageButton profiloVenditore = (ImageButton)findViewById(R.id.btnDettaglioProfiloVenditore);
        ImageButton aggiungiPreferiti = (ImageButton)findViewById(R.id.btnAggiungiPreferiti);
        ImageButton aggiungiCarrello = (ImageButton)findViewById(R.id.btnCarrello);
        ImageView imgProdotto = (ImageView)findViewById(R.id.imgDettaglioImmagine);

        Bundle data = getIntent().getExtras();
        final Prodotto p = data.getParcelable("prodotto");
        nomeProdotto.setText(p.getNome());
        prezzo.setText(p.getPrezzo() + "€");
        descrizione.setText(p.getDescrizione());
        Picasso.with(dettaglio_prodotto.this).load(p.getUrlImmagine()).into(imgProdotto);
        contattaVenditore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{p.getVenditore()});
                startActivity(intent);
            }
        });
        aggiungiPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserimentoPreferiti(Principale.username,p.getId());
            }
        });
        profiloVenditore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonProfiloVenditore(p.getVenditore());
            }
        });
        aggiungiCarrello.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                boolean trovato = false;
                for(int i = 0; i<SingletonCarrello.getInstance().getCarrello().size();i++){
                    String x = SingletonCarrello.getInstance().getCarrello().get(i).getId();
                    String y = p.getId();
                    if (SingletonCarrello.getInstance().getCarrello().get(i).getId().equals(p.getId())){
                        trovato = true;
                    }
                }
                if(!trovato){
                    SingletonCarrello.getInstance().getCarrello().add(p);
                    Toast.makeText(dettaglio_prodotto.this, "Prodotto aggiunto al carrello", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(dettaglio_prodotto.this, "Prodotto già presente nel carrello", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void inserimentoPreferiti(String username,String idProdotto){
        class aggiungiPreferiti extends AsyncTask<String, Void, String>{
            RequestHandler ruc = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("id",params[1]);
                String result = ruc.sendPostRequest("http://mechavendor.16mb.com/inserimentoPreferiti.php",data);
                return  result;
            }
        }

        aggiungiPreferiti ru = new aggiungiPreferiti();
        ru.execute(username,idProdotto);
    }
    public void jsonProfiloVenditore(String mail){
        class profiloVenditore extends AsyncTask<String, Void, String>{
            RequestHandler ruc = new RequestHandler();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Intent intent = new Intent(dettaglio_prodotto.this,Profilo.class);
                intent.putExtra("json",s);
                intent.putExtra("toolbar","disattiva");
                startActivity(intent);
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("mail",params[0]);
                String result = ruc.sendPostRequest("http://mechavendor.16mb.com/getProfiloVenditore.php",data);
                return  result;
            }
        }
        profiloVenditore ru = new profiloVenditore();
        ru.execute(mail);
    }

}
