package com.example.riccardo.hermes;

import java.util.ArrayList;

/**
 * Created by tomma on 29/04/2016.
 */
public class SingletonCarrello {

    private static SingletonCarrello istanza = null;

    private ArrayList<Prodotto> carrello;

    private SingletonCarrello(){
        carrello = new ArrayList<Prodotto>();
    }

    public static SingletonCarrello getInstance(){
        if(istanza == null)
        {
            istanza = new SingletonCarrello();
        }
        return istanza;
    }

    public ArrayList<Prodotto> getCarrello(){
        return this.carrello;
    }

}
