package com.example.egonzalezh94.testproject.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.egonzalezh94.testproject.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseURLUtility {

    public static final String API_URL = "/api.php/";


    public static String getBaseURL() {
        return BuildConfig.BASE_URL;
    }

    public static String getApiURL() {
        return BuildConfig.BASE_URL + API_URL;
    }



}
