package com.daniellasmontesssorischool.dms;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;
import com.daniellasmontesssorischool.dms.utils.DatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQuiz extends Fragment {

    MaterialDialog dialog;

    RelativeLayout _english, _reasoning,_science,_geography, _history, _genknow;

    Button update;

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_QUIZQ="quiz";
    private static String TAG_VERSION="version";

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

       /* _english = (RelativeLayout)rootView.findViewById(R.id._english);
        _reasoning = (RelativeLayout)rootView.findViewById(R.id._reasoning);
        _science = (RelativeLayout)rootView.findViewById(R.id._science);
        _geography = (RelativeLayout)rootView.findViewById(R.id._geog);
        _history = (RelativeLayout)rootView.findViewById(R.id._history);*/

        _genknow = (RelativeLayout)rootView.findViewById(R.id._generalknowlede);
        update = (Button)rootView.findViewById(R.id._update_quiz);
        update .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuiz();
            }
        });

       /* CoordinatorLayout coordinatorLayout = (CoordinatorLayout)rootView.findViewById(R.id.coordinatorlayout);
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id._fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuiz();
            }
        });
*/
          /*_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ((Home)getActivity()).quiztype = "english";
                ((Home)getActivity()).quizcount = 0;
                ((Home)getActivity()).scorecount = 0;
                getquizoption();

                *//*Fragment frag = new FragmentQuizStart();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                fragmentTransaction.commit();*//*
            }
        });

        _reasoning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Home)getActivity()).quiztype = "reasoning";
                ((Home)getActivity()).quizcount = 0;
                ((Home)getActivity()).scorecount = 0;
                getquizoption();

               *//* Fragment frag = new FragmentQuizStart();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                fragmentTransaction.commit();*//*
            }
        });

        _science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Home)getActivity()).quiztype = "science";
                ((Home)getActivity()).quizcount = 0;
                ((Home)getActivity()).scorecount = 0;
                getquizoption();

               *//* Fragment frag = new FragmentQuizStart();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                fragmentTransaction.commit();*//*
            }
        });

        _geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Home)getActivity()).quiztype = "geography";
                ((Home)getActivity()).quizcount = 0;
                ((Home)getActivity()).scorecount = 0;
                getquizoption();

              *//*  Fragment frag = new FragmentQuizStart();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                fragmentTransaction.commit();*//*
            }
        });

        _history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Home)getActivity()).quiztype = "history";
                ((Home)getActivity()).quizcount = 0;
                ((Home)getActivity()).scorecount = 0;
                getquizoption();

               *//* Fragment frag = new FragmentQuizStart();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                fragmentTransaction.commit();*//*
            }
        });*/

        _genknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Home)getActivity()).quiztype = "general";
                ((Home)getActivity()).quizcount = 0;
                ((Home)getActivity()).scorecount = 0;

                ((Home)getActivity()).quizdiff = "easy";
                ((Home)getActivity()).getquiz();

                Fragment frag = new FragmentQuizStart();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                fragmentTransaction.commit();
                //getquizoption();

            }
        });

        return rootView;
    }


    public void updateQuiz(){
        dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.getQuizTit)
                .content(R.string.getQuizCon)
                .positiveText(R.string.exitPos)
                .negativeText(R.string.exitNeg)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //make request
                        //check for sharedpref value to see the value
                        String prevUpdateVersion = "0";
                        if(((Home)getActivity()).pref.contains("quizvers")){
                            prevUpdateVersion = ((Home)getActivity()).pref.getString("quizvers","0");
                        }

                        if(((Home)getActivity()).netCheck.isConnected()) {
                            //continue with update
                            getQuiz(prevUpdateVersion);
                        }else{
                            ((Home)getActivity()).dialogError("Your data connection seems to be unavailable. Please try again!", "fragmteachers2");
                        }
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

    /*public void getquizoption(){
        dialog = new MaterialDialog.Builder(getActivity())
                .title("Select Difficulty Level")
                .items(R.array.difficulty)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            ((Home)getActivity()).quizdiff = "easy";
                            ((Home)getActivity()).getquiz();

                            Fragment frag = new FragmentQuizStart();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                            fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                            fragmentTransaction.commit();
                        }else if (which == 1) {
                            ((Home)getActivity()).quizdiff = "hard";
                            ((Home)getActivity()).getquiz();

                            Fragment frag = new FragmentQuizStart();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                            fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                            fragmentTransaction.commit();
                        }else if (which == 2) {
                            ((Home)getActivity()).quizdiff = "challenge";
                            ((Home)getActivity()).getquiz();

                            Fragment frag = new FragmentQuizStart();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                            fragmentTransaction.replace(R.id._frame_home, frag, "fragquizstart");
                            fragmentTransaction.commit();
                        }

                    }
                })
                //.positiveText("Continue")
                .show();
    }
*/

    void getQuiz(final String qversion){
        ((Home)getActivity()).progressbarStartRetrieve("Quiz");
        String tag_json_obj_quiz = "json_obj_req_quiz";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Quiz Retrieval", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar
                        //mSwipeRefreshLayout.setRefreshing(false);

                        if(response != null){
                            Log.e("Quiz", "Not null "+response.toString());

                            DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
                            //db.clearData(4);


                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Quiz", response.toString());

                                if(db.addQuizDataOnline(data.getString(TAG_QUIZQ))) {

                                    //add shared pref value
                                    ((Home)getActivity()).pref.edit().putString("quizvers", data.getString(TAG_VERSION)).commit();

                                    //then show toast

                                    ((Home)getActivity()).dialogError("Quiz has been updated", "quiz");


                                }

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("No quiz updates available. Please check back later!", "quiz");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "quiz");
                        }
                    }else{ //response from server is null
                        ((Home) getActivity()).progressbarStop();
                        ((Home) getActivity()).dialogOutput("Retrieval Error", "Please check your data connection", false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ((Home)getActivity()).progressbarStop();
                //   Handle Error
                ((Home)getActivity()).dialogError("Please try again!", "quiz");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("version", qversion);
                params.put("api_target", "14");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_quiz);
    }

}
