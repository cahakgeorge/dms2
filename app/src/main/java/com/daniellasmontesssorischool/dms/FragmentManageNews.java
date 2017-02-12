package com.daniellasmontesssorischool.dms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentManageNews extends Fragment {

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

    public static EditText _ntitle, _ndetail, _ntype;

    String user="Mr destiny", title, detail, type;
    Button upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_manage_news, container, false);
        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());


        _ntitle = (EditText)rootView.findViewById(R.id._ntitle);
        _ndetail = (EditText)rootView.findViewById(R.id._ndetail);
        _ntype = (EditText)rootView.findViewById(R.id._ntype);

        //upload_field = (LinearLayout) rootView.findViewById(R.id._upload_field);
        upload = (Button)rootView.findViewById(R.id._upload_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(netCheck.isConnected()){
                    //addname, st_class, gender
                    user = "Mr destiny";
                    title = _ntitle.getText().toString().trim();
                    detail = _ndetail.getText().toString().trim();
                    type = _ntype.getText().toString().trim();

                    if(validate()){
                        addNews();
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
        //teacher, ntitle, ndetail, ntype;
        //VALIDATE PHONE

        if(title.isEmpty()){
            _ntitle.setError("Invalid news Title");
            _ntitle.requestFocus();
            valid=false;
        }else if(detail.isEmpty()){
            _ndetail.setError("Invalid news entered");
            _ndetail.requestFocus();
            valid=false;
        }else if(type.isEmpty()){
            _ntype.setError("Invalid news type entered");
            _ntype.requestFocus();
            valid=false;
        }
        return valid;
    }

    void addNews(){
        ((Home)getActivity()).progressbarStartAction("Uploading news","Wait while server performs request...");
        String tag_json_obj_news = "json_obj_req_addnews";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("News Add", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("News Addition", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("News", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag News", response.toString());

                               /* Snackbar.make(rootView, "Assignment was successfully added!", Snackbar.LENGTH_LONG)
                                        .setAction("Result", null).show();*/

                                ((Home)getActivity()).dialogDisplay("Result", data.getString(TAG_SERVER_MSG));

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("Action couldn't be performed. Please try again! \n"+data.getString(TAG_SERVER_MSG), "addassignment");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "addnews");
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
                ((Home)getActivity()).dialogError("Please try again!", "addnews");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "4.1");
                params.put("nuser", user);
                params.put("nhead", title);
                params.put("ndetail", detail);
                params.put("ntype", type);

                Log.e("Tag News",params.toString());
                Log.e("Tag News",params.toString());
                Log.e("Tag News",params.toString());
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_news);
    }

}
