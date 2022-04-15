package com.enriquegh.marshealth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.enriquegh.marshealth.network.LoginErrorResponseListener;
import com.enriquegh.marshealth.network.LoginResponseListener;
import com.enriquegh.marshealth.util.BaseURLUtility;


public class LoginActivity extends AppCompatActivity {

    /**
     * This URL needs to be configured to wherever the API and SQL are, local or remote.
     */
    static final String API_URL = BaseURLUtility.getApiURL();
    static final String CLIENT_URL = "clients";
    private static final String TAG = "LoginActivity";
    EditText email;
    EditText password;

    String emailString;
    String passwordString;
    boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
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



        FloatingActionButton fab = findViewById(R.id.fab);
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

    /**
     * This function is called when the Login button is pressed. See content_login.xml
     * Gets client's username and password and sends them to CheckClient to check if credentials
     * are valid.
     *
     * @link content_login.xml
     * @param view - Current view
     */
    public void checkLogin(View view) {
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        emailString = email.getText().toString();
        passwordString = password.getText().toString();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String filter = String.format("?filter[]=email,eq,%s&filter[]=pwd,eq,%s", emailString, passwordString);
        String url = API_URL + CLIENT_URL + filter;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new LoginResponseListener(this, TAG),
                new LoginErrorResponseListener(this, TAG)
        );
        queue.add(stringRequest);

    }

    /**
     * Function called when "Call" button is pressed and would call the physician's office.
     * @param view - Current view
     */
    public void call(View view) {
        String phoneNum = "tel:555-555-5555";

        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNum));
        startActivity(callIntent);
    }
}
