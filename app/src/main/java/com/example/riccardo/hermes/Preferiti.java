package com.example.riccardo.hermes;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by riccardo on 22/04/16.
 */
public class Preferiti extends Fragment {
    View fragmentView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView =  inflater.inflate(R.layout.fragment_preferiti,null);
        dbPreferiti db = new dbPreferiti(getActivity());
        ArrayList<Prodotto> list = db.getAllProdotto();
        for(int i = 0;i< list.size();i++){
            Log.d("list",list.get(i).getNome());
        }
        return  fragmentView;
    }
}
