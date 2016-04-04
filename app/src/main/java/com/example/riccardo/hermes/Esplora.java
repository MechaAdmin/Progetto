package com.example.riccardo.hermes;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by riccardo on 04/04/16.
 */
public class Esplora extends Fragment {
    JSONArray prodotti;
    ArrayList<HashMap<String,String>> listaProdotti;
    ListView lista;
    private static final String JSON_URL = "http://mechavendor.16mb.com/jsonProdotti.php";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_esplora,null);
        lista = (ListView) fragmentView.findViewById(R.id.listProdotti);
        listaProdotti = new ArrayList<HashMap<String,String>>();
        OttieniJson(JSON_URL);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Toast.makeText(getActivity().getApplicationContext(),(((TextView)(view.findViewById(R.id.txtListId))).getText().toString()) ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),dettaglio_prodotto.class);
                String idprodotto = ((TextView)(view.findViewById(R.id.txtListId))).getText().toString();
                intent.putExtra("id",idprodotto);
                startActivity(intent);
            }

        });

        return  fragmentView;
    }
    private void OttieniJson(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait...",null,true,true);
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
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //stringJson = s;
                //Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                stampaLista(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
    public void stampaLista(String s){
        try {
            JSONObject jsonObj = new JSONObject(s);
            prodotti = jsonObj.getJSONArray("result");

            for(int i=0;i<prodotti.length();i++){
                JSONObject c = prodotti.getJSONObject(i);
                String id = c.getString("id");
                String nomeProdotto = c.getString("nomeProdotto");
                String prezzo = c.getString("prezzo")+ "€";

                HashMap<String,String> prod = new HashMap<String,String>();

                prod.put("id",id);
                prod.put("nomeProdotto",nomeProdotto);
                prod.put("prezzo",prezzo);

                listaProdotti.add(prod);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listaProdotti, R.layout.list_item,
                    new String[]{"id","nomeProdotto","prezzo"},
                    new int[]{R.id.txtListId, R.id.txtListNome, R.id.txtListPrezzo}
            );

            lista.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


   /* public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_esplora,null);


    }*/
}
