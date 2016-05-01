package com.example.egonzalezh94.testproject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    static final String API_URL = "http://192.168.1.253/api.php/";
    static final String CLIENT_URL = "clients";
    EditText email;
    EditText password;

    String emailString;
    String passwordString;
    boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLogin = prefs.getBoolean("isLogin", false);

        if(isLogin) {
            Intent intent = new Intent(this, MainActivity.class);
            Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            startService(serviceIntent);
            finish();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    class CheckClient extends AsyncTask<Object, Void, String> {
        private Context context;
        public CheckClient(Context context){
            this.context=context;
        }

        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            //resultBox.setText("");
        }

        protected String doInBackground(Object... params) {
            //String name = nameText.getText().toString();
            // Do some validation here

            String email = (String) params[0];
            String password = (String) params[1];

            String filter = String.format("?filter[]=email,eq,%s&filter[]=pwd,eq,%s", email, password);
            try {
                URL url = new URL(API_URL + CLIENT_URL + filter);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {

                Log.e("ERROR", e.toString(), e);

                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR ON RETRIEVECLIENT";
            }
            //progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            //resultBox.setText(response);
            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject clients = object.getJSONObject("clients");
                JSONArray recordsList = clients.getJSONArray("records");
                String username = recordsList.getJSONArray(0).get(3).toString();


                if (recordsList.length() == 0) {
                    TextView tv = (TextView) findViewById(R.id.loginText);
                    tv.setText("Invalid username or password");

                }
                else if (recordsList.length() == 1) {
                    //Result should be valid
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    prefs.edit().putBoolean("isLogin", true).apply(); // isLogin is a boolean value of your login status
                    prefs.edit().putString("username", username).apply();
                    Intent intent = new Intent(context, MainActivity.class);
                    Intent serviceIntent = new Intent(context, MessageService.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    startService(serviceIntent);

                    finish();


                }

                else { //Shouldn't have more than two records with same password and email
                    //TODO: Throw exception
                }
            }
            catch (JSONException e) {
                Log.e("JSON error", e.toString(), e);
            }


        }

    }


    public void checkLogin(View view) {
        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        emailString = email.getText().toString();
        passwordString = password.getText().toString();

        new CheckClient(this).execute(emailString, passwordString);




    }


    public void call(View view) {
        String phoneNum = "6195653087";

        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNum));
        startActivity(callIntent);
    }
}
