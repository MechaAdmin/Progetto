package com.example.riccardo.hermes;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by riccardo on 09/05/16.
 */
public class ProdottiAcquistati extends Fragment {
    View fragmentView;
    ListView listview;
    ListAdapterVenduto adp;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView =  inflater.inflate(R.layout.fragment_acquistati,null);
        listview = (ListView) fragmentView.findViewById(R.id.listProdottiAcquistati);
        ArrayList<ProdottoVenduto> listAcquistati = new ArrayList<ProdottoVenduto>();
        adp = new ListAdapterVenduto(getActivity(),listAcquistati,getActivity());
        listview.setAdapter(adp);
        getJsonAcquistati();
        return  fragmentView;
    }
    private void getJsonAcquistati() {
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
        gu.execute("http://MechaVendor.16mb.com/jsonProdottiAcquistati.php?mail="+Principale.mailCliente);
    }
}
