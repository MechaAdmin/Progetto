package com.example.riccardo.hermes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tomma on 29/04/2016.
 */
public class ListAdapterCarrello extends ArrayAdapter<Prodotto> {
    ArrayList<Prodotto> p;
    Activity activity;
    public ListAdapterCarrello(Context context, ArrayList<Prodotto> p, Activity activity) {
        super(context, 0, p);
        this.p = p;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        Prodotto p = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_carrello, parent, false);
        }
        TextView txtNome = (TextView) convertView.findViewById(R.id.txtListNomeCarrello);
        TextView txtPrezzo = (TextView) convertView.findViewById(R.id.txtListPrezzoCarrello);
        ImageView img = (ImageView)convertView.findViewById(R.id.imgListImmagineCarrello);
        txtNome.setText(p.getNome());
        txtPrezzo.setText(p.getPrezzo());;
        Picasso.with(activity).load(p.getUrlImmagine()).into(img);
        return convertView;
    }
}
