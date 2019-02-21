package com.example.egonzalezh94.testproject;

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
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    /**
     * This URL needs to be configured to wherever the API and SQL are, local or remote.
     */
    static final String API_URL = "https://mars.enriquegh.com/api.php/";
    //static final String API_URL = "http://[INSERT SERVER ADDRESS]/api.php/";
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

        //Check if user has logged in before
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLogin = prefs.getBoolean("isLogin", false);

        //If user is already logged in, change activity and start Message Service
        if(isLogin) {
            Intent intent = new Intent(this, MainActivity.class);
            Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            startService(serviceIntent);
            finish();
        }
        String base_url = "";
        try {
            base_url = Util.getProperty("BASE_URL", getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
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

        }

        protected String doInBackground(Object... params) {

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
                response = "THERE WAS AN ERROR ON CHECKCLIENT";
            }
            Log.i("INFO", response);
            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject clients = object.getJSONObject("clients");
                JSONArray recordsList = clients.getJSONArray("records");
                //TODO: Check if empty
                String username = recordsList.getJSONArray(0).get(3).toString();
                String userID = recordsList.getJSONArray(0).get(0).toString();
                String name = recordsList.getJSONArray(0).get(1).toString() + " " + recordsList.getJSONArray(0).get(2).toString();


                if (recordsList.length() == 0) {
                    TextView tv = (TextView) findViewById(R.id.loginText);
                    tv.setText("Invalid username or password");

                }
                else if (recordsList.length() == 1) {
                    //Result should be valid
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    prefs.edit().putBoolean("isLogin", true).apply(); // isLogin is a boolean value of your login status
                    prefs.edit().putString("username", username).apply();
                    prefs.edit().putString("userid", userID).apply();
                    prefs.edit().putString("name", name).apply();

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


    /**
     * This function is called when the Login button is pressed. See content_login.xml
     * Gets client's username and password and sends them to CheckClient to check if credentials
     * are valid.
     *
     * @link content_login.xml
     * @param view
     */
    public void checkLogin(View view) {
        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        emailString = email.getText().toString();
        passwordString = password.getText().toString();

        new CheckClient(this).execute(emailString, passwordString);
    }

    /**
     * Function called when "Call" button is pressed and would call the physician's office.
     * @param view
     */
    public void call(View view) {
        String phoneNum = "ENTER PHONE NUMBER";

        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNum));
        startActivity(callIntent);
    }
}
