package com.example.riccardo.hermes;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by riccardo on 22/04/16.
 */
public class Preferiti extends Fragment {
    View fragmentView;
    ListView listview;
    ListAdapter adp;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView =  inflater.inflate(R.layout.fragment_preferiti,null);
        listview = (ListView) fragmentView.findViewById(R.id.listPreferiti);
        getPreferiti(Principale.username);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Prodotto p  =(Prodotto)arg0.getAdapter().getItem(pos);
                EliminaPreferito(p.getId(),Principale.username,p);
                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {

                Prodotto p  =(Prodotto)parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), dettaglio_prodotto.class);
                intent.putExtra("prodotto",p);
                startActivity(intent);
            }
        });
        return  fragmentView;
    }
    public void getPreferiti(String username){
        class Pref extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RequestHandler ruc = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try{
                    ArrayList<Prodotto> list = new ArrayList<Prodotto>();
                    adp = new ListAdapter(getActivity(),list,getActivity());
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray stringJson = jsonObject.getJSONArray("result");
                    for(int i=0;i<stringJson.length();i++){
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
                    listview.setAdapter(adp);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                String result = ruc.sendPostRequest("http://mechavendor.16mb.com/jsonPreferiti.php",data);

                return  result;
            }
        }

        Pref ru = new Pref();
        ru.execute(username);
    }
    public void EliminaPreferito(String id,String username,final Prodotto p){
        class Pref extends AsyncTask<String, Void, String>{
            RequestHandler ruc = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                if(s.contentEquals("Prodotto rimosso dai Preferiti")){
                    adp.remove(p);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("id",params[1]);
                String result = ruc.sendPostRequest("http://mechavendor.16mb.com/eliminaPreferito.php",data);

                return  result;
            }
        }

        Pref ru = new Pref();
        ru.execute(username,id);
    }
}
