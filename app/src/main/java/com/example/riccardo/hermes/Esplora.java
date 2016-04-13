package com.example.riccardo.hermes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
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
public class Esplora extends Fragment implements AdapterView.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener{
    ListView lista;
    GetProdotti getProdotti;
    private CustomList customList;
    View fragmentView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_esplora,null);
        lista = (ListView) fragmentView.findViewById(R.id.listProdotti);
        lista.setOnItemClickListener(this);
        getJson();
        return  fragmentView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.search_toolbar, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.searchProdotti);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getProdotti.Filtra(newText);
                customList = new CustomList(getActivity(),GetProdotti.idFiltrato,GetProdotti.nomeProdottoFiltrato,GetProdotti.prezzoFiltrato,GetProdotti.bitmapsFiltrato);
                customList.notifyDataSetChanged();
                lista.setAdapter(customList);
                return false;
            }
        });
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }
    private void getDati(){
        class GetImages extends AsyncTask<Void,Void,Void>{
            ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(getActivity(),"Download Prodotti","Attendi...",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                customList = new CustomList(getActivity(),GetProdotti.id,GetProdotti.nomeProdotto,GetProdotti.prezzo,GetProdotti.bitmaps);
                lista.setAdapter(customList);
                dialog.dismiss();
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

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
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
