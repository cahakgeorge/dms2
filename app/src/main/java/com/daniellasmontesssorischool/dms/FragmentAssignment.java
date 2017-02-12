package com.daniellasmontesssorischool.dms;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;
import com.daniellasmontesssorischool.dms.utils.DatabaseHandler;
import com.daniellasmontesssorischool.dms.utils.RecyclerViewAdapterAssignment;
import com.daniellasmontesssorischool.dms.utils.RowItemsAssignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAssignment extends Fragment {

    ArrayList<RowItemsAssignment> histlist = new ArrayList<>();
    //private static final String TAG_DATE = "dates";
    private static final String TAG_SUBMDATE = "subm_date";
    private static final String TAG_SUBJTITLE = "subject";
    private static final String TAG_QUESTITLE = "questitle";
    private static final String TAG_QDETAIL = "question";
    private static final String TAG_TEACHER = "teacher_id";

    private static final String TAG_CLASS = "class";
    private static final String TAG_EXTRA = "extra_info";

    RecyclerView recyclerView;
    ArrayList<RowItemsAssignment> itemsList = new ArrayList<>();
    RecyclerViewAdapterAssignment adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Button _upload;

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";


    NetworkCheck netCheck;
    View rootView;
    String student_class = "";
    MaterialDialog dialog;
    TextView iniLabel,term,termyear,week;
    View vi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_assignment, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_assignment);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if(((Home)getActivity()).netCheck.isConnected()) {
                    getAssignment();
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "fragmteachers2");
                }
            }
        });


        //((Home)getActivity()).toolbarTitle.setText("Assignment");

        term = (TextView)rootView.findViewById(R.id._term);
        termyear = (TextView)rootView.findViewById(R.id._term_year);
        week = (TextView)rootView.findViewById(R.id._week);
        //vi = (View)rootView.findViewById(R.id._ini_view);

        term.setText(((Home)getActivity()).TERM);
        termyear.setText(((Home)getActivity()).YEAR);
        week.setText(((Home)getActivity()).WEEK);

        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new SlideInUpAnimator());

        //getArticles();
        getAssignmentFromDb();
        return rootView;
    }

    void getUserClass(){
            /*dialog = new MaterialDialog.Builder(getActivity())
                    .title("Select your current class")
                    .items(R.array.stclass)
                    .cancelable(false)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which == 0) {
                                student_class = "Year 1";
                                getAssignmentFromDb(student_class);
                            }else if (which == 1) {
                                student_class = "Year 2";
                                getAssignmentFromDb(student_class);
                            }else if (which == 3) {
                                student_class = "Year 3";
                                getAssignmentFromDb(student_class);
                            }else if (which == 4) {
                                student_class = "Year 4";
                                getAssignmentFromDb(student_class);
                            }else if (which == 5) {
                                student_class = "Year 5";
                                getAssignmentFromDb(student_class);
                            }

                        }
                    })
                    //.positiveText("Continue")
                    .show();
*/
    }

    void getAssignmentFromDb() {
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        Cursor cursor = db.getAssignmentData();

        try {
            Log.e("DATA RETRIEVAL", "CURSOR DATA FROM DB"+cursor.toString());

            Log.e("Response", "JSON RESPONSE TO DB"+cursor.toString());

            int count = 1;
            int j = 1;
            // Move to first row
            if(cursor.moveToFirst() && !cursor.toString().equals(null) || cursor.toString().length() > 60) {
                do {
                    // Storing JSON item in a Variable
                    String subdate = cursor.getString(cursor.getColumnIndex("assignment_subm"));
                    subdate = getCurrentTimeStamp(subdate);
                    String title = cursor.getString(cursor.getColumnIndex("assignment_subject"));
                    String questitle = cursor.getString(cursor.getColumnIndex("assignment_title"));
                    String content = cursor.getString(cursor.getColumnIndex("assignment_detail"));
                    String teacher = cursor.getString(cursor.getColumnIndex("assignment_teacher"));
                    String initial = title.substring(0,1) + "+";
                    String extra = cursor.getString(cursor.getColumnIndex("assignment_extra"));

                    String cla = cursor.getString(cursor.getColumnIndex("assignment_class"));


                    RowItemsAssignment items = new RowItemsAssignment();
                    items.setDate(subdate);
                    items.setTitle(title);
                    items.setAss_class(cla);
                    items.setAss_content(content);
                    items.setAss_teacher(teacher);
                    items.setAss_initials(initial);

                    items.setAss_extrainfo(extra);//set Extra info

                    if(j>6)
                        j=1;

                    if(j==1)
                        items.setLin_assignleft(getResources().getColor(R.color.colorLogin));
                    else if(j==2)
                        items.setLin_assignleft(getResources().getColor(R.color.indicator2));
                    else if(j==3)
                        items.setLin_assignleft(getResources().getColor(R.color.LightGreen));
                    else if(j==4)
                        items.setLin_assignleft(getResources().getColor(R.color.PowderBlue));
                    else if(j==5)
                        items.setLin_assignleft(getResources().getColor(R.color.lime));
                    else if(j==6)
                        items.setLin_assignleft(getResources().getColor(R.color.Tan));

                    j++;


                    histlist.add(items);

                    adapter = new RecyclerViewAdapterAssignment(getActivity(), histlist);
                    recyclerView.setAdapter(adapter);

                    recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                        @Override
                        public void onClick(View view, int position){
                            //Movie movie = movieList.get(position);
                            //Toast.makeText(getActivity().getApplicationContext(), "Full Detail :: " + newsContent.get(position), Toast.LENGTH_SHORT).show();

                            ((Home)getActivity()).assignmentview.clear();


                            TextView _subject = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assign_title);

                            TextView _qdetail = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assign_details);

                            TextView _qsubdate = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assign_date_cont);
                            TextView _qassigner = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assigner);
                            TextView _qclass = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assign_class);

                            //TextView _textname = (TextView)view.findViewById(R.id._name);

                            ((Home)getActivity()).assignmentview.add(_subject.getText().toString());
                            ((Home)getActivity()).assignmentview.add(_subject.getTag().toString());

                            ((Home)getActivity()).assignmentview.add(_qdetail.getText().toString());
                            ((Home)getActivity()).assignmentview.add(_qdetail.getTag().toString());

                            ((Home)getActivity()).assignmentview.add(_qsubdate.getText().toString());
                            ((Home)getActivity()).assignmentview.add(_qassigner.getText().toString());
                            ((Home)getActivity()).assignmentview.add(_qclass.getText().toString());

                            Fragment frag = new FragmentAssignmentView();

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.zoomin,0);
                            fragmentTransaction.replace(R.id._frame_home, frag, "fragassignmentview");
                            fragmentTransaction.commit();

                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));

                    count++;
                } while (cursor.moveToNext());
            }else{//if db is empty, retrieve from server
                if(((Home)getActivity()).netCheck.isConnected()) {
                   getAssignment();
                }else{
                    ((Home)getActivity()).dialogError("Your data connection seems to be diabled. Please try again!", "fragmteachers2");
                }
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void getAssignment(){
        ((Home)getActivity()).progressbarStartRetrieve("Assignment");
        String tag_json_obj_assignment = "json_obj_req_assignment";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("AssignmentList Retrieve", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar
                        mSwipeRefreshLayout.setRefreshing(false);

                        if(response != null){
                            Log.e("Assignment", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Assignment", response.toString());

                                storeAssignmentDb(data);

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("No Assignment found. Please check back later!", "assignment");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "assignment");
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
                ((Home)getActivity()).dialogError("Please try again!", "article");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("class", student_class);
                params.put("api_target", "2");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_assignment);
    }

    private void storeAssignmentDb(final JSONObject data) throws JSONException {
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        db.clearData(4);

        JSONArray response = null;
        try {
            Log.e("DATA STORAGE", "JSON DATA TO DB"+data.toString());
            response = data.getJSONArray("message");
            Log.e("Response", "JSON RESPONSE TO DB"+response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(db.addAssignmentData(response)) {

            Fragment frag = new FragmentAssignment();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.zoomin, 0);
            fragmentTransaction.replace(R.id._frame_home, frag, "fragassignment");
            fragmentTransaction.commit();
        }

        //getUserClass();
    }


    private void showListview(JSONObject data) throws JSONException {

        JSONArray response = null;
        try {
            response = data.getJSONArray("message");

            term.setText(((Home)getActivity()).TERM);
            termyear.setText(((Home)getActivity()).YEAR);
            week.setText(((Home)getActivity()).WEEK);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        int j = 1;
        int col;

        for(int i = 0; i < response.length(); i++) {
            JSONObject c = response.getJSONObject(i);

            // Storing JSON item in a Variable
            String subdate = c.getString(TAG_SUBMDATE);
            subdate = getCurrentTimeStamp(subdate);
            String title = c.getString(TAG_SUBJTITLE);
            String content = c.getString(TAG_QDETAIL);
            String teacher = c.getString(TAG_TEACHER);
            String initial = title.substring(0,1) + "+";
            String extra = c.getString(TAG_EXTRA);

            String cla = c.getString(TAG_CLASS);


            RowItemsAssignment items = new RowItemsAssignment();
            items.setDate(subdate);
            items.setTitle(title);
            items.setAss_class(cla);
            items.setAss_content(content);
            items.setAss_teacher(teacher);
            items.setAss_initials(initial);

            items.setAss_extrainfo(extra);//set Extra info

            if(j>6)
                j=1;

            if(j==1)
                items.setLin_assignleft(getResources().getColor(R.color.colorLogin));
            else if(j==2)
                items.setLin_assignleft(getResources().getColor(R.color.indicator2));
            else if(j==3)
                items.setLin_assignleft(getResources().getColor(R.color.LightGreen));
            else if(j==4)
                items.setLin_assignleft(getResources().getColor(R.color.PowderBlue));
            else if(j==5)
                items.setLin_assignleft(getResources().getColor(R.color.lime));
            else if(j==6)
                items.setLin_assignleft(getResources().getColor(R.color.Tan));

            j++;


            histlist.add(items);

            adapter = new RecyclerViewAdapterAssignment(getActivity(), histlist);
            recyclerView.setAdapter(adapter);

            recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                @Override
                public void onClick(View view, int position){
                    //Movie movie = movieList.get(position);
                    //Toast.makeText(getActivity().getApplicationContext(), "Full Detail :: " + newsContent.get(position), Toast.LENGTH_SHORT).show();

                    ((Home)getActivity()).assignmentview.clear();


                    TextView _subject = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assign_title);

                    TextView _qdetail = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assign_details);

                    TextView _qsubdate = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assign_date_cont);
                    TextView _qassigner = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assigner);
                    TextView _qclass = (TextView)recyclerView.getChildAt(position).findViewById(R.id._assign_class);

                    //TextView _textname = (TextView)view.findViewById(R.id._name);

                    ((Home)getActivity()).assignmentview.add(_subject.getText().toString());
                    ((Home)getActivity()).assignmentview.add(_subject.getTag().toString());

                    ((Home)getActivity()).assignmentview.add(_qdetail.getText().toString());
                    ((Home)getActivity()).assignmentview.add(_qdetail.getTag().toString());

                    ((Home)getActivity()).assignmentview.add(_qsubdate.getText().toString());
                    ((Home)getActivity()).assignmentview.add(_qassigner.getText().toString());
                    ((Home)getActivity()).assignmentview.add(_qclass.getText().toString());

                    Fragment frag = new FragmentAssignmentView();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.zoomin,0);
                    fragmentTransaction.replace(R.id._frame_home, frag, "fragassignmentview");
                    fragmentTransaction.commit();

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }

    }

    public static String getCurrentTimeStamp(String dates) {
        long date = Long.parseLong(dates);
        Date d=new Date(((long)date)*1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm a", Locale.ENGLISH); //"dd-MMM-yyyy"
        //SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }


}
