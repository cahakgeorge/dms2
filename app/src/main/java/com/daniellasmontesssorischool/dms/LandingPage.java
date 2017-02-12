package com.daniellasmontesssorischool.dms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daniellasmontesssorischool.dms.app.Config;
import com.daniellasmontesssorischool.dms.service.MyFirebaseInstanceIDService;
import com.daniellasmontesssorischool.dms.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

public class LandingPage extends AppCompatActivity {

    MaterialDialog dialog;

    String logintype = "";

    SharedPreferences pref;

    Boolean mes = false;

    public static final String PREFS_NAME = "DmsPrefsFile";

    private static final String TAG = LandingPage.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    public void onBackPressed() {

        Fragment fragmentlanding = getSupportFragmentManager().findFragmentByTag("fraglanding");
        Fragment fragmentlogin = getSupportFragmentManager().findFragmentByTag("fraglogin");
        Fragment fragmentloginteacher = getSupportFragmentManager().findFragmentByTag("fragloginteacher");


        if (fragmentlogin != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentlogin).commit();

            Fragment frag = new FragmentLanding();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_select, frag, "fraglanding");
            fragmentTransaction.commit();

        }else if (fragmentloginteacher != null) {
            Fragment frag = new FragmentLanding();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_select, frag, "fraglanding");
            fragmentTransaction.commit();

        }else if (fragmentlanding != null) {
            /*getSupportFragmentManager().beginTransaction().remove(fragmentlogin).commit();
            Intent intent = new Intent(this, LandingPage.class);
            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else
                finish();*/
            confirmExit();
        } else
            confirmExit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Intent i = new Intent(this, MyFirebaseInstanceIDService.class);
        startService(i);
        /*Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();*/

        /*txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);*/

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic("global");//Config.TOPIC_GLOBAL

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    Log.e("News Addition", "PUSH NOTIFICATION "+message.toString());

                    dialog("DMS INFO", message);
                    //txtMessage.setText(message);
                }
            }
        };

        //displayFirebaseRegId();
        if(!mes){
            if(pref.contains("pref_prevlogin")) {
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
            }else if(pref.contains("pref_prevlogin_teacher")) {
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
            }else {
                Fragment frag = new FragmentLanding();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id._frame_select, frag, "fraglanding");
                fragmentTransaction.commit();
            }
        }

    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Log.e(TAG, "Firebase Reg Id: " + regId);
        else
            Log.e(TAG, "Firebase Reg Id is not received yet!");
    }

    public void dialog(String title, String body){
        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(body+"\n Continue to app?")
                .positiveText(R.string.exitPos)
                .negativeText(R.string.exitNeg)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(LandingPage.this, LandingPage.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        // TODO
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        } else
                            finish();
                    }
                })
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void confirmExit(){
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.exitAuth)
                .content(R.string.exitConf)
                .positiveText(R.string.exitPos)
                .negativeText(R.string.exitNeg)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        } else
                            finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public void progressbarStartLogin(){
        dialog = new MaterialDialog.Builder(this)
                .title("Authenticating")
                .content("Please Wait...")
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    public void progressbarStop(){
        dialog.dismiss();
    }

    public void alertNetErr(){
        dialog = new MaterialDialog.Builder(this)
                .title("Network")
                .content("Your network seems to be disabled. Please try again!")
                .positiveText("OK")
                .show();
    }

        public void alertInvalidErr(){
        dialog = new MaterialDialog.Builder(this)
                .title("Alert")
                .content("Incorrect details entered")
                .positiveText("OK")
                .show();
    }

    public void alertInvalidLoginCont(String content){
        dialog = new MaterialDialog.Builder(this)
                .title("Alert")
                .content(content)
                .positiveText("OK")
                .show();
    }

}
