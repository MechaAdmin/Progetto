package com.example.riccardo.hermes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    private static final String LOGIN_URL = "http://MechaVendor.16mb.com/login.php";
    public static final String USER_NAME = "USER_NAME";
    TextView txtUsername;
    TextView txtPassword;
    TextView txtRegistrati;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setto il layout
        setContentView(R.layout.activity_login);

        // recupero le varie componenti dell'UI
        txtRegistrati = (TextView)findViewById(R.id.txtRegistrati);
        txtUsername = (TextView)findViewById(R.id.txtModificaMail);
        txtPassword = (TextView)findViewById(R.id.txtModificaPassword);

        //recupero le SharedPreferences passando al metodo il contesto che le richiede
        prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
        String prova = prefs.getString("username","false");
        //se username è rinosciuto effettuo il login automatico senza richiedere di nuovo le credenziali di accesso
        if(!prova.equals("false")){
            Intent intent = new Intent(Login.this,Principale.class);
            intent.putExtra(USER_NAME,prefs.getString("username","false"));
            startActivity(intent);
            finish();
        }

        txtRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //faccio partire l'activity Registrazione
                Intent intentRegistrazione = new Intent(Login.this,Registrazione.class);
                startActivity(intentRegistrazione);
            }
        });

        final Button btnAccedi = (Button)findViewById(R.id.btnAccedi);
        btnAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recupero Username e Password digitati dall'utente chiamo il metodo userLogin passandogli i parametri richiesti
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                userLogin(username,password);
            }
        });
    }
    private void userLogin(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //fermo il loading
                loading.dismiss();
                //se s (che nel nostro caso è la risposta della pagina php) è uguale a ok
                if(s.equalsIgnoreCase("ok")){

                    //cambio le SharedPreferences in modo che l'utente possa eseguire l'accesso
                    //automatico senza dover ogni volta digitare le credenziali
                    prefs.edit().putString("username",username).commit();

                    //faccio partire l'activity principale passandogli l'username di chi ha effettuato l'accesso
                    Intent intent = new Intent(Login.this,Principale.class);
                    intent.putExtra(USER_NAME,username);
                    startActivity(intent);

                    finish();

                }else{
                    Toast.makeText(Login.this,s,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                //Creo un hashMap per utilizzarla come contenitore per i dati dati inviare alla pagina php
                HashMap<String,String> data = new HashMap<>();
                //Riprendo i dati contenuti nel vettore params e li inserisco nell'hashmap
                data.put("username",params[0]);
                data.put("password",params[1]);

                //Creo un oggetto RequestHandler e tramite il metodo sendPostRequest invio alla
                //pagina php username e password e aspetto la risposta che viene assegnata a result
                RequestHandler ruc = new RequestHandler();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        //Instanzio un oggetto della classe appena creata e faccio partire il task con execute
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }

}
