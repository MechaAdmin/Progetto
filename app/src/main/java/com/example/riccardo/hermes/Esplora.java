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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by riccardo on 08/04/16.
 */
public class Esplora extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    ListAdapter adp;
    ArrayList<Prodotto> listData;
    ListView listView;
    int inizioRigaQuery = 0;
    int numRigheQuery = 3;
    Spinner s;
    String ricerca = "";
    String categoria = "All";
    Boolean flag_loading = false;
    Boolean first = true;
    View fragmentView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView =  inflater.inflate(R.layout.fragment_esplora,null);
        listData = new ArrayList<Prodotto>();
        adp = new ListAdapter(getActivity(),listData,getActivity());
        listView = (ListView) fragmentView.findViewById(R.id.listProdotti);
        listView.setAdapter(adp);
        getJson(inizioRigaQuery,numRigheQuery,"");
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(flag_loading == false)
                    {
                        flag_loading = true;
                        getJson(inizioRigaQuery,numRigheQuery,ricerca);
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        MenuItem spinner = menu.findItem( R.id.SpinnerCerca);
        s = (Spinner)spinner.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerCerca, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adp.clear();
                inizioRigaQuery = 0;
                categoria = s.getSelectedItem().toString();
                ricerca = newText;
                getJson(inizioRigaQuery, numRigheQuery, newText);
                return true;
            }
        });
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(first){
                    first = false;
                }else{
                    adp.clear();
                    inizioRigaQuery = 0;
                    categoria = parentView.getItemAtPosition(position).toString();
                    getJson(inizioRigaQuery, numRigheQuery, ricerca);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
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

                    flag_loading = false;
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
        gu.execute("http://MechaVendor.16mb.com/jsonProdottiList.php?condizione="+condizione +"&inizio="+i+"&fine="+f+"&categoria="+categoria);
        inizioRigaQuery = inizioRigaQuery + numRigheQuery;
    }
}
