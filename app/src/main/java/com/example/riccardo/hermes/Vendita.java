package com.example.riccardo.hermes;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by riccardo on 01/04/16.
 */
public class Vendita extends Fragment {
    private TextView txtNome;
    private TextView txtPrezzo;
    private TextView txtDescrizione;
    private String nomeProdotto;
    private String prezzo;
    private String descrizione;
    private Button btnInsVendita;
    private Button btnCaricaImmagine;
    private ImageView immagine;
    private Bitmap bitmap;
    private Uri filePath;
    public static final String UPLOAD_URL = "http://mechavendor.16mb.com/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_vendita,null);
        txtNome = (TextView)fragmentView.findViewById(R.id.txtVenditaNome);
        txtPrezzo = (TextView)fragmentView.findViewById(R.id.txtVenditaPrezzo);
        txtDescrizione = (TextView)fragmentView.findViewById(R.id.txtVenditaDescrizione);
        btnInsVendita = (Button)fragmentView.findViewById(R.id.btnAggiungiVendita);
        btnCaricaImmagine = (Button)fragmentView.findViewById(R.id.btnVenditaCaricaImmagine);
        immagine = (ImageView)fragmentView.findViewById(R.id.imgVenditaImmagine);
        btnCaricaImmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        btnInsVendita.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                nomeProdotto = txtNome.getText().toString();
                prezzo = txtPrezzo.getText().toString();
                descrizione = txtDescrizione.getText().toString();
                uploadDati();
            }
        });
        return fragmentView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                immagine.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadDati(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();

            }
            public String getStringImage(Bitmap bmp){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                return encodedImage;
            }
            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put("nome",nomeProdotto);
                data.put("prezzo",prezzo);
                data.put("descrizione",descrizione);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
}

