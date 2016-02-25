package com.example.egonzalezh94.testproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    static final String API_URL = "http://192.168.1.71/api.php/clients";
    static final String USER = "test";
    static final String PASS = "test";

    EditText nameText;
    TextView resultBox;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        nameText = (EditText) findViewById(R.id.nameText);
        resultBox = (TextView) findViewById(R.id.newTextBox);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button queryButton = (Button) findViewById(R.id.queryButton);

        queryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });

        //createClient("Andrew Segal", "Computer Science", "PROFESSOR");
        //testDB();


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

    public void testDB() {

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

        TextView tv = (TextView) this.findViewById(R.id.newTextBox);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection testConn = DriverManager.getConnection(API_URL, USER, PASS);
            String result = "";

            Statement statement = testConn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
            ResultSetMetaData metaData = resultSet.getMetaData();

            while (resultSet.next()) {
                result += metaData.getColumnName(1) + resultSet.getInt(1) + "\n";
                result += metaData.getColumnName(2) + resultSet.getString(2) + "\n";
                result += metaData.getColumnName(3) + resultSet.getString(3) + "\n";
                result += metaData.getColumnName(4) + resultSet.getString(4) + "\n";

            }

            tv.setText(result);
            CharSequence text = tv.getText();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tv.setText(e.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            tv.setText(e.toString());
        }


    }

    public void createClient(String name, String major, String status) {

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView tv = (TextView) this.findViewById(R.id.newTextBox);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection testConn = DriverManager.getConnection(API_URL, USER, PASS);

            Statement statement = testConn.createStatement();


            int i = statement.executeUpdate(String.format("INSERT INTO clients (id, name, major, status) VALUES (NULL, '%s', '%s', '%s')", name, major, status));



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tv.setText(e.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            tv.setText(e.toString());
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        //String name;
        //RetrieveFeedTask(String name) {
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
                URL url = new URL(API_URL);
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
                //resultBox.setText(e.toString());
                Log.e("ERROR",e.toString(),e);

                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            resultBox.setText(response);
            // TODO: check this.exception
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

