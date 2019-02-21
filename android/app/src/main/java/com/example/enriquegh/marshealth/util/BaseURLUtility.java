package com.example.enriquegh.marshealth.util;

import com.example.enriquegh.marshealth.BuildConfig;

public class BaseURLUtility {

    public static final String API_URL = "/api.php/";


    public static String getBaseURL() {
        return BuildConfig.BASE_URL;
    }

    public static String getApiURL() {
        return BuildConfig.BASE_URL + API_URL;
    }



}
