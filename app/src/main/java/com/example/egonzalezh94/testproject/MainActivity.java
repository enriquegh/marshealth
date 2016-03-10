package com.example.egonzalezh94.testproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    static final String API_URL = "http://10.1.120.243/api.php/";
    static final String CLIENT_URL = "clients2";
    static final String APPOINTMENT_URL = "appointments";

    EditText nameText;
    EditText majorText;
    TextView resultBox;
    ProgressBar progressBar;
    Spinner dropDownMenu;

    String name;
    String major;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        nameText = (EditText) findViewById(R.id.nameText);
        majorText = (EditText) findViewById(R.id.majorText);
        resultBox = (TextView) findViewById(R.id.newTextBox);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        dropDownMenu = (Spinner) findViewById(R.id.StatusText);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dropDownMenu.setAdapter(adapter);

        Button queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                status = dropDownMenu.getSelectedItem().toString();
                name = nameText.getText().toString();
                major = majorText.getText().toString();

                //new SendClient().execute(name,major,status);
                new RetrieveSchedule().execute();

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

                Log.e("ERROR",e.toString(),e);

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
                    Log.e("major",major);
                    Log.e("status",status);
                    String urlParameters = "name=" + URLEncoder.encode(this.name, "UTF-8") +
                            "&major=" + URLEncoder.encode(major, "UTF-8") +
                            "&status=" + URLEncoder.encode(status, "UTF-8");

                    DataOutputStream wr = new DataOutputStream(
                            urlConnection.getOutputStream ());
                    wr.writeBytes (urlParameters);
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

                Log.e("ERROR",e.toString(),e);

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

    class RetrieveSchedule extends AsyncTask<Void, Void, String> {

        private Exception exception;
        //To show available appointments only
        private static final String filter = "?filter=status,eq,0";

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            resultBox.setText("");
        }

        protected String doInBackground(Void... urls) {
            //String name = nameText.getText().toString();
            // Do some validation here

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

                Log.e("ERROR",e.toString(),e);

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
}


