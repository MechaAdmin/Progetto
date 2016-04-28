package com.example.riccardo.hermes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by riccardo on 22/04/16.
 */
public class dbPreferiti extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Preferiti";
    Context c;
    public dbPreferiti(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE Preferiti ( " +
                "id TEXT PRIMARY KEY , " +
                "nome TEXT, " +
                "prezzo TEXT, " +
                "descrizione TEXT, " +
                "urlImmagine TEXT, " +
                "venditore TEXT )";
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Preferiti");
        this.onCreate(db);

    }
    public void addProdotto(Prodotto p){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] COLUMNS = {"id","nome","prezzo","descrizione","urlImmagine","venditore"};
        Cursor cursor =
                db.query("Preferiti", // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(p.getId()) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();

        if(cursor.getCount() > 0){
            Toast.makeText(c,"Prodotto già aggiunto ai Preferiti",Toast.LENGTH_SHORT).show();
            return;
        }
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id",p.getId());
        values.put("nome",p.getNome());
        values.put("prezzo",p.getPrezzo());
        values.put("descrizione",p.getDescrizione());
        values.put("urlImmagine",p.getUrlImmagine());
        values.put("venditore",p.getVenditore());
        db.insert("Preferiti", null, values);
        Toast.makeText(c,"Prodotto aggiunto ai Preferiti",Toast.LENGTH_SHORT).show();
        db.close();

    }
    public Prodotto getProdotto(String id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] COLUMNS = {"id","nome","prezzo","descrizione","urlImmagine","venditore"};
        Cursor cursor =
                db.query("Preferiti", // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
        Prodotto p = new Prodotto(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(0),cursor.getString(4),cursor.getString(5));
        return p;
    }
    public ArrayList<Prodotto> getAllProdotto() {
        ArrayList<Prodotto> prodottiPreferiti = new ArrayList<Prodotto>();
        String query = "SELECT  * FROM Preferiti";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Prodotto p = null;
        if (cursor.moveToFirst()) {
            do {
                p = new Prodotto(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(0),cursor.getString(4),cursor.getString(5));
                prodottiPreferiti.add(p);
            } while (cursor.moveToNext());
        }

        return prodottiPreferiti;
    }
    public void deleteProdotto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Preferiti", "id"+" = ?", new String[] { String.valueOf(id) });
        db.close();

    }
}