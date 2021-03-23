package com.enriquegh.marshealth;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.WritableMessage;



public class MessageService extends Service implements SinchClientListener {
    private static final String APP_KEY = "5d06c12b-32a9-4b43-95f3-3fdd1e2b1902";
    private static final String APP_SECRET = "eA+Nf7aCuEeoF2Acr79iMA==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    private final MessageServiceInterface serviceInterface = new MessageServiceInterface();
    private SinchClient sinchClient = null;
    private MessageClient messageClient;
    private String currentUserId = null;

    private LocalBroadcastManager broadcaster;
    private Intent broadcastIntent = new Intent("com.enriquegh.marshealth.MainActivity");

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get the current user id from Parse
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        currentUserId = prefs.getString("username","DEFAULT_NO");
        Log.d("currentUserIDMessServ",currentUserId);
        if (currentUserId != null && !isSinchClientStarted()) {
            startSinchClient(currentUserId);
        }
        else {
        }
        broadcaster = LocalBroadcastManager.getInstance(this);
        return super.onStartCommand(intent, flags, startId);
    }
    public void startSinchClient(String username) {
        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(username)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();
        //this client listener requires that you define
        //a few methods below
        sinchClient.addSinchClientListener(this);
        //messaging is "turned-on", but calling is not
        sinchClient.setSupportMessaging(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.checkManifest();
        sinchClient.start();
    }
    private boolean isSinchClientStarted() {
        return sinchClient != null && sinchClient.isStarted();
    }
    //The next 5 methods are for the sinch client listener
    @Override
    public void onClientFailed(SinchClient client, SinchError error) {
        Log.d("onclientFailed",error.getMessage());
        sinchClient = null;
        broadcastIntent.putExtra("success", false);
        broadcaster.sendBroadcast(broadcastIntent);
    }
    @Override
    public void onClientStarted(SinchClient client) {
        client.startListeningOnActiveConnection();
        Log.d("DEBUG", "onclientstarted was called");
        messageClient = client.getMessageClient();
        Log.d("sinchclientstarted",Boolean.toString(sinchClient != null));
        Log.d("messageClientstarted",Boolean.toString(messageClient != null));
        broadcastIntent.putExtra("success", true);
        broadcaster.sendBroadcast(broadcastIntent);
    }
    @Override
    public void onClientStopped(SinchClient client) {
        Log.d("ClientStopped", "sinch client got stopped");
        //sinchClient = null;
    }
    @Override
    public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration) {}
    @Override
    public void onLogMessage(int level, String area, String message) {}
    @Override
    public IBinder onBind(Intent intent) {
        return serviceInterface;
    }
    public void sendMessage(String recipientUserId, String textBody) {
        boolean statement = messageClient != null;
        String clientIsStarted = Boolean.toString(statement);
        Log.e("sendmessageClient", clientIsStarted);
        if (messageClient != null) {
            WritableMessage message = new WritableMessage(recipientUserId, textBody);
            messageClient.send(message);
        }
        else {
            Log.e("ERROR", "Message was not sent!");
        }
    }
    public void addMessageClientListener(MessageClientListener listener) {
        if (messageClient != null) {
            messageClient.addMessageClientListener(listener);
        }
    }
    public void removeMessageClientListener(MessageClientListener listener) {
        if (messageClient != null) {
            messageClient.removeMessageClientListener(listener);
        }
    }
    @Override
    public void onDestroy() {
        if (sinchClient != null) {
            sinchClient.stopListeningOnActiveConnection();
            sinchClient.terminate();
        }

    }
    //public interface for ListUsersActivity & MessagingActivity
    public class MessageServiceInterface extends Binder {
        public void sendMessage(String recipientUserId, String textBody) {
            MessageService.this.sendMessage(recipientUserId, textBody);
        }
        public void addMessageClientListener(MessageClientListener listener) {
            MessageService.this.addMessageClientListener(listener);
        }
        public void removeMessageClientListener(MessageClientListener listener) {
            MessageService.this.removeMessageClientListener(listener);
        }
        public boolean isSinchClientStarted() {
            return MessageService.this.isSinchClientStarted();
        }
    }
}
