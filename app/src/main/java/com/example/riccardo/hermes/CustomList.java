package com.example.riccardo.hermes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by riccardo on 08/04/16.
 */
public class CustomList extends ArrayAdapter<String> {
    private String[] prezzo;
    private String[] id;
    private Bitmap[] immagini;
    private String[] nomeProdotto;
    private Activity context;

    public CustomList(Activity context,String[] id,String[] nomeProdotto, String[] prezzo, Bitmap[] immagini) {
        super(context, R.layout.list_item, prezzo);
        this.context = context;
        this.prezzo= prezzo;
        this.immagini= immagini;
        this.nomeProdotto = nomeProdotto;
        this.id = id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_item, null, true);
        TextView txtId = (TextView) listViewItem.findViewById(R.id.txtListId);
        ImageView imgProdotto = (ImageView) listViewItem.findViewById(R.id.imgListImmagine);
        TextView txtNomeProdotto = (TextView) listViewItem.findViewById(R.id.txtListNome);
        TextView txtPrezzo = (TextView) listViewItem.findViewById(R.id.txtListPrezzo);
        txtNomeProdotto.setText(nomeProdotto[position]);
        txtPrezzo.setText(prezzo[position]);
        imgProdotto.setImageBitmap(Bitmap.createScaledBitmap(immagini[position],100,50,false));
        return  listViewItem;
    }
}
