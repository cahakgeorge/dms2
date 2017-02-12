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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;
import com.daniellasmontesssorischool.dms.utils.DatabaseHandler;
import com.daniellasmontesssorischool.dms.utils.RecyclerViewAdapterCalendar;
import com.daniellasmontesssorischool.dms.utils.RowItemsCalendar;

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
public class FragmentCalendar extends Fragment {

    ArrayList<RowItemsCalendar> histlist = new ArrayList<>();
    private static final String TAG_DAYMONTH = "pic_monthdate";
    private static final String TAG_PICTITLE = "pic_title";
    private static final String TAG_HEADER = "title";
    private static final String TAG_DETAIL = "details";
    private static final String TAG_INTERVAL = "timeframe";

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";

    RecyclerView recyclerView;
    ArrayList<RowItemsCalendar> itemsList = new ArrayList<>();
    RecyclerViewAdapterCalendar adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;


    Button _upload;

    NetworkCheck netCheck;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_cec);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if(netCheck.isConnected()) {
                    getCalendar();
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "manageteachersdismiss");
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

        getCalendarFromDB();

        return rootView;
    }


    void getCalendarFromDB() {
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        Cursor cursor = db.getCalendarData();

        try {
            Log.e("DATA RETRIEVAL", "CURSOR DATA FROM DB"+cursor.toString());

            Log.e("Response", "JSON RESPONSE TO DB"+cursor.toString());

            int j = 1;
            // Move to first row
            if(cursor.moveToFirst() && !cursor.toString().equals(null) || cursor.toString().length() > 60) {
                do {
                    //lastid = cursor.getString(cursor.getColumnIndex("newsid"));
                    // Storing JSON item in a Variable

                    String date = cursor.getString(cursor.getColumnIndex("calen_daymonth"));
                    date = getCurrentTimeStamp(date);
                    String[] dateStr = date.split(" "); //split date by space delimiter to pick out month and day

                    String month = dateStr[1];
                    String day = dateStr[2];

                    String pictitle = cursor.getString(cursor.getColumnIndex("calen_pictitle"));
                    String header = cursor.getString(cursor.getColumnIndex("calen_header"));
                    String detail = cursor.getString(cursor.getColumnIndex("calen_detail"));
                    String interval = cursor.getString(cursor.getColumnIndex("calen_interval"));


                    RowItemsCalendar items = new RowItemsCalendar();
                    items.setCal_day(day);
                    items.setCal_month(month);
                    items.setCalimg_title(pictitle);
                    items.setCal_title(header);
                    items.setCal_detail(detail);
                    items.setCal_interval(interval);

                    if(j>12)
                        j=1;

                    if(j==1)
                        items.setBackpic(R.drawable.app_img1);
                    else if(j==2)
                        items.setBackpic(R.drawable.app_img2);
                    else if(j==3)
                        items.setBackpic(R.drawable.app_img3);
                    else if(j==4)
                        items.setBackpic(R.drawable.app_img4);
                    else if(j==5)
                        items.setBackpic(R.drawable.app_img5);
                    else if(j==6)
                        items.setBackpic(R.drawable.app_img6);
                    else if(j==7)
                        items.setBackpic(R.drawable.app_img7);
                    else if(j==8)
                        items.setBackpic(R.drawable.app_img8);
                    else if(j==9)
                        items.setBackpic(R.drawable.app_img9);
                    else if(j==10)
                        items.setBackpic(R.drawable.app_img10);
                    else if(j==11)
                        items.setBackpic(R.drawable.app_img1);
                    else if(j==12)
                        items.setBackpic(R.drawable.app_img12);
                    j++;

                    histlist.add(items);

                    adapter = new RecyclerViewAdapterCalendar(getActivity(), histlist);
                    recyclerView.setAdapter(adapter);

                    /*recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            //Movie movie = movieList.get(position);
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));*/
                    //count++;
                } while (cursor.moveToNext());
            }else{//if db is empty, retrieve from server
                if(netCheck.isConnected()) {
                    getCalendar();
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "manageteachersdismiss");
                }
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void getCalendar(){
        ((Home)getActivity()).progressbarStartRetrieve("Calendar Items");
        String tag_json_obj_assignment = "json_obj_req_calendar";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("CalendarList Retrieve", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar
                        mSwipeRefreshLayout.setRefreshing(false);


                        if(response != null){
                            Log.e("Calendar", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Calendar", response.toString());

                                storeCalendarDb(data);

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("No calendar items found. Please check back later!", "calendar");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few hours..", "calendar");
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
                mSwipeRefreshLayout.setRefreshing(false);
                ((Home)getActivity()).dialogError("Please try again!", "Calendar");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("type", "all");
                params.put("api_target", "9");

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

    private void storeCalendarDb(final JSONObject data) throws JSONException {
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        db.clearData(3);

        JSONArray response = null;
        try {
            Log.e("DATA STORAGE", "JSON DATA TO DB"+data.toString());
            response = data.getJSONArray("message");
            Log.e("Response", "JSON RESPONSE TO DB"+response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(db.addCalendarData(response)) {

            Fragment frag = new FragmentCalendar();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.zoomin, 0);
            fragmentTransaction.replace(R.id._frame_home, frag, "fragcalendar");
            fragmentTransaction.commit();
        }


        //getCalendarFromDB();
    }


    private void showListview(JSONObject data) throws JSONException {
        JSONArray response = null;
        try {
            response = data.getJSONArray("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        int j = 1;
        for(int i = 0; i < response.length(); i++) {
            JSONObject c = response.getJSONObject(i);

            // Storing JSON item in a Variable
            String date = c.getString(TAG_DAYMONTH);
            date = getCurrentTimeStamp(date);
            String[] dateStr = date.split(" "); //split date by space delimiter to pick out month and day

            String month = dateStr[1];
            String day = dateStr[2];

            String pictitle = c.getString(TAG_PICTITLE);
            String header = c.getString(TAG_HEADER);
            String detail = c.getString(TAG_DETAIL);
            String interval = c.getString(TAG_INTERVAL);

            RowItemsCalendar items = new RowItemsCalendar();
            items.setCal_day(day);
            items.setCal_month(month);
            items.setCalimg_title(pictitle);
            items.setCal_title(header);
            items.setCal_detail(detail);
            items.setCal_interval(interval);

            if(j>12)
                j=1;

            if(j==1)
                items.setBackpic(R.drawable.app_img1);
            else if(j==2)
                items.setBackpic(R.drawable.app_img2);
            else if(j==3)
                items.setBackpic(R.drawable.app_img3);
            else if(j==4)
                items.setBackpic(R.drawable.app_img4);
            else if(j==5)
                items.setBackpic(R.drawable.app_img5);
            else if(j==6)
                items.setBackpic(R.drawable.app_img6);
            else if(j==7)
                items.setBackpic(R.drawable.app_img7);
            else if(j==8)
                items.setBackpic(R.drawable.app_img8);
            else if(j==9)
                items.setBackpic(R.drawable.app_img9);
            else if(j==10)
                items.setBackpic(R.drawable.app_img10);
            else if(j==11)
                items.setBackpic(R.drawable.app_img1);
            else if(j==12)
                items.setBackpic(R.drawable.app_img12);
            j++;

            histlist.add(items);

            adapter = new RecyclerViewAdapterCalendar(getActivity(), histlist);
            recyclerView.setAdapter(adapter);
        }

    }


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
