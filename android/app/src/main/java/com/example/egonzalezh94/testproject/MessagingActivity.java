/*
package com.example.egonzalezh94.testproject;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    private String recipientId;
    private EditText messageBodyField;
    private String messageBody;
    private MessageService.MessageServiceInterface messageService;
    private String currentUserId;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();
    private ListView messagesList;
    private MessageAdapter messageAdapter;
    private EditText mTxtRecipient;
    private Spinner mLstRecipient;

    static final String API_URL = "http://mars.cs.usfca.edu/api.php/";
    static final String STAFF_URL = "staff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        new RetrieveRecipients().execute();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

        //get recipientId from the intent
        //mTxtRecipient = (EditText) findViewById(R.id.txtRecipient);
        mLstRecipient = (Spinner) findViewById(R.id.listRecipient);

        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.listRecipientArray, android.R.layout.simple_spinner_dropdown_item);
        new RetrieveRecipients().execute();
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //mLstRecipient.setAdapter(adapter);
        //mLstRecipient.setPrompt("Hello");



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        currentUserId = prefs.getString("username","DEFAULT_NO");

        messageBodyField = (EditText) findViewById(R.id.txtTextBody);

        messagesList = (ListView) findViewById(R.id.lstMessages);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);
        //listen for a click on the send button
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send the message!
                //String recipient = mTxtRecipient.getText().toString();
                String recipient = mLstRecipient.getSelectedItem().toString();
                messageBody = messageBodyField.getText().toString();
                if (messageBody.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a message", Toast.LENGTH_LONG).show();
                    return;
                }
                messageService.sendMessage(recipient, messageBody);
                Log.d("DEBUG",messageBody);
                messageBodyField.setText("");
            }
        });
    }
    //unbind the service when the activity is destroyed
    @Override
    public void onDestroy() {
        unbindService(serviceConnection);
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageService = (MessageService.MessageServiceInterface) iBinder;
            messageService.addMessageClientListener(messageClientListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageService = null;
            messageService.removeMessageClientListener(messageClientListener);
        }
    }

    private class MyMessageClientListener implements MessageClientListener {

        //Notify the user if their message failed to send
        @Override
        public void onMessageFailed(MessageClient client, Message message,
                                    MessageFailureInfo failureInfo) {
            Toast.makeText(MessagingActivity.this, "Message failed to send.", Toast.LENGTH_LONG).show();
        }


        @Override
        public void onIncomingMessage(MessageClient client, Message message) {
            //Display an incoming message
            //if (message.getSenderId().equals(recipientId)) {
                //WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
                messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
            //}
        }

        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {
            //Display the message that was just sent

            //Later, I'll show you how to store the
            //message in Parse, so you can retrieve and
            //display them every time the conversation is opened
            //WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
        }

        //Do you want to notify your user when the message is delivered?
        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}

        //Don't worry about this right now
        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {}
    }

    class RetrieveRecipients extends AsyncTask<Void, Void, String> {

        ArrayList<String> options=new ArrayList<>();


        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            //resultBox.setText("");
        }

        protected String doInBackground(Void... params) {
            //String dateStart = params[0], dateEnd = params[1];
            */
/**
             * This filter string will filter the results in three ways:
             * 1. Send only results that have a status of 0 (meaning status is available)
             * 2. Where the date is equal or greater than the start date.
             * 3. Where the date is equal or less than the end date.
             *//*


            try {
                //For now I believe this is insecure since the filter parameter is passed
                //within the URL and could be manipulated.
                //TODO: Figure out how to send parameter through request body
                URL url = new URL(API_URL + STAFF_URL);
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
                response = "THERE WAS AN ERROR ON RETRIEVECLIENTS";
            }
            //progressBar.setVisibility(View.GONE);
            try {
                String pastText;

                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject appointments = object.getJSONObject("staff");
                JSONArray recordsList = appointments.getJSONArray("records");

                for (int i = 0; i < recordsList.length(); i++) {
                    JSONArray records = recordsList.getJSONArray(i);
                    String fName = records.getString(1);
                    String lName = records.getString(2);
                    String userId = records.getString(3);

                    options.add(userId);
                    Log.d("OPTIONS",userId);

                }
                ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter(MessagingActivity.this,android.R.layout.simple_spinner_dropdown_item,options);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mLstRecipient.setAdapter(adapter);

            } catch (JSONException e) {
                Log.e("JSON error", e.toString(), e);
//            }
            }

        }



    }

}
*/
