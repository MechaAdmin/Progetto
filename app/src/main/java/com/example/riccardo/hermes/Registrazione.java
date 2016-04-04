package com.example.riccardo.hermes;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Registrazione  extends AppCompatActivity {
    private TextView txtNome;
    private TextView txtCognome;
    private TextView txtUsername;
    private TextView txtDataNascita;
    private TextView txtCitta;
    private TextView txtCap;
    private TextView txtIndirizzo;
    private TextView txtMail;
    private TextView txtPassword;
    private Bitmap immagine;
    private static final String Registrazione_Url = "http://MechaVendor.16mb.com/inserimentoCliente.php";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        final Button btnRegistrazione = (Button)findViewById(R.id.btnAggiungiVendita);
        txtNome = (TextView)findViewById(R.id.txtVenditaNome);
        txtCognome = (TextView)findViewById(R.id.txtCognome);
        txtUsername = (TextView)findViewById(R.id.txtUserName);
        txtDataNascita = (TextView)findViewById(R.id.txtDataNascita);
        txtCitta = (TextView)findViewById(R.id.txtCitta);
        txtCap = (TextView)findViewById(R.id.txtCap);
        txtIndirizzo = (TextView)findViewById(R.id.txtIndirizzo);
        txtMail = (TextView)findViewById(R.id.txtUsernameNavBar);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
        immagine = BitmapFactory.decodeResource(getResources(),R.drawable.userdefault);
        btnRegistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nome = txtNome.getText().toString().trim().toLowerCase();
                final String cognome = txtCognome.getText().toString().trim().toLowerCase();
                final String username = txtUsername.getText().toString().trim().toLowerCase();
                final String email = txtMail.getText().toString().trim().toLowerCase();
                final String password = txtPassword.getText().toString().trim();
                final String dataNascita = txtDataNascita.getText().toString();
                final String indirizzo = txtIndirizzo.getText().toString();
                final String citta = txtCitta.getText().toString();
                final String cap = txtCap.getText().toString();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                immagine.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);


                registrazione(nome,cognome,username,email,password,dataNascita,indirizzo,citta,cap,encodedImage);
            }
        });
    }
    private void registrazione(String nome, String cognome, String username, String email,String password,String dataNascita,String indirizzo,String citta, String cap,String immagine){
      class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Registrazione.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("nome",params[0]);
                data.put("cognome",params[1]);
                data.put("username",params[2]);
                data.put("mail",params[3]);
                data.put("password",params[4]);
                data.put("dataNascita",params[5]);
                data.put("indirizzo",params[6]);
                data.put("citta",params[7]);
                data.put("cap",params[8]);
                data.put("immagine",params[9]);
                String result = ruc.sendPostRequest(Registrazione_Url,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(nome,cognome,username,email,password,dataNascita,indirizzo,citta,cap,immagine);

    }
}
