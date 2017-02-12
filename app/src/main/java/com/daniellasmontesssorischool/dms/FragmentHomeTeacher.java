package com.daniellasmontesssorischool.dms;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeTeacher extends Fragment {
    private ImageView imageView;
    private CircleImageView imgv;
    private ImageLoader mImageLoader;
    private RequestQueue myqueue;

    String username="";
    String password ="";

    EditText _user, _pass;

    TextView _teachername;
    Button _teacherpost;

    LinearLayout _ass, _test, _art;

    private static String TAG_SUCCESS = "success";
    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";


    String tag_json_obj_image = "json_req_homeimg";

    NetworkCheck netCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_teacher, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        //imageView = (ImageView)rootView.findViewById(R.id._imageView);

        //volleyimage fetched from dms server
        imgv = (CircleImageView)rootView.findViewById(R.id.circleView);

        loadUserImage();

        _teachername = (TextView)rootView.findViewById(R.id._teacher_name);
        _teacherpost = (Button)rootView.findViewById(R.id._teachpost);

        _teachername.setText(((Home)getActivity()).USER_FULLNAME);
        _teacherpost.setText(((Home)getActivity()).USER_CLASS);

        _ass = (LinearLayout)rootView.findViewById(R.id._ass);
        _test = (LinearLayout)rootView.findViewById(R.id._ontest);
        _art = (LinearLayout)rootView.findViewById(R.id._article);

        _ass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new FragmentAssignment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id._frame_home, frag, "fragassignment");
                fragmentTransaction.commit();
            }
        });

        _art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((LandingPage)getActivity()).logintype="student";
                Fragment frag = new FragmentArticle();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id._frame_home, frag, "fragarticle");
                fragmentTransaction.commit();
            }
        });

        /*_user = (EditText)rootView.findViewById(R.id._username);
        _pass = (EditText)rootView.findViewById(R.id._password);


        //Auto fill phone field
        if(((LandingPage)getActivity()).pref.contains(PREF_PREVIOUSLOGIN)){
            _user.setText(((LandingPage)getActivity()).pref.getString(PREF_PREVIOUSLOGIN, null));
        }

       *//* if(((LandingPage)getActivity()).logintype.equalsIgnoreCase("teacher")){
            _user.setHint("Teacher Id");
        }else{
            _user.setHint("Student Id");
        }*//*

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
        });*/

        return rootView;
    }

    private void  loadUserImage(){

        //Image URL - This can point to any image file supported by Android
        final String url = "" + Const.imagelink + "" + ((Home)getActivity()).USER_IMAGEID + ".png";

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        // If you are using normal ImageView
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOAD IMAGE", "Image Load Error: " + error.getMessage());
                //imgv.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.cahak2));
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    imgv.setImageBitmap(response.getBitmap());
                }
            }
        });

        Log.e("Tag LOADING IMAGE",url + "");
        Log.e("Tag LOADING IMAGE",url + "");
        Log.e("Tag LOADING IMAGE",url + "");
    }


    /*private void userLogin() {

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


                                ((LandingPage)getActivity()).pref
                                        .edit().putString(PREF_LOGINAME, data.getString(TAG_FULLNAME))
                                        .putString(PREF_LOGINSEX, data.getString(TAG_SEX))
                                        .putString(PREF_LOGINPHONE, TAG_PHONE)
                                        .putString(PREF_LOGINCLASS, TAG_CLASS)
                                        .putString(PREF_LOGINEMAIL, data.getString(TAG_EMAIL))
                                        .putString(PREF_IMAGEID, data.getString(TAG_IMAGEID))
                                        .putString(PREF_HEADTEACHER, TAG_HEADTEACHER)
                                        .putString(PREF_IMAGEID_HEADT, TAG_IMAGEID_HEADT)
                                        .putString(PREF_IMAGEID_PROP, data.getString(TAG_IMAGEID_PROPR))
                                        .putString(PREF_PREVIOUSLOGIN, username).commit();


                                Intent intent = new Intent(getActivity(), Home.class);
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
                        ((LandingPage)getActivity()).alertInvalidLoginCont("Please check your data connection!");
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
                ((LandingPage)getActivity()).alertInvalidLoginCont("Volley Error");

                Log.e("Tag LOGIN VOLLEY ERROR", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("regno", username);
                params.put("pass", password);
                params.put("type", ((LandingPage)getActivity()).logintype);
                params.put("api_target", "1");

                Log.e("Tag LOGIN PARAMS", params.toString());
                *//*long unixTime = System.currentTimeMillis() / 1000L;

                String word = md5(pword)+""+unixTime;
                params.put("msisdn", base(phn));
                params.put("savekey", base(word));
                params.put("password", base(word+md5(phn))+""+unixTime);
                params.put("api_location", "2");*//*

                return params;
            }

        };

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_login);

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
    }*/



}
