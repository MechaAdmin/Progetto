package com.example.riccardo.hermes;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by tomma on 27/04/2016.
 */
public class Carrello extends Fragment {
    ListAdapterCarrello adp;
    ArrayList<Prodotto> carrello;

    ListView listView;
    View fragmentView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView =  inflater.inflate(R.layout.fragment_carrello, null);

        //Riprendo il carrello
        carrello = SingletonCarrello.getInstance().getCarrello();
        adp = new ListAdapterCarrello(getActivity(),carrello,getActivity());
        listView = (ListView) fragmentView.findViewById(R.id.listCarrello);
        listView.setAdapter(adp);









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


}
