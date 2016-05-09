package com.example.riccardo.hermes;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tomma on 27/04/2016.
 */
public class Carrello extends Fragment {
    ListAdapter adp;
    ArrayList<Prodotto> carrello;
    int sizeCarrelloBefore;
    float tot = 0;

    final String CARRELLO_URL = "http://MechaVendor.16mb.com/checkCarrello.php";
    ListView listView;
    View fragmentView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_carrello, null);
        adp = new ListAdapter(getActivity(),null,getActivity());

        //Riprendo il carrello
        carrello = SingletonCarrello.getInstance().getCarrello();
        sizeCarrelloBefore = carrello.size();

        for (int i = 0; i < sizeCarrelloBefore;i++){
            checkCarrello("" + i,carrello.get(i).getId());
        }
        if (sizeCarrelloBefore != carrello.size()){
            Toast.makeText(getActivity(),"Alcuni Prodotti sono stati eliminati perchè no sono più disponibili", Toast.LENGTH_LONG);
        }
        adp = new ListAdapter(getActivity(), carrello, getActivity());
        listView = (ListView) fragmentView.findViewById(R.id.listCarrello);
        listView.setAdapter(adp);
        TextView totale = (TextView) fragmentView.findViewById(R.id.txtTotaleCarrello);

        for (int i = 0; i < carrello.size(); i++) {
            tot += Float.valueOf(carrello.get(i).getPrezzo());
        }
        totale.setText("Totale: " + tot + "€");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Prodotto p = (Prodotto) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), dettaglio_prodotto.class);
                intent.putExtra("prodotto", p);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Prodotto p  =(Prodotto)arg0.getAdapter().getItem(pos);
                carrello.remove(pos);

                return true;
            }
        });

        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    private void checkCarrello(final String i, final String id){
        class checkCarrelloClass extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(s.contains("not")){
                    tot = tot - Float.valueOf(carrello.get(Integer.parseInt(i)).getPrezzo());
                    carrello.remove(Integer.parseInt(i));
                   Toast.makeText(getActivity(),id + "Rimosso", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("id",params[1]);
                RequestHandler ruc = new RequestHandler();

                String result = ruc.sendPostRequest(CARRELLO_URL,data);
                return result;
            }
        }
        checkCarrelloClass ccc = new checkCarrelloClass();
        ccc.execute(i,id);
    }


}
