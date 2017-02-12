package com.daniellasmontesssorischool.dms;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentArticleUpload extends Fragment {
    private NetworkImageView mNetworkImageView;
    private CircleImageView imgv;
    private ImageLoader mImageLoader;
    private RequestQueue myqueue;

    String article;

    EditText _title, _type, _content;

    TextView _student_name,  date;
    Button _student_class, send_button;

    LinearLayout _ass, _test, _art;

    private static String TAG_SUCCESS = "success";
    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";


    String tag_json_obj_image = "json_req_homeimg";

    NetworkCheck netCheck;

    MaterialSpinner _articleType;
    View rootView;
    String title, type, content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upload_article, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // _title, _type, _content;
        _title = (EditText)rootView.findViewById(R.id._title2);
        _content = (EditText)rootView.findViewById(R.id._content);

        _student_name = (TextView)rootView.findViewById(R.id._name);
        _student_name.setText(((Home)getActivity()).USER_FULLNAME);

        date = (TextView)rootView.findViewById(R.id._date);
        date.setText(day + "/" + (month+1) + "/" + year);

        _student_class = (Button)rootView.findViewById(R.id._upload_button);
        _student_class.setText(((Home)getActivity()).USER_CLASS);

        send_button = (Button)rootView.findViewById(R.id._send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(String title, type, content;)
                title = _title.getText().toString();
                content = _content.getText().toString();

                if(title!=null && title != "" && type!=null && type!="" && !type.equalsIgnoreCase("Select article type") && content!=null && netCheck.isConnected() && content != "" && content.length() >= 20){
                    addArticle();
                }else if(!netCheck.isConnected()){
                    Snackbar.make(rootView, "Please check your data connection", Snackbar.LENGTH_LONG).show();
                }else if(type!=null || type!="" || !type.equalsIgnoreCase("Select article type")){
                    Snackbar.make(rootView, "Please select article type", Snackbar.LENGTH_LONG).show();
                }else if(title!=null || title.equalsIgnoreCase("") ){
                    Snackbar.make(rootView, "Please select article type", Snackbar.LENGTH_SHORT).show();
                    _title.requestFocus();
                }else if(content!=null || content.equalsIgnoreCase("") || content.length() < 20 ){
                    Snackbar.make(rootView, "Please enter valid article content", Snackbar.LENGTH_SHORT).show();
                    _content.requestFocus();
                }else{
                    Snackbar.make(rootView, "Please input correct values", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        _articleType = (MaterialSpinner)rootView.findViewById(R.id.spinner);
        _articleType.setItems("Select article type","Essay", "Poem", "Debate", "Short Story", "Others");
        _articleType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        type = "Select article type";
                        break;
                    case 1:
                        type = "Essay";
                        break;
                    case 2:
                        type = "Poem";
                        break;
                    case 3:
                        type = "Debate";
                        break;
                    case 4:
                        type = "Short story";
                        break;
                    case 5:
                        type = "Others";
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(rootView, "Please select an article type", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getActivity().getApplicationContext(), "Please select an article type", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }


    void addArticle(){
        ((Home)getActivity()).progressbarStartAction("Uploadinging Article","uploading...");
        String tag_json_obj_addarticle = "json_obj_req_addarticle";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Articles Add", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Article Addition", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("Articles", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Articles", response.toString());

                                Snackbar.make(rootView, "Article was successfully added!", Snackbar.LENGTH_LONG)
                                        .setAction("Article", null).show();

                                ((Home)getActivity()).dialogDisplay("Result","Article was successfully added!");

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("Article couldn't be uploaded. Please try again!", "addarticle");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please try again in a few seconds..", "addarticle");
                        }
                    }else{ //response from server is null
                        ((Home) getActivity()).progressbarStop();
                        ((Home) getActivity()).dialogOutput("Uploading Error", "Please check your data connection", false);
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
                ((Home)getActivity()).dialogError("Please try again!", "add article");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "5");
                //$_POST['title'], $_POST['type'], $_POST['content'], $_POST['addedby'], $_POST['stud_class']

                params.put("title", title);
                params.put("type", type);
                params.put("content", content);
                params.put("addedby", ((Home)getActivity()).USER_FULLNAME);
                params.put("stud_class", ((Home)getActivity()).USER_CLASS);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_addarticle);
    }


}
