package com.example.riccardo.hermes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by riccardo on 19/04/16.
 */
public class ListAdapter extends ArrayAdapter<Prodotto> {
    ArrayList<Prodotto> p;
    Activity activity;
    public ListAdapter(Context context, ArrayList<Prodotto> p, Activity activity) {
        super(context, 0, p);
        this.p = p;
        this.activity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        Prodotto p = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView txtNome = (TextView) convertView.findViewById(R.id.txtListNome);
        TextView txtPrezzo = (TextView) convertView.findViewById(R.id.txtListPrezzo);
        ImageView img = (ImageView)convertView.findViewById(R.id.imgListImmagine);
        txtNome.setText(p.getNome());
        txtPrezzo.setText(p.getPrezzo() + "€");;
        Picasso.with(activity).invalidate(p.getUrlImmagine());
        Picasso.with(activity).load(p.getUrlImmagine()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(img);
        return convertView;
    }


}
