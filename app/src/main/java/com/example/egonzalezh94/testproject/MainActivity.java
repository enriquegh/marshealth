package com.example.egonzalezh94.testproject;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    static final String API_URL = "http://10.1.12.153/api.php/";
    static final String CLIENT_URL = "clients2";
    static final String APPOINTMENT_URL = "appointments";

    EditText startDateText;
    EditText endDateText;
    TextView resultBox;
    ProgressBar progressBar;

    String startDate;
    String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        startDateText = (EditText) findViewById(R.id.startDateText);
        endDateText = (EditText) findViewById(R.id.endDateText);
        resultBox = (TextView) findViewById(R.id.newTextBox);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        Button queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startDate = startDateText.getText().toString();
                endDate = endDateText.getText().toString();

                //new SendClient().execute(name,major,status);
                new RetrieveSchedule().execute(startDate,endDate);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class RetrieveClient extends AsyncTask<Void, Void, String> {

        private Exception exception;
        //String name;
        //RetrieveClient(String name) {
        //   this.name = name;
        //}

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            resultBox.setText("");
        }

        protected String doInBackground(Void... urls) {
            //String name = nameText.getText().toString();
            // Do some validation here

            try {
                URL url = new URL(API_URL + CLIENT_URL);
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
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            resultBox.setText(response);
            // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
//                .
//                .
//                .
//                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

    }

    class SendClient extends AsyncTask<String, Void, String> {
        String name;
        String major;
        String status;

        int totalLength;


        //String name;
        //RetrieveClient(String name) {
        //   this.name = name;
        //}

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            resultBox.setText("");
        }

        protected String doInBackground(String... params) {
            //String name = nameText.getText().toString();
            // Do some validation here
            name = params[0];
            major = params[1];
            status = params[2];
            totalLength = name.length() + major.length() + status.length();

            try {
                URL url = new URL(API_URL + CLIENT_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Connection", "close");
                try {
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(totalLength);
                    Log.e("name", name);
                    Log.e("major", major);
                    Log.e("status", status);
                    String urlParameters = "name=" + URLEncoder.encode(this.name, "UTF-8") +
                            "&major=" + URLEncoder.encode(major, "UTF-8") +
                            "&status=" + URLEncoder.encode(status, "UTF-8");

                    DataOutputStream wr = new DataOutputStream(
                            urlConnection.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    //TODO: EOFException is thrown when using API 16 or lower.
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()));
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
                response = "THERE WAS AN ERROR IN SENDCLIENT";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            // TODO: do something with the feed

//            try {
//                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
//                String requestID = object.getString("requestId");
//                int likelihood = object.getInt("likelihood");
//                JSONArray photos = object.getJSONArray("photos");
//                .
//                .
//                .
//                .
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

    }

    class RetrieveSchedule extends AsyncTask<String, Void, String> {

        //To show available appointments only


        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            resultBox.setText("");
        }

        protected String doInBackground(String... params) {
            String dateStart = params[0], dateEnd = params[1];
            /**
             * This filter string will filter the results in three ways:
             * 1. Send only results that have a status of 0 (meaning status is available)
             * 2. Where the date is equal or greater than the start date.
             * 3. Where the date is equal or less than the end date.
             */
            final String filter = String.format("?filter[]=status,eq,0&filter[]=date,ge,%s&filter[]=date,le,%s",dateStart,dateEnd);
            try {
                //For now I believe this is insecure since the filter parameter is passed
                //within the URL and could be manipulated.
                //TODO: Figure out how to send parameter through request body
                URL url = new URL(API_URL + APPOINTMENT_URL + filter);
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
            progressBar.setVisibility(View.GONE);

            try {
                String pastText;

                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject appointments = object.getJSONObject("appointments");
                JSONArray recordsList = appointments.getJSONArray("records");

                for (int i=0;i<recordsList.length();i++) {
                    JSONArray records = recordsList.getJSONArray(i);
                    String date = records.getString(0);
                    String timeStart = records.getString(1);
                    String timeEnd = records.getString(2);

                    //TODO arrange records onto a schedule table.
                    /**
                     * For now the results are just being printed in order.
                     */
                    pastText = (String) resultBox.getText();
                    String result = String.format("%s \n Date: %s Start: %s End: %s",pastText,date,timeStart,timeEnd);
                    resultBox.setText(result);
                }


            } catch (JSONException e) {
                Log.e("JSON error", e.toString(), e);
//            }
            }

        }


    }

}