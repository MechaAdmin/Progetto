package com.example.riccardo.hermes;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by riccardo on 08/04/16.
 */
public class Esplora2 extends Fragment implements AdapterView.OnItemClickListener{
    ListView lista;
    GetProdotti getProdotti;
    EditText txtCerca;
    private CustomList customList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_esplora,null);
        lista = (ListView) fragmentView.findViewById(R.id.listProdotti);
        txtCerca = (EditText) fragmentView.findViewById(R.id.txtListCerca);
        lista.setOnItemClickListener(this);
        lista.setTextFilterEnabled(true);
        getJson();

        txtCerca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                getProdotti.Filtra(s.toString());
                customList = new CustomList(getActivity(),GetProdotti.idFiltrato,GetProdotti.nomeProdottoFiltrato,GetProdotti.prezzoFiltrato,GetProdotti.bitmapsFiltrato);
                customList.notifyDataSetChanged();
                lista.setAdapter(customList);
            }
        });
        return  fragmentView;
    }
    private void getDati(){
        class GetImages extends AsyncTask<Void,Void,Void>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Downloading images...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();
                customList = new CustomList(getActivity(),GetProdotti.id,GetProdotti.nomeProdotto,GetProdotti.prezzo,GetProdotti.bitmaps);
                lista.setAdapter(customList);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getProdotti.getDati();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }
    private void getJson() {
        class GetURLs extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getProdotti = new GetProdotti(s);
                getDati();
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
        gu.execute("http://MechaVendor.16mb.com/jsonProdotti2.php");
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        TextView c = (TextView) view.findViewById(R.id.txtListId);
        String idProdotto = c.getText().toString();
        Intent intent = new Intent(getActivity(), dettaglio_prodotto.class);
        intent.putExtra("id", idProdotto);
        startActivity(intent);

    }
}
