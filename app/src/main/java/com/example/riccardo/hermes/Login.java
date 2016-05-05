package com.example.riccardo.hermes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Intent intentRegistrazione = new Intent(this,Registrazione.class);
        final Intent intentPrincipale = new Intent(this,Principale.class);
        final TextView txtRegistrati = (TextView)findViewById(R.id.txtRegistrati);
        txtUsername = (TextView)findViewById(R.id.txtModificaMail);
        txtPassword = (TextView)findViewById(R.id.txtModificaPassword);
        prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
        if(prefs.getString("username","false") != "false"){
            Intent intent = new Intent(Login.this,Principale.class);
            intent.putExtra(USER_NAME,prefs.getString("username","false"));
            startActivity(intent);
            finish();
        }
        txtRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentRegistrazione);
            }
        });
        final Button btnAccedi = (Button)findViewById(R.id.btnAccedi);
        btnAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                loading.dismiss();
                if(s.equalsIgnoreCase("ok")){
                    prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                    prefs.edit().putString("username",username).commit();
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
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RequestHandler ruc = new RequestHandler();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }

}
