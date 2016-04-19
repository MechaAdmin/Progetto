package com.example.riccardo.hermes;

import android.graphics.Bitmap;

public class Prodotto {
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

    public void setVenditore(String venditore) {
        this.venditore = venditore;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrlImmagine(String urlImmagine) {
        this.urlImmagine = urlImmagine;
    }

}

