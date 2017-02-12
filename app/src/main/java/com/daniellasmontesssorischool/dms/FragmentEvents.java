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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;
import com.daniellasmontesssorischool.dms.utils.DatabaseHandler;
import com.daniellasmontesssorischool.dms.utils.RecyclerViewAdapterEvent;
import com.daniellasmontesssorischool.dms.utils.RowItemsEvent;

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
public class FragmentEvents extends Fragment {


    ArrayList<RowItemsEvent> histlist = new ArrayList<>();
    private static final String TAG_PARTICIPANT = "participants";
    private static final String TAG_HEADER = "title";
    private static final String TAG_DETAIL = "detail";

    private static final String TAG_DATE = "event_date";

    private static final String TAG_ID = "eventid";
    //private static final String TAG_DAY = "day";

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";

    ArrayList<String> eventContent = new ArrayList<>();
    int dialogopenedcount = 0;

    RecyclerView recyclerView;
    ArrayList<RowItemsEvent> itemsList = new ArrayList<>();
    RecyclerViewAdapterEvent adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Button _upload;

    NetworkCheck netCheck;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_event, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_cec);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if(((Home)getActivity()).netCheck.isConnected()) {
                    getEvents();
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    ((Home)getActivity()).dialogError("Your data connection seems to be disabled. Please try again!", "fragmteachers2");
                }
            }
        });

        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new SlideInUpAnimator());

        //getEvents();
        //getEvents();
        getEventsFromDb();

        return rootView;
    }

    void getEventsFromDb() {
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        Cursor cursor = db.getEventData();

        try {
            Log.e("DATA RETRIEVAL", "CURSOR DATA FROM DB"+cursor.toString());

            Log.e("Response", "JSON RESPONSE TO DB"+cursor.toString());

            int count = 1;
            int j = 1;
            // Move to first row
            if(cursor.moveToFirst() && !cursor.toString().equals(null) || cursor.toString().length() > 60)
                do {

                    //lastid = cursor.getString(cursor.getColumnIndex("eventid"));
                    // Storing JSON item in a Variable
                    String date = cursor.getString(cursor.getColumnIndex("event_date"));
                    date = getCurrentTimeStamp(date);

                    String[] dateStr = date.split(" "); //split date by space delimiter to pick out month and day

                    String month = dateStr[2];
                    String day = dateStr[1];

                    String header = cursor.getString(cursor.getColumnIndex("event_header"));
                    String detail = cursor.getString(cursor.getColumnIndex("event_detail"));
                    String participant = cursor.getString(cursor.getColumnIndex("event_partip"));

                    RowItemsEvent items = new RowItemsEvent();
                    items.setParticipants(participant);
                    items.setEvent_header(header);
                    items.setEvent_detail(detail);

                    items.setEvent_month(month);
                    items.setEvent_day(day);

                    if (j > 6)
                        j = 1;

                    if (j == 1)
                        items.setEvent_col(getResources().getColor(R.color.dmsPurple));
                    else if (j == 2)
                        items.setEvent_col(getResources().getColor(R.color.indicator2));
                    else if (j == 3)
                        items.setEvent_col(getResources().getColor(R.color.dmsPink400));
                    else if (j == 4)
                        items.setEvent_col(getResources().getColor(R.color.indicator5));
                    else if (j == 5)
                        items.setEvent_col(getResources().getColor(R.color.indicator1));
                    else if (j == 6)
                        items.setEvent_col(getResources().getColor(R.color.indicator3));

                    j++;

                    histlist.add(items);

                    adapter = new RecyclerViewAdapterEvent(getActivity(), histlist);
                    recyclerView.setAdapter(adapter);

                    recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            //Movie movie = movieList.get(position);
                            //((Home)getActivity()).newsview.clear();

                            TextView _title = (TextView)recyclerView.getChildAt(position).findViewById(R.id._event_header);
                            TextView _detail = (TextView)recyclerView.getChildAt(position).findViewById(R.id._event_detail);

                            TextView _month = (TextView)recyclerView.getChildAt(position).findViewById(R.id._event_month);
                            TextView _day = (TextView)recyclerView.getChildAt(position).findViewById(R.id._event_day);

                            String title = _title.getText().toString()+"("+_month.getText().toString()+" "+_day.getText().toString()+")";
                            String detail = _detail.getText().toString();

                            ((Home)getActivity()).eventout(title, detail);
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));

                    count++;
                } while (cursor.moveToNext());
            else{//if db is empty, retrieve from server
                if(((Home)getActivity()).netCheck.isConnected()) {
                    getEvents();
                }else{
                    ((Home)getActivity()).dialogError("Your data connection seems to be disabled. Please try again!", "fragmteachers2");
                }
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void getEvents(){
        ((Home)getActivity()).progressbarStartRetrieve("Events");
        String tag_json_obj_assignment = "json_obj_req_event";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("EventList Retrieve", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar
                        mSwipeRefreshLayout.setRefreshing(false);

                        if(response != null){
                            Log.e("Events", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Events", response.toString());

                                storeEventsDb(data);

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("No events available. Please check back later!", "events");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few hours..", "events");
                        }
                    }else{ //response from server is null
                        ((Home) getActivity()).progressbarStop();
                        ((Home) getActivity()).dialogOutput("Couldn't access server", "Please check your data connection", false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ((Home)getActivity()).progressbarStop();
                mSwipeRefreshLayout.setRefreshing(false);
                //   Handle Error
                ((Home)getActivity()).dialogError("Please try again!", "Events");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("which", "all");
                params.put("api_target", "8");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        // Adding request to request queue
        //jsonObjReq.setShouldCache(false);
        /*AppController.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });*/

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_assignment);
    }

    private void storeEventsDb(final JSONObject data) throws JSONException {
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        db.clearData(2);

        JSONArray response = null;
        try {
            Log.e("DATA STORAGE", "JSON DATA TO DB"+data.toString());
            response = data.getJSONArray("message");
            Log.e("Response", "JSON RESPONSE TO DB"+response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(db.addEventData(response)) {

            Fragment frag = new FragmentEvents();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.zoomin, 0);
            fragmentTransaction.replace(R.id._frame_home, frag, "fragevent");
            fragmentTransaction.commit();
        }

        //getEventsFromDb();
    }


   /* private void showListview(JSONObject data) throws JSONException {

        JSONArray response = null;
        try {
            response = data.getJSONArray("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        int j = 1;
        int col = getResources().getColor(R.color.dmsPink400);
        for(int i = 0; i < response.length(); i++) {
            JSONObject c = response.getJSONObject(i);

            // Storing JSON item in a Variable
            String participant = c.getString(TAG_PARTICIPANT);
            String header = c.getString(TAG_HEADER);
            String detail = c.getString(TAG_DETAIL);

            String date = c.getString(TAG_DATE);
            date = getCurrentTimeStamp(date);
            String[] dateStr = date.split(" "); //split date by space delimiter to pick out month and day

            String month = dateStr[2];
            String day = dateStr[1];

            //add article content to arraylist, to be retrieved later
            eventContent.add(detail);

            RowItemsEvent items = new RowItemsEvent();
            items.setParticipants(participant);
            items.setEvent_header(header);
            items.setEvent_detail(detail);

            items.setEvent_month(month);
            items.setEvent_day(day);

            if(j>6)
                j=1;

            if(j==1)
                items.setEvent_col(getResources().getColor(R.color.dmsPurple));
            else if(j==2)
                items.setEvent_col(getResources().getColor(R.color.indicator2));
            else if(j==3)
                items.setEvent_col(getResources().getColor(R.color.dmsPink400));
            else if(j==4)
                items.setEvent_col(getResources().getColor(R.color.indicator5));
            else if(j==5)
                items.setEvent_col(getResources().getColor(R.color.indicator1));
            else if(j==6)
                items.setEvent_col(getResources().getColor(R.color.indicator3));

            j++;

            histlist.add(items);

            adapter = new RecyclerViewAdapterEvent(getActivity(), histlist);
            recyclerView.setAdapter(adapter);

            recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //Movie movie = movieList.get(position);
                    //((Home)getActivity()).newsview.clear();

                    TextView _title = (TextView)recyclerView.getChildAt(position).findViewById(R.id._event_header);
                    TextView _detail = (TextView)recyclerView.getChildAt(position).findViewById(R.id._event_detail);

                    TextView _month = (TextView)recyclerView.getChildAt(position).findViewById(R.id._event_month);
                    TextView _day = (TextView)recyclerView.getChildAt(position).findViewById(R.id._event_day);

                    String title = _title.getText().toString()+"("+_month.getText().toString()+" "+_day.getText().toString()+")";
                    String detail = _detail.getText().toString();

                        ((Home)getActivity()).eventout(title, detail);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        }

    }*/


    public static String getCurrentTimeStamp(String dates) {
        long date = Long.parseLong(dates);
        Date d=new Date(((long)date)*1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm a", Locale.ENGLISH); //"dd-MMM-yyyy"
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }

}
