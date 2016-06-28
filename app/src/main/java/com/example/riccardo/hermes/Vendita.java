package com.example.riccardo.hermes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
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
        //immagine.setImageResource(R.drawable.default_product);
        immagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        btnInsVendita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nomeProdotto = txtNome.getText().toString();
                prezzo = txtPrezzo.getText().toString();
                descrizione = txtDescrizione.getText().toString();
                categoria = spinner.getSelectedItem().toString();
                if (!nomeProdotto.isEmpty() && !prezzo.isEmpty()){
                    uploadDati();
                }else{
                    Toast.makeText(getActivity(),"Campo mancante",Toast.LENGTH_LONG).show();
                }

            }
        });
        return fragmentView;
    }
    private void selectImage() {
        final CharSequence[] items = { "Fotocamera", "Galleria",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        immagine.setImageBitmap(resizeImage(bitmap));
        bitmap = resizeImage(bitmap);
    }
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        immagine.setImageBitmap(resizeImage(bitmap));
        bitmap = resizeImage(bitmap);
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
