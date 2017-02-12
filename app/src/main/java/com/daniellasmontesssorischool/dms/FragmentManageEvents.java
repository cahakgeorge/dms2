package com.daniellasmontesssorischool.dms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentManageEvents extends Fragment {

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";

    NetworkCheck netCheck;
    View rootView;

    MaterialDialog dialog;
    LinearLayout upload_field;
    CircleImageView chooseimage;
    MaterialSpinner _gender, _stclass;

    public static EditText _etitle, _edetail, _epartic, _edate, _evenue;

    String user="Mr Destiny", etitle, edetail, epartic, evenue, edate;
    Button upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_manage_events, container, false);
        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        if(((Home)getActivity()).getSupportActionBar() != null)
            ((Home)getActivity()).getSupportActionBar().setTitle("Manage Assignments");

        _etitle = (EditText)rootView.findViewById(R.id._etitle);
        _edetail = (EditText)rootView.findViewById(R.id._edetail);
        _epartic = (EditText)rootView.findViewById(R.id._eparticip);
        _edate = (EditText)rootView.findViewById(R.id._edate);

        _evenue = (EditText)rootView.findViewById(R.id._evenue);
        _edate.setClickable(false);
        _edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogTheme1();
                dialogfragment.show(getFragmentManager(), "Theme 1");
            }
        });


        //upload_field = (LinearLayout) rootView.findViewById(R.id._upload_field);
        upload = (Button)rootView.findViewById(R.id._upload_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(netCheck.isConnected()){
                    //user="Mr Destiny", etitle, edetail, epartic, evenue, edate;
                    etitle = _etitle.getText().toString().trim();
                    edetail = _edetail.getText().toString().trim();
                    epartic = _epartic.getText().toString().trim();
                    evenue = _evenue.getText().toString().trim();

                    edate = _edate.getText().toString().trim();
                    if(!edate.isEmpty() && !edate.equals(null)) {
                        edate = "" + getUnixTime(edate);
                    }

                    if(validate()){
                        addEvent();
                    }
                }else{
                    ((Home)getActivity()).alertNetErr();
                }
            }
        });

        return rootView;
    }

    private boolean validate(){
        boolean valid=true;
        //user="Mr Destiny", etitle, edetail, epartic, evenue, edate;
        //VALIDATE PHONE

        if(etitle.isEmpty()){
            _etitle.setError("Invalid Event Title");
            _etitle.requestFocus();
            valid=false;
        }else if(edetail.isEmpty()){
            _edetail.setError("Enter the details");
            _edetail.requestFocus();
            valid=false;
        }else if(epartic.isEmpty()){
            _epartic.setError("Enter target participants");
            _epartic.requestFocus();
            valid=false;
        }else if(evenue.isEmpty()){
            _evenue.setError("No venue entered");
            _evenue.requestFocus();
            valid=false;
        }else if(edate.isEmpty()){
            _edate.setError("No date entered");
            _edate.requestFocus();
            valid=false;
        }
        return valid;
    }

    void addEvent(){
        ((Home)getActivity()).progressbarStartAction("Uploading event","Wait while server performs request...");
        String tag_json_obj_event = "json_obj_req_addevent";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Assignment Add", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Event Addition", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("Event", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Event", response.toString());

                               /* Snackbar.make(rootView, "Assignment was successfully added!", Snackbar.LENGTH_LONG)
                                        .setAction("Result", null).show();*/

                                ((Home)getActivity()).dialogDisplay("Result", data.getString(TAG_SERVER_MSG));

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("Action couldn't be performed. Please try again! \n"+data.getString(TAG_SERVER_MSG), "addassignment");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "addEvent");
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
                ((Home)getActivity()).dialogError("Please try again!", "addEvent");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "4");
                params.put("euser", user);
                params.put("etitle", etitle);
                params.put("edetail", edetail);
                params.put("eparticipant", epartic);
                params.put("edate", edate);
                params.put("evenue", evenue);

                Log.e("Tag Event",params.toString());
                Log.e("Tag Event",params.toString());
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_event);
    }


    public static class DatePickerDialogTheme1 extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            @SuppressWarnings("ResourceType") DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            //Long getUnix = getUnixTime(day + "/" + (month+1) + "/" + year);
            _edate.setText(day + "/" + (month+1) + "/" + year);
        }
    }


    public static Long getUnixTime(String dateString){
        //DateFormat dfm = new SimpleDateFormat("d/MMM/yyyy");
        //String dateString = "Fri, 09 Nov 2012 23:40:18 GMT";
        DateFormat dateFormat = new SimpleDateFormat("d/MM/yy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTime = (long)date.getTime()/1000;

        return unixTime;
    }


}
