package com.daniellasmontesssorischool.dms;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLogin extends Fragment {
    String username="";
    String password ="";

    EditText _user, _pass;
    ImageButton _login;


    private static String TAG_SUCCESS = "success";
    private static String TAG_FULLNAME="name";
    private static String TAG_SEX="sex";
    private static String TAG_PHONE="phone";
    private static String TAG_CLASS="class";
    private static String TAG_EMAIL="email";

    private static String TAG_IMAGEID="imageid";

    private static String TAG_TERM = "term";
    private static String TAG_WEEK="week";
    private static String TAG_YEAR="year";

    private static String TAG_HEADTEACHER="headteacher";
    private static String TAG_IMAGEID_HEADT="headteacherimageid";
    private static String TAG_IMAGEID_PROPR="proprietressimageid";
    private static String TAG_USERTYPE="";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";

    private static String PREF_LOGINAME="pref_login_name";
    private static String PREF_LOGINSEX = "pref_login_sex";
    private static String PREF_LOGINPHONE = "pref_login_phone";
    private static String PREF_LOGINCLASS = "pref_login_class";
    private static String PREF_LOGINEMAIL="pref_login_email";

    private static String PREF_TERM = "term";
    private static String PREF_WEEK="week";
    private static String PREF_YEAR="year";

    private static String PREF_USERTYPE="usertype";

    private static String PREF_IMAGEID="pref_login_imageid";
    private static String PREF_HEADTEACHER = "pref_login_headteacher";

    private static String PREF_IMAGEID_HEADT = "pref_login_imghead";
    private static String PREF_IMAGEID_PROP = "pref_login_imgprop";

    private static String PREF_PREVIOUSLOGIN = "pref_prevlogin";


    String tag_json_obj_login = "json_req_login";

    NetworkCheck netCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        _user = (EditText)rootView.findViewById(R.id._username);
        _pass = (EditText)rootView.findViewById(R.id._password);

        _user.requestFocus();
        //Auto fill phone field
       /* if(((LandingPage)getActivity()).pref.contains(PREF_PREVIOUSLOGIN)){
            _user.setText(((LandingPage)getActivity()).pref.getString(PREF_PREVIOUSLOGIN, null));
            _pass.setText("123456789");
        }*/

       /* if(((LandingPage)getActivity()).logintype.equalsIgnoreCase("teacher")){
            _user.setHint("Teacher Id");
        }else{
            _user.setHint("Student Id");
        }*/

        _login = (ImageButton)rootView.findViewById(R.id._signin_button);

        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((LandingPage)getActivity()).logintype="student";

                username = _user.getText().toString().trim();
                password = _pass.getText().toString().trim();

                if (validate()) {
                    if (netCheck.isConnected()) {
                        //log in user
                        userLogin();
                    } else {
                        ((LandingPage)getActivity()).alertNetErr();
                    }
                }
            }
        });

        return rootView;
    }



    private void userLogin() {

        ((LandingPage)getActivity()).progressbarStartLogin();

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {

                        Log.e("Tag LOGIN SUCCESS", response.toString());
                        Log.e("Tag LOGIN SUCCESS", response.toString());
                        Log.e("Tag LOGIN SUCCESS", response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        //String resp = data.getString(TAG_SUCCESS);

                        if(data.getString(TAG_SUCCESS) != null){
                            Log.e("Tag LOGIN SUCCESS", response.toString());

                            String res = data.getString(TAG_SUCCESS);

                            if (res.equalsIgnoreCase("1")) {
                                ((LandingPage)getActivity()).progressbarStop();

                                clearPrefTeachers();

                                ((LandingPage)getActivity()).pref
                                        .edit().putString(PREF_LOGINAME, data.getString(TAG_FULLNAME))
                                        .putString(PREF_LOGINSEX, data.getString(TAG_SEX))
                                        .putString(PREF_LOGINPHONE, data.getString(TAG_PHONE))
                                        .putString(PREF_LOGINCLASS,  data.getString(TAG_CLASS))
                                        .putString(PREF_LOGINEMAIL, data.getString(TAG_EMAIL))
                                        .putString(PREF_IMAGEID, data.getString(TAG_IMAGEID))
                                        .putString(PREF_TERM,  data.getString(TAG_TERM))
                                        .putString(PREF_WEEK, data.getString(TAG_WEEK))
                                        .putString(PREF_YEAR, data.getString(TAG_YEAR))
                                        //.putString(PREF_HEADTEACHER, data.getString(TAG_HEADTEACHER))
                                        //.putString(PREF_IMAGEID_PROP, data.getString(TAG_IMAGEID_PROPR))
                                        //.putString(PREF_USERTYPE, data.getString(TAG_USERTYPE))
                                        .putString(PREF_PREVIOUSLOGIN, username).commit();

                                Intent intent = null;
                                intent = new Intent(getActivity(), Home.class);
                                /*if(data.getString(TAG_USERTYPE) == null || data.getString(TAG_USERTYPE) == "student")
                                    intent = new Intent(getActivity(), Home.class);
                                else if(data.getString(TAG_USERTYPE) == "teacher")
                                    intent = new Intent(getActivity(), ManageApp.class);
                                else if(data.getString(TAG_USERTYPE) == "admin")
                                    intent = new Intent(getActivity(), ManageStudents.class);
*/
                                startActivity(intent);
                                getActivity().finish();

                            }else{
                                ((LandingPage)getActivity()).progressbarStop();

                                _login.setBackgroundColor(getResources().getColor(R.color.dmsPink));

                                JSONObject data_err = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                                ((LandingPage)getActivity()).alertInvalidLoginCont(data.getString(TAG_SERVERMSG));

                                Log.e("Tag LOGIN ERROR",data.getString(TAG_SERVERMSG));
                                Log.e("Tag LOGIN ERROR",data.getString(TAG_SERVERMSG));
                                Log.e("Tag LOGIN ERROR",data.getString(TAG_SERVERMSG));

                            }
                        }else{
                            ((LandingPage)getActivity()).progressbarStop();
                            ((LandingPage)getActivity()).alertInvalidErr();
                        }
                    }else{ //response from server is null
                        ((LandingPage)getActivity()).progressbarStop();
                        ((LandingPage)getActivity()).alertInvalidLoginCont("Please, check your data connection!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ((LandingPage)getActivity()).progressbarStop();
                //   Handle Error
                ((LandingPage)getActivity()).alertInvalidLoginCont("Please check your data connection!");

                Log.e("Tag LOGIN VOLLEY ERROR", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("regno", username);
                params.put("pass", password);
                params.put("type", "student");
                params.put("api_target", "1");

                Log.e("Tag LOGIN PARAMS", params.toString());
                /*long unixTime = System.currentTimeMillis() / 1000L;

                String word = md5(pword)+""+unixTime;
                params.put("msisdn", base(phn));
                params.put("savekey", base(word));
                params.put("password", base(word+md5(phn))+""+unixTime);
                params.put("api_location", "2");*/

                return params;
            }

        };

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_login);

    }

    void clearPrefTeachers(){
        if(((LandingPage)getActivity()).pref.contains("pref_prevlogin_teacher")){
            ((LandingPage)getActivity()).pref.edit()
                    .remove("pref_login_name_teacher")
                    .remove("pref_login_sex_teacher")
                    .remove("pref_position_teacher")
                    .remove("pref_login_username")
                    .remove("usertype_teacher")
                    .remove("pref_startdate_teacher")
                    .remove("pref_prevlogin_teacher")
                    .commit();
        }

    }

    private boolean validate(){
        boolean valid=true;
        //VALIDATE PHONE
        if (username.isEmpty() || username.length()<4){
            _user.setError("Incorrect student id!");
            valid=false;
        }

        if(password.isEmpty()){
            _pass.setError("Enter correct password");
            valid=false;
        }
        return valid;
    }


    private static String md5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String base(String text){

        byte[] data = null;
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1){
            e1.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        return base64;
    }

}
