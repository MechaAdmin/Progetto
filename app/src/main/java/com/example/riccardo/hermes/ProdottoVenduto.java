package com.example.riccardo.hermes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by riccardo on 09/05/16.
 */
public class ProdottoVenduto extends Prodotto {
    String acquirente;
    String dataVendita;
    public ProdottoVenduto(String nome, String prezzo, String descrizione, String id, String urlImmagine, String venditore, String categoria, String acquirente, String dataVendita){
        super(nome, prezzo, descrizione, id, urlImmagine, venditore, categoria);
        this.acquirente = acquirente;
        this.dataVendita = dataVendita;
    }
    public ProdottoVenduto(Parcel in){
        super(in);
        String[] data = new String[2];
        in.readStringArray(data);
        this.acquirente = data[0];
        this.dataVendita = data[1];
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.acquirente,
                this.dataVendita,});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ProdottoVenduto createFromParcel(Parcel in) {
            return new ProdottoVenduto(in);
        }

        public ProdottoVenduto[] newArray(int size) {
            return new ProdottoVenduto[size];
        }
    };
}
