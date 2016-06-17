package com.example.egonzalezh94.testproject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AppointmentFragment extends Fragment {

    static final String API_URL = "http://[INSERT SERVER ADDRESS]/api.php/";
    static final String APPOINTMENT_URL = "appointments";

    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;

    EditText startDateText;
    EditText endDateText;

    // This will be opened when focus is changed to startDateText or endDateText
    Calendar cal = Calendar.getInstance();

    TextView resultBox;
    ProgressBar progressBar;
    String startDate;
    String endDate;

/*    AppointmentAdapter adapter;
    ListView appointmentList;*/



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_appointments, container, false); // inflating the layout

        startDateText = (EditText) view.findViewById(R.id.startDateText);
        endDateText = (EditText) view.findViewById(R.id.endDateText);
        resultBox = (TextView) view.findViewById(R.id.newTextBox);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);



        // A listener for each date input box
        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), startListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), endListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button queryButton = (Button) view.findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startDate = startDateText.getText().toString();
                endDate = endDateText.getText().toString();

                new RetrieveSchedule().execute(startDate, endDate);

            }
        });

        return view;

    }

    // Global listeners for startDate and endDate EditText objeects
    DatePickerDialog.OnDateSetListener startListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateStart();
        }
    };


    DatePickerDialog.OnDateSetListener endListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEnd();
        }
    };
    // TODO Combine updateStart() and updateEnd() into one function somehow?
    private void updateStart() {

        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        startDateText.setText(sdf.format(cal.getTime()));

    }
    private void updateEnd() {

        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        endDateText.setText(sdf.format(cal.getTime()));

    }

    protected class AppointmentButton extends Button {
        String appointmentID;
        String timeStart;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Do stuff
            }
        };

        public AppointmentButton(Context context, String a, String t) {
            super(context);
            appointmentID = a;
            timeStart = t;
        }

        public void setAppointmentID(String a) {
            appointmentID = a;
        }

        public void setTimeStart(String t) {
            timeStart = t;
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
            final String filter = String.format("?filter[]=status,eq,0&filter[]=date,ge,%s&filter[]=date,le,%s", dateStart, dateEnd);
            try {
                //For now I believe this is insecure since the filter parameter is passed
                //within the URL and could be manipulated.
                //TODO: Figure out how to send parameter through request body POST method
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

        // Available Timeslots view
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR ON RETRIEVESCHEDULE";
            }
            progressBar.setVisibility(View.GONE);

            try {
                String pastText;

                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject appointments = object.getJSONObject("appointments");
                JSONArray appointmentRecords  = appointments.getJSONArray("records");

                // TODO print hourly from time open to time closed (use schedule table)
                // TODO next to each hour, print 15 min intervals if appointment table doesn't contain an appointment from that time slot


                    for (int i = 0; i < appointmentRecords.length(); i++) {
                        JSONArray records = appointmentRecords.getJSONArray(i);
                        String date = records.getString(0);
                        String timeStart = records.getString(1);
                        String timeEnd = records.getString(2);


                        pastText = (String) resultBox.getText();
                        String result = String.format("%s \n Date: %s Start: %s End: %s", pastText, date, timeStart, timeEnd);
                        resultBox.setText(result);
                    }



            } catch (JSONException e) {
                Log.e("JSON error", e.toString(), e);
            }


            

        }



    }


}
