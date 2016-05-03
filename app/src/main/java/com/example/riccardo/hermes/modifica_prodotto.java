package com.example.riccardo.hermes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class modifica_prodotto extends AppCompatActivity {
    private TextView txtNome;
    private TextView txtPrezzo;
    private TextView txtDescrizione;
    private ImageView immagine;
    private Bitmap imgProdotto;
    String nome;
    String prezzo;
    String descrizione;
    String id;
    String categoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_prodotto);
        txtNome = (TextView)findViewById(R.id.txtModificaProdottoNome);
        txtPrezzo = (TextView)findViewById(R.id.txtModificaProdottoPrezzo);
        txtDescrizione = (TextView)findViewById(R.id.txtModificaProdottoDescrizione);
        immagine = (ImageView)findViewById(R.id.imgModificaProdottoImmagine);
        Button btnConferma = (Button)findViewById(R.id.btnConfermaModificaProdotto);
        Button btnElimina = (Button)findViewById(R.id.btnEliminaProdotto);
        Bundle data = getIntent().getExtras();
        final Prodotto p = data.getParcelable("prodotto");
        txtNome.setText(p.getNome());
        txtPrezzo.setText(p.getPrezzo());
        txtDescrizione.setText(p.getDescrizione());
        final String urlImmagine = p.getUrlImmagine();
        final Spinner spinner = (Spinner)findViewById(R.id.spinnerCategoriaModificaProdotto);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerVendita, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(p.getCategoria())){
                spinner.setSelection(i);
                break;
            }
        }
        Picasso.with(modifica_prodotto.this).load(urlImmagine).into(immagine);
        id = p.getId();
        BitmapDrawable drawable = (BitmapDrawable) immagine.getDrawable();
        imgProdotto = drawable.getBitmap();


        immagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminaProdotto();
            }
        });
        btnConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = txtNome.getText().toString();
                prezzo = txtPrezzo.getText().toString();
                descrizione = txtDescrizione.getText().toString();
                categoria = spinner.getSelectedItem().toString();
                uploadDati();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                imgProdotto = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                immagine.setImageBitmap(resizeImage(imgProdotto));
                imgProdotto = resizeImage(imgProdotto);
            } catch (IOException e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static Bitmap resizeImage(Bitmap image){
        double height = image.getHeight();
        double width = image.getWidth();
        double maxTexture = 2048;


        if(width > maxTexture || height > maxTexture) {
            if(width > height ){
                double scale = width - maxTexture;
                scale = scale / width;
                height = height - height * scale;
                width = maxTexture;

            }else{
                double scale = height - maxTexture;
                scale = scale / height;
                width = width - width * scale;
                height = maxTexture;
            }
        }
        return Bitmap.createScaledBitmap(image, (int)width, (int)height, true);
    }
    public void uploadDati(){
        class UploadDati extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(modifica_prodotto.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(modifica_prodotto.this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent Principale = new Intent(modifica_prodotto.this, Principale.class);
                startActivity(Principale);

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put("immagine", uploadImage);
                data.put("nome",nome);
                data.put("prezzo",prezzo);
                data.put("descrizione",descrizione);
                data.put("id",id);
                data.put("categoria",categoria);
                String result = rh.sendPostRequest("http://mechavendor.16mb.com/modificaProdotto.php",data);

                return result;
            }
        }

        UploadDati ui = new UploadDati();
        ui.execute(imgProdotto);
    }
    public void eliminaProdotto(){
        class UploadDati extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(modifica_prodotto.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(modifica_prodotto.this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent Principale = new Intent(modifica_prodotto.this, Principale.class);
                startActivity(Principale);

            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("id",id);
                String result = rh.sendPostRequest("http://mechavendor.16mb.com/eliminaProdotto.php",data);

                return result;
            }
        }

        UploadDati ui = new UploadDati();
        ui.execute();
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
