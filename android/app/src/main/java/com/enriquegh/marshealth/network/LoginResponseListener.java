package com.enriquegh.marshealth.network;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.enriquegh.marshealth.MainActivity;
import com.enriquegh.marshealth.MessageService;
import com.enriquegh.marshealth.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class LoginResponseListener implements Response.Listener<String>{

    Activity activity;
    String tag;
    public LoginResponseListener(Activity activity, String tag) {
        this.activity = activity;
        this.tag = tag;
    }
    @Override
    public void onResponse(String response) {

        Log.i(this.tag, "Response: " + response);
        try {
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            JSONObject clients = object.getJSONObject("clients");
            JSONArray recordsList = clients.getJSONArray("records");

            if (recordsList.length() == 0) {
                TextView tv = activity.findViewById(R.id.loginText);
                tv.setText("Invalid username or password");

            }
            else if (recordsList.length() == 1) {
                String username = recordsList.getJSONArray(0).get(3).toString();
                String userID = recordsList.getJSONArray(0).get(0).toString();
                String name = recordsList.getJSONArray(0).get(1).toString() + " " + recordsList.getJSONArray(0).get(2).toString();
                //Result should be valid
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                prefs.edit().putBoolean("isLogin", true).apply(); // isLogin is a boolean value of your login status
                prefs.edit().putString("username", username).apply();
                prefs.edit().putString("userid", userID).apply();
                prefs.edit().putString("name", name).apply();

                Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                Intent serviceIntent = new Intent(activity.getApplicationContext(), MessageService.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                activity.startActivity(intent);
                activity.startService(serviceIntent);
                activity.finish();
            }

            else { //Shouldn't have more than two records with same password and email
                //TODO: Throw exception
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
