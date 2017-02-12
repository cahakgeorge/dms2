package com.daniellasmontesssorischool.dms.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.daniellasmontesssorischool.dms.app.Config;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static final String PREFS_NAME = "DmsPrefsFile";

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    Context context;

    public MyFirebaseInstanceIDService(){
        //this.onTokenRefresh();
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
        this.onTokenRefresh();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
//        try {
//            subscribeTopics(refreshedToken);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        //LocalBroadcastManager.getInstance(getApplication().getApplicationContext()).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token){
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        /*SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();*/
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        /*for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }*/
        pubSub.subscribe(token, "/topics/global", null);
    }

}