package com.example.riccardo.hermes;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by riccardo on 08/04/16.
 */
public class Vendita extends Fragment {
    private Uri filePath;
    private TextView txtNome;
    private TextView txtPrezzo;
    private TextView txtDescrizione;
    private String nomeProdotto;
    private String prezzo;
    private String descrizione;
    private String categoria;
    private Button btnInsVendita;
    private ImageView immagine;
    private Bitmap bitmap;
    private String mail;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_vendita,null);
        mail = this.getArguments().getString("mail");
        txtNome = (TextView)fragmentView.findViewById(R.id.txtModificaNome);
        txtPrezzo = (TextView)fragmentView.findViewById(R.id.txtVenditaPrezzo);
        txtDescrizione = (TextView)fragmentView.findViewById(R.id.txtVenditaDescrizione);
        btnInsVendita = (Button)fragmentView.findViewById(R.id.btnConfermaModifica);
        immagine = (ImageView)fragmentView.findViewById(R.id.imgVenditaImmagine);
        final Spinner spinner = (Spinner)fragmentView.findViewById(R.id.spinnerCategoriaVendita);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerVendita, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        immagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        btnInsVendita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nomeProdotto = txtNome.getText().toString();
                prezzo = txtPrezzo.getText().toString();
                descrizione = txtDescrizione.getText().toString();
                categoria = spinner.getSelectedItem().toString();
                uploadDati();
            }
        });
        return fragmentView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                immagine.setImageBitmap(resizeImage(bitmap));
                bitmap = resizeImage(bitmap);
            } catch (IOException e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void uploadDati(){
        class UploadDati extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
            String uploadImage = getStringImage(bitmap);

            HashMap<String,String> data = new HashMap<>();

            data.put("immagine", uploadImage);
            data.put("nomeProdotto",nomeProdotto);
            data.put("prezzo",prezzo);
            data.put("descrizione",descrizione);
            data.put("categoria",categoria);
            data.put("venditore",mail);
            String result = rh.sendPostRequest("http://mechavendor.16mb.com/uploadProdotti.php",data);

            return result;
        }
    }

    UploadDati ui = new UploadDati();
    ui.execute(bitmap);
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

}
