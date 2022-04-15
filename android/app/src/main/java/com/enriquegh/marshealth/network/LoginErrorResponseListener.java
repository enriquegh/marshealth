package com.enriquegh.marshealth.network;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.enriquegh.marshealth.R;

public class LoginErrorResponseListener implements Response.ErrorListener{

    Activity activity;
    String tag;
    public LoginErrorResponseListener(Activity activity, String tag) {
        this.activity = activity;
        this.tag = tag;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(this.tag, "onErrorResponse: ", error);
        TextView tv = activity.findViewById(R.id.loginText);
        tv.setText("Something went wrong");
    }
}
