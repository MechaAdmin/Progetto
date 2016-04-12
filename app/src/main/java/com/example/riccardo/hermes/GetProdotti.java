package com.example.riccardo.hermes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by riccardo on 08/04/16.
 */
public class GetProdotti {
    public static String[] prezzo;
    public static Bitmap[] bitmaps;
    public static String[] nomeProdotto;
    public static String[] id;
    public static String[] prezzoFiltrato;
    public static Bitmap[] bitmapsFiltrato;
    public static String[] nomeProdottoFiltrato;
    public static String[] idFiltrato;
    private String json;
    private JSONArray stringJson;

    public GetProdotti(String json){
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(json);
            stringJson = jsonObject.getJSONArray("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString("immagine"));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void getDati() throws JSONException {
        bitmaps = new Bitmap[stringJson.length()];
        nomeProdotto = new String[stringJson.length()];
        prezzo = new String[stringJson.length()];
        id = new String[stringJson.length()];
        for(int i=0;i< stringJson.length();i++){
            prezzo[i] = stringJson.getJSONObject(i).getString("prezzo") + "€";
            JSONObject jsonObject = stringJson.getJSONObject(i);
            bitmaps[i]=getImage(jsonObject);
            nomeProdotto[i] = stringJson.getJSONObject(i).getString("nomeProdotto");
            id[i] = stringJson.getJSONObject(i).getString("id");
        }
    }
    public void Filtra(String s){
        ArrayList<String> tmpNome = new ArrayList<String>();
        ArrayList<String> tmpPrezzo = new ArrayList<String>();
        ArrayList<String> tmpId = new ArrayList<String>();
        ArrayList<Bitmap> tmpBitmaps = new ArrayList<Bitmap>();
        for(int i = 0;i< nomeProdotto.length;i++){
            if(nomeProdotto[i].contains(s)){
                  tmpNome.add(nomeProdotto[i]);
                  tmpId.add(id[i]);
                  tmpBitmaps.add(bitmaps[i]);
                  tmpPrezzo.add(prezzo[i]);
            }
        }
        nomeProdottoFiltrato = new String[tmpPrezzo.size()];
        prezzoFiltrato = new String[tmpPrezzo.size()];
        idFiltrato = new String[tmpPrezzo.size()];
        bitmapsFiltrato = new Bitmap[tmpPrezzo.size()];
        for(int i = 0;i<tmpPrezzo.size();i++){
                nomeProdottoFiltrato[i] = tmpNome.get(i);
                prezzoFiltrato[i] = tmpPrezzo.get(i);
                idFiltrato[i] = tmpId.get(i);
                bitmapsFiltrato[i] = tmpBitmaps.get(i);
        }
    }

}
