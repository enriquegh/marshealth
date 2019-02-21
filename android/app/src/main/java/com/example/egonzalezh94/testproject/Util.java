package com.example.egonzalezh94.testproject;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {

    public static final String BASE_URL = "https://mars.enriquegh.com";
    public static final String API_URL = "/api.php/";


    public static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }

    public static String getAPIURL() {

        return BASE_URL + API_URL;
    }



}
