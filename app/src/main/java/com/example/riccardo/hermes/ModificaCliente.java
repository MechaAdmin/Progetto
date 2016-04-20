package com.example.riccardo.hermes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ModificaCliente extends AppCompatActivity {
    private TextView txtNome;
    private TextView txtCognome;
    private TextView txtUsername;
    private TextView txtDataNascita;
    private TextView txtCitta;
    private TextView txtCap;
    private TextView txtIndirizzo;
    private TextView txtMail;
    private TextView txtPassword;
    private TextView txtPaese;
    private ImageView immagine;
    String nome;
    String cognome;
    String username;
    String dataNascita;
    String citta;
    String indirizzo;
    String cap;
    String mail;
    String vecchiaMail;
    String password;
    String paese;
    Bitmap imgProfilo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_cliente);
        Intent intent = getIntent();
        String jsonCliente = intent.getStringExtra("json");
        txtNome = (TextView)findViewById(R.id.txtModificaNome);
        txtCognome = (TextView)findViewById(R.id.txtModificaCognome);
        txtUsername = (TextView)findViewById(R.id.txtModificaUsername);
        txtDataNascita = (TextView)findViewById(R.id.txtModificaDataNascita);
        txtCitta = (TextView)findViewById(R.id.txtModificaCitta);
        txtCap = (TextView)findViewById(R.id.txtModificaCap);
        txtIndirizzo = (TextView)findViewById(R.id.txtModificaIndirizzo);
        txtMail = (TextView)findViewById(R.id.txtModificaMail);
        txtPassword = (TextView)findViewById(R.id.txtModificaPassword);
        txtPaese = (TextView)findViewById(R.id.txtModificaPaese);
        immagine = (ImageView)findViewById(R.id.imgModificaImmagine);

        try {
            JSONObject jsonObject = new JSONObject(jsonCliente);
            JSONArray stringJson = jsonObject.getJSONArray("result");
            nome = stringJson.getJSONObject(0).getString("nome");
            cognome = stringJson.getJSONObject(0).getString("cognome");
            username = stringJson.getJSONObject(0).getString("username");
            dataNascita = stringJson.getJSONObject(0).getString("dataNascita");
            citta = stringJson.getJSONObject(0).getString("citta");
            indirizzo = stringJson.getJSONObject(0).getString("indirizzo");
            cap = stringJson.getJSONObject(0).getString("cap");
            vecchiaMail = stringJson.getJSONObject(0).getString("mail");
            password = stringJson.getJSONObject(0).getString("password");
            paese = stringJson.getJSONObject(0).getString("paese");
            final String urlImmagine = stringJson.getJSONObject(0).getString("immagine");
            txtNome.setText(nome);
            txtCognome.setText(cognome);
            txtUsername.setText(username);
            txtDataNascita.setText(dataNascita);
            txtCitta.setText(citta);
            txtIndirizzo.setText(indirizzo);
            txtCap.setText(cap);
            txtMail.setText(vecchiaMail);
            txtPassword.setText(password);
            txtPaese.setText(paese);
            Picasso.with(ModificaCliente.this).load(urlImmagine).into(immagine);
            BitmapDrawable drawable = (BitmapDrawable) immagine.getDrawable();
            imgProfilo = drawable.getBitmap();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        immagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        Button btnConferma = (Button)findViewById(R.id.btnConfermaModifica);
        btnConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = txtNome.getText().toString();
                cognome = txtCognome.getText().toString();
                dataNascita = txtDataNascita.getText().toString();
                citta =txtCitta.getText().toString();
                indirizzo = txtIndirizzo.getText().toString();
                cap = txtCap.getText().toString();
                mail = txtMail.getText().toString();
                password = txtPassword.getText().toString();
                paese = txtPaese.getText().toString();
                uploadDati();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                imgProfilo = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                immagine.setImageBitmap(resizeImage(imgProfilo));
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
                loading = ProgressDialog.show(ModificaCliente.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ModificaCliente.this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put("immagine", uploadImage);
                data.put("nome",nome);
                data.put("cognome",cognome);
                data.put("username",username);
                data.put("mail",mail);
                data.put("vecchiaMail",vecchiaMail);
                data.put("password",password);
                data.put("dataNascita",dataNascita);
                data.put("indirizzo",indirizzo);
                data.put("citta",citta);
                data.put("cap",cap);
                String result = rh.sendPostRequest("http://mechavendor.16mb.com/modificaCliente.php",data);

                return result;
            }
        }

        UploadDati ui = new UploadDati();
        ui.execute(imgProfilo);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
