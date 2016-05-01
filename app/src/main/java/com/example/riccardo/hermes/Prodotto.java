package com.example.riccardo.hermes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Prodotto implements Parcelable{
    private String nome;
    private String prezzo;
    private String descrizione;
    private String id;
    private String urlImmagine;
    private String venditore;
    public Prodotto(String nome, String prezzo, String descrizione, String id, String urlImmagine,String venditore) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.id = id;
        this.urlImmagine = urlImmagine;
        this.venditore = venditore;
    }
    public Prodotto(Parcel in){
        String[] data = new String[6];
        in.readStringArray(data);
        this.nome = data[0];
        this.prezzo = data[1];
        this.descrizione = data[2];
        this.id = data[3];
        this.urlImmagine = data[4];
        this.venditore = data[5];
    }

    public String getNome() {
        return nome;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getId() {
        return id;
    }

    public String getUrlImmagine() {
        return urlImmagine;
    }

    public String getVenditore() {
        return venditore;
    }



    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.nome,
                this.prezzo,
                this.descrizione,
                this.id,
                this.urlImmagine,
                this.venditore});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Prodotto createFromParcel(Parcel in) {
            return new Prodotto(in);
        }

        public Prodotto[] newArray(int size) {
            return new Prodotto[size];
        }
    };

}

