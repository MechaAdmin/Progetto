package com.example.riccardo.hermes;

import android.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by riccardo on 22/04/16.
 */
public class Preferiti extends Fragment {
    View fragmentView;
    dbPreferiti db;
    ListView listview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView =  inflater.inflate(R.layout.fragment_preferiti,null);
        db = new dbPreferiti(getActivity());
        //ArrayList<Prodotto> list = db.getAllProdotto();
        //ListAdapter adp = new ListAdapter(getActivity(),list,getActivity());
        listview = (ListView) fragmentView.findViewById(R.id.listPreferiti);
        //listview.setAdapter(adp);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Prodotto p  =(Prodotto)arg0.getAdapter().getItem(pos);
                db.deleteProdotto(p);
                ArrayList<Prodotto> list = db.getAllProdotto();
                ListAdapter adp = new ListAdapter(getActivity(),list,getActivity());
                listview.setAdapter(adp);
                return true;
            }
        });
        return  fragmentView;
    }
    private void getJson(int i,int f,String condizione) {
        class GetURLs extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray stringJson = jsonObject.getJSONArray("result");
                    HashSet<String> hs = new HashSet<String>();
                    ArrayList<Prodotto> list = db.getAllProdotto();
                    for(int i=0;i<stringJson.length();i++){
                        String id = stringJson.getJSONObject(i).getString("id");
                        hs.add(id);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
        GetURLs gu = new GetURLs();
        Integer x = Integer.MAX_VALUE;
        gu.execute("http://MechaVendor.16mb.com/jsonProdottiList.php?condizione=&inizio=0?&fine="+x);
    }
}
