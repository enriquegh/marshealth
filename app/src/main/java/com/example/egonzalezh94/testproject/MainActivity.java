package com.example.egonzalezh94.testproject;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "jdbc:mysql://10.1.86.20:8889/medical_app";
    private static final String USER = "test";
    private static final String PASS = "test";

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
        createClient("Andrew Segal", "Computer Science", "PROFESSOR");
        testDB();


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
            Connection testConn = DriverManager.getConnection(URL, USER, PASS);
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
            Connection testConn = DriverManager.getConnection(URL, USER, PASS);

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
}
