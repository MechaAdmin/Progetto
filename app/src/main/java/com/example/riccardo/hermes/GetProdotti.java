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

/**
 * Created by riccardo on 08/04/16.
 */
public class GetProdotti {
    public static String[] prezzo;
    public static Bitmap[] bitmaps;
    public static String[] nomeProdotto;
    public static String[] id;
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

            prezzo[i] = stringJson.getJSONObject(i).getString("prezzo");
            JSONObject jsonObject = stringJson.getJSONObject(i);
            bitmaps[i]=getImage(jsonObject);
            nomeProdotto[i] = stringJson.getJSONObject(i).getString("nomeProdotto");
            id[i] = stringJson.getJSONObject(i).getString("id");
            Log.d("Prodotto",id[i] + nomeProdotto[i] +prezzo[i] + bitmaps[i] );
        }
    }
}
