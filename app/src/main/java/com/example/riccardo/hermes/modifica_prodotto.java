package com.example.riccardo.hermes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    Boolean x = false;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
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
//        BitmapDrawable drawable = (BitmapDrawable) immagine.getDrawable();
//        imgProdotto = drawable.getBitmap();


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
                if(!x){
                    BitmapDrawable drawable = (BitmapDrawable) immagine.getDrawable();
                    imgProdotto = drawable.getBitmap();
                }
                uploadDati();
            }
        });
    }
    private void selectImage() {
        final CharSequence[] items = { "Fotocamera", "Galleria",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aggiungi Immagine");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Fotocamera")) {
                    cameraIntent();

                } else if (items[item].equals("Galleria")) {
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        imgProdotto = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imgProdotto.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        immagine.setImageBitmap(resizeImage(imgProdotto));
        imgProdotto = resizeImage(imgProdotto);

    }
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                imgProdotto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        immagine.setImageBitmap(resizeImage(imgProdotto));
        imgProdotto = resizeImage(imgProdotto);
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
