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
import com.daniellasmontesssorischool.dms.utils.DividerItemDecoration;
import com.daniellasmontesssorischool.dms.utils.RecyclerViewAdapterNews;
import com.daniellasmontesssorischool.dms.utils.RowItemsNews;

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
public class FragmentNews extends Fragment {

    ArrayList<RowItemsNews> histlist = new ArrayList<>();
    private static final String TAG_DATE = "news_date";
    private static final String TAG_TITLE = "news_header";
    private static final String TAG_DETAIL = "news_detail";
    private static final String TAG_TYPE = "news_type";
    private static final String TAG_ID = "newsid";

    private static String PREF_LAST_SCHNEWS = "pref_lastnews_update";

    private static final String lastupdateid = "";

    ArrayList<String> newsContent = new ArrayList<>();

    RecyclerView recyclerView;
    ArrayList<RowItemsNews> itemsList = new ArrayList<>();
    RecyclerViewAdapterNews adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Button _upload;

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";


    NetworkCheck netCheck;
    View rootView;

    View vi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_news, container, false);

        ((Home)getActivity()).toolbar.setTitle("School News");

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_news);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if(netCheck.isConnected()) {
                    getNews();
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

        //getArticles();
        //getNews();

        getNewsFromDb();

        return rootView;
    }

    void getNewsFromDb() {
                //Do Work here
                DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
                Cursor cursor = db.getNewsData();

                try {
                    Log.e("DATA RETRIEVAL", "CURSOR DATA FROM DB"+cursor.toString());

                    Log.e("Response", "JSON RESPONSE TO DB"+cursor.toString());

                    int count = 1;
                    int j = 1;
                    // Move to first row
                    if(cursor.moveToFirst() && !cursor.toString().equals(null) || cursor.toString().length() > 60) {
                        do {
                            //lastid = cursor.getString(cursor.getColumnIndex("newsid"));
                            // Storing JSON item in a Variable
                            String date = cursor.getString(cursor.getColumnIndex("news_date"));
                            date = getCurrentTimeStamp(date);
                            String title = cursor.getString(cursor.getColumnIndex("news_title"));
                            String detail = cursor.getString(cursor.getColumnIndex("news_detail"));
                            String type = cursor.getString(cursor.getColumnIndex("news_type"));

                            //add article content to arraylist, to be retrieved later
                            //newsContent.add(detail);

                            RowItemsNews items = new RowItemsNews();
                            items.setDate(date);
                            items.setNews_title(title);
                            items.setNews_content(detail);
                            items.setNews_type(type);

                            if (j > 2)
                                j = 1;

                            if (j == 1)
                                items.setCol(getResources().getColor(R.color.dmsPurple), getResources().getColor(R.color.GreyTextFb), getResources().getColor(R.color.dmsnewstype));
                            else if (j == 2)
                                items.setCol(getResources().getColor(R.color.dmsPink), getResources().getColor(R.color.dmsPurple), getResources().getColor(R.color.dmsnewstype));
                            else if (j == 3)
                                items.setCol(getResources().getColor(R.color.indicator2), getResources().getColor(R.color.GreyTextFb), getResources().getColor(R.color.dmsnewstype));

                            j++;

                            histlist.add(items);

                           /* recyclerView.removeAllViewsInLayout();
                            recyclerView.removeAllViews();*/

                            adapter = new RecyclerViewAdapterNews(getActivity(), histlist);
                            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

                            //adapter.notifyDataSetChanged();
                            // set the adapter
                            recyclerView.setAdapter(adapter);

                            recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    //Movie movie = movieList.get(position);
                                    //Toast.makeText(getActivity().getApplicationContext(), "Full Detail :: " + newsContent.get(position), Toast.LENGTH_SHORT).show();

                                    ((Home) getActivity()).newsview.clear();


                                    TextView _title = (TextView) recyclerView.getChildAt(position).findViewById(R.id._news_header);
                                    TextView _detail = (TextView) recyclerView.getChildAt(position).findViewById(R.id._news_detail);

                                    TextView _date = (TextView) recyclerView.getChildAt(position).findViewById(R.id._news_date);
                                    TextView _type = (TextView) recyclerView.getChildAt(position).findViewById(R.id._news_type);

                                    //TextView _textname = (TextView)view.findViewById(R.id._name);

                                    ((Home) getActivity()).newsview.add(_title.getText().toString());
                                    ((Home) getActivity()).newsview.add(_detail.getText().toString());
                                    ((Home) getActivity()).newsview.add(_date.getText().toString());
                                    ((Home) getActivity()).newsview.add(_type.getText().toString());

                                    Fragment frag = new FragmentNewsView();

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.zoomin, 0);
                                    fragmentTransaction.replace(R.id._frame_home, frag, "fragnewsview");
                                    fragmentTransaction.commit();

                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));

                            count++;
                        } while (cursor.moveToNext());
                    }else{//if db is empty, retrieve from server
                        if(netCheck.isConnected()) {
                            getNews();
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

    void getNews(){
        /*if(_retry!=null)
            _retry.setVisibility(View.GONE);*/

        ((Home)getActivity()).progressbarStartRetrieve("News");
        String tag_json_obj_assignment = "json_obj_req_news";


        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("NewsList Retrieve", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        mSwipeRefreshLayout.setRefreshing(false);

                        if(response != null){
                            Log.e("News", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag News", response.toString());

                                storeNewsDb(data);

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("No news items found. Please check back later!", "news");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few hours..", "news");
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
                mSwipeRefreshLayout.setRefreshing(false);
                //   Handle Error
                ((Home)getActivity()).dialogError("Please try again!", "news");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("lastid", "all");
                params.put("api_target", "7");

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

    /*private void showRetry(){
        _retry = (LinearLayout)rootView.findViewById(R.id._retry);
        _retry.setVisibility(View.VISIBLE);

        _retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArticles();
            }
        });
    }
*/


        private void storeNewsDb(final JSONObject data) throws JSONException {
        /*new Thread(new Runnable() {
            public void run() {*/
                //Do Work here
                DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
                db.clearData(1);

                JSONArray response = null;
                try {
                    Log.e("DATA STORAGE", "JSON DATA TO DB"+data.toString());
                    response = data.getJSONArray("message");
                    Log.e("Response", "JSON RESPONSE TO DB"+response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    if(db.addNewsData(response)) {

                        Fragment frag = new FragmentNews();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        //fragmentTransaction.setCustomAnimations(R.anim.zoomin, 0);
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragnews");
                        fragmentTransaction.commit();
                    }

           /* }
        }).start();*/

        //getNewsFromDb();
    }

    /*private void showListview(JSONObject data) throws JSONException {

        //DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

        JSONArray response = null;
        try {
            Log.e("DATA Response", "JSON DATA"+data.toString());
            response = data.getJSONArray("message");
            Log.e("Response", "JSON RESPONSE"+response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int j = 1;
        int col;
        String lastid = null;


        for(int i = 0; i < response.length(); i++) {
            JSONObject c = response.getJSONObject(i);

            lastid = c.getString(TAG_ID);
            // Storing JSON item in a Variable
            String date = c.getString(TAG_DATE);
            date = getCurrentTimeStamp(date);
            String title = c.getString(TAG_TITLE);
            String detail = c.getString(TAG_DETAIL);
            String type = c.getString(TAG_TYPE);

            //add article content to arraylist, to be retrieved later
            newsContent.add(detail);

            RowItemsNews items = new RowItemsNews();
            items.setDate(date);
            items.setNews_title(title);
            items.setNews_content(detail);
            items.setNews_type(type);

            if(j>2)
                j=1;

            if(j==1)
                items.setCol(getResources().getColor(R.color.dmsPurple), getResources().getColor(R.color.GreyTextFb), getResources().getColor(R.color.dmsnewstype));
            else if(j==2)
                items.setCol(getResources().getColor(R.color.dmsPink), getResources().getColor(R.color.dmsPurple), getResources().getColor(R.color.dmsnewstype));
            else if(j==3)
                items.setCol(getResources().getColor(R.color.indicator2), getResources().getColor(R.color.GreyTextFb), getResources().getColor(R.color.dmsnewstype));

            j++;

            histlist.add(items);

            adapter = new RecyclerViewAdapterNews(getActivity(), histlist);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

            // set the adapter
            recyclerView.setAdapter(adapter);

            recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //Movie movie = movieList.get(position);
                    //Toast.makeText(getActivity().getApplicationContext(), "Full Detail :: " + newsContent.get(position), Toast.LENGTH_SHORT).show();

                    ((Home)getActivity()).newsview.clear();


                    TextView _title = (TextView)recyclerView.getChildAt(position).findViewById(R.id._news_header);
                    TextView _detail = (TextView)recyclerView.getChildAt(position).findViewById(R.id._news_detail);

                    TextView _date = (TextView)recyclerView.getChildAt(position).findViewById(R.id._news_date);
                    TextView _type = (TextView)recyclerView.getChildAt(position).findViewById(R.id._news_type);

                    //TextView _textname = (TextView)view.findViewById(R.id._name);

                    ((Home)getActivity()).newsview.add(_title.getText().toString());
                    ((Home)getActivity()).newsview.add(_detail.getText().toString());
                    ((Home)getActivity()).newsview.add(_date.getText().toString());
                    ((Home)getActivity()).newsview.add(_type.getText().toString());

                    Fragment frag = new FragmentNewsView();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.zoomin,0);
                    fragmentTransaction.replace(R.id._frame_home, frag, "fragnewsview");
                    fragmentTransaction.commit();

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }

        *//*((Home)getActivity()).pref.edit()
                .putString(PREF_LAST_SCHNEWS, lastid)
                .commit();*//*

    }
*/
    public static String getCurrentTimeStamp(String dates) {
        long date = Long.parseLong(dates);
        Date d=new Date(((long)date)*1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm a", Locale.ENGLISH); //"dd-MMM-yyyy"
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }

    /*public void showPopup(View view) {
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button btnDismiss = (Button) popupView.findViewById(R.id.btn_dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(popupView, 0, 0);
    }*/


    //for running long ui threads
    /*private void populateTable() {
        runOnUiThread(new Runnable(){
            public void run() {
                //If there are stories, add them to the table
                for (Parcelable currentHeadline : allHeadlines) {
                    addHeadlineToTable(currentHeadline);
                }
                try {
                    dialog.dismiss();
                } catch (final Exception ex) {
                    Log.i("---","Exception in thread");
                }
            }
        });
    }*/


}
