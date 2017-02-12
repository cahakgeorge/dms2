package com.daniellasmontesssorischool.dms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class FragmentManageAssignments extends Fragment {

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

    public static EditText _qtitle, _question, _extra, _subject, _subm_date;

    String teacher, st_class="", qtitle, question, extra, subject, subm_date;
    Button upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_manage_assignments, container, false);
        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        if(((Home)getActivity()).getSupportActionBar() != null)
            ((Home)getActivity()).getSupportActionBar().setTitle("Manage Assignments");

        _qtitle = (EditText)rootView.findViewById(R.id._qtitle);
        _question = (EditText)rootView.findViewById(R.id._question);
        _extra = (EditText)rootView.findViewById(R.id._extra);
        _subject = (EditText)rootView.findViewById(R.id._subject);

        _subm_date = (EditText)rootView.findViewById(R.id._subm_date);
        _subm_date.setClickable(false);
        _subm_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogTheme1();
                dialogfragment.show(getFragmentManager(), "Theme 1");
            }
        });

        _stclass = (MaterialSpinner)rootView.findViewById(R.id._class);
        _stclass.setItems("Year 1", "Year 2", "Year 3", "Year 4", "Year 5");
        _stclass.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        st_class = "Year 1";
                        break;
                    case 1:
                        st_class = "Year 2";
                        break;
                    case 2:
                        st_class = "Year 3";
                        break;
                    case 3:
                        st_class = "Year 4";
                        break;
                    case 4:
                        st_class = "Year 5";
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(rootView, "Please select student class", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getActivity().getApplicationContext(), "Please select an article type", Toast.LENGTH_LONG).show();
            }

        });

        //upload_field = (LinearLayout) rootView.findViewById(R.id._upload_field);
        upload = (Button)rootView.findViewById(R.id._upload_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(netCheck.isConnected()){
                    //addname, st_class, gender
                    teacher = "Mr destiny";
                    qtitle = _qtitle.getText().toString().trim();
                    question = _question.getText().toString().trim();
                    extra = _extra.getText().toString().trim();
                    subject = _subject.getText().toString().trim();

                    subm_date = _subm_date.getText().toString().trim();
                    if(!subm_date.isEmpty() && !subm_date.equals(null)) {
                        subm_date = "" + getUnixTime(subm_date);
                    }

                    if(validate()){
                        addAssignment();
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
        //teacher, st_class="", qtitle, question, extra, subject, subm_date;
        //VALIDATE PHONE

        if(qtitle.isEmpty()){
            _qtitle.setError("Invalid Question Title");
            _qtitle.requestFocus();
            valid=false;
        }else if(question.isEmpty()){
            _question.setError("Invalid question entered");
            _question.requestFocus();
            valid=false;
        }else if(extra.isEmpty()){
            _extra.setError("Invalid extra details entered");
            _extra.requestFocus();
            valid=false;
        }else if(st_class.isEmpty()){
            Snackbar.make(rootView, "Select class of student", Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            _stclass.requestFocus();
            valid=false;
        }else if(subject.isEmpty()){
            _subject.setError("Enter your email address");
            _subject.requestFocus();
            valid=false;
        }else if(subm_date=="" || subm_date==null){
            _subm_date.setError("Enter your email address");
            _subm_date.requestFocus();
            valid=false;
        }
        return valid;
    }

    void addAssignment(){
        ((Home)getActivity()).progressbarStartAction("Uploading assignment","Wait while server performs request...");
        String tag_json_obj_assignment = "json_obj_req_addassignment";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Assignment Add", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Assignment Addition", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("Assignment", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Assignment", response.toString());

                               /* Snackbar.make(rootView, "Assignment was successfully added!", Snackbar.LENGTH_LONG)
                                        .setAction("Result", null).show();*/

                                ((Home)getActivity()).dialogDisplay("Result", data.getString(TAG_SERVER_MSG));

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("Action couldn't be performed. Please try again! \n"+data.getString(TAG_SERVER_MSG), "addassignment");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "addassignment");
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
                ((Home)getActivity()).dialogError("Please try again!", "addassignment");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "3");
                params.put("qtitle", qtitle);
                params.put("question", question);
                params.put("extra", extra);
                params.put("class", st_class);
                params.put("subject", subject);
                params.put("teacher", teacher);
                params.put("subm_date", subm_date);

                Log.e("Tag Assignment",params.toString());
                Log.e("Tag Assignment",params.toString());
                Log.e("Tag Assignment",params.toString());
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_assignment);
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
            _subm_date.setText(day + "/" + (month+1) + "/" + year);
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
