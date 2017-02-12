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
import com.daniellasmontesssorischool.dms.utils.RecyclerViewAdapter;
import com.daniellasmontesssorischool.dms.utils.RowItems;

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
public class FragmentArticle extends Fragment {



    ArrayList<RowItems> histlist = new ArrayList<RowItems>();
    private static final String TAG_DATE = "date_added";
    private static final String TAG_TITLE = "title";
    private static final String TAG_NAME = "added_by";
    private static final String TAG_CLASS = "stud_class";

    private static final String TAG_CONTENT = "content";
    private static final String TAG_TYPE = "type";

    RecyclerView recyclerView;
    ArrayList<RowItems> itemsList = new ArrayList<>();
    RecyclerViewAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;


    Button _upload;

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";


    String tag_json_obj_image = "json_req_homeimg";

    NetworkCheck netCheck;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_article, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        _upload = (Button)rootView.findViewById(R.id._upload_button);

        _upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Home)getActivity()).toolbarTitle.setText("Upload Article");
                Fragment frag = new FragmentArticleUpload();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id._frame_home, frag, "fragupload");
                fragmentTransaction.commit();
            }
        });

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if(((Home)getActivity()).netCheck.isConnected()) {
                   getArticles();
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "fragmteachers2");
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

        //adapter = new RecyclerViewAdapter(MainActivity.this, getData());

        getArticlesFromDb();

        return rootView;
    }

    void getArticlesFromDb() {
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        Cursor cursor = db.getArticleData();

        try {
            Log.e("DATA RETRIEVAL", "CURSOR DATA FROM DB"+cursor.toString());

            Log.e("Response", "JSON RESPONSE TO DB"+cursor.toString());

            int j = 1;
            // Move to first row
            if(cursor.moveToFirst() && !cursor.toString().equals(null) || cursor.toString().length() > 60) {
                do {
                    // Storing JSON item in a Variable
                    String date = cursor.getString(cursor.getColumnIndex("article_date"));
                    date = getCurrentTimeStamp(date);
                    String title = cursor.getString(cursor.getColumnIndex("article_title"));
                    String stname = cursor.getString(cursor.getColumnIndex("article_name"));
                    String stclass = cursor.getString(cursor.getColumnIndex("article_class"));

                    String stcontent = cursor.getString(cursor.getColumnIndex("article_content"));
                    String art_type = cursor.getString(cursor.getColumnIndex("article_type"));

                    RowItems items = new RowItems();
                    items.setDate(date);
                    items.setTitle(title);
                    items.setStud_name(stname);
                    items.setStud_class(stclass);
                    items.setArticle_type(art_type);
                    items.setArticle_content(stcontent);
                    histlist.add(items);

                    adapter = new RecyclerViewAdapter(getActivity(), histlist);

                    // set the adapter
                    recyclerView.setAdapter(adapter);

                    recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            ((Home)getActivity()).articleview.clear();

                            TextView _textdate = (TextView)recyclerView.getChildAt(position).findViewById(R.id._date);
                            TextView _texttype = (TextView)recyclerView.getChildAt(position).findViewById(R.id._type);

                            TextView _texttitle = (TextView)recyclerView.getChildAt(position).findViewById(R.id._title);
                            TextView _textname = (TextView)recyclerView.getChildAt(position).findViewById(R.id._name);

                            TextView _textclass = (TextView)recyclerView.getChildAt(position).findViewById(R.id._class);
                            //TextView _textname = (TextView)view.findViewById(R.id._name);

                            ((Home)getActivity()).articleview.add(_textdate.getText().toString());
                            ((Home)getActivity()).articleview.add(_texttype.getText().toString());
                            ((Home)getActivity()).articleview.add(_texttitle.getText().toString());
                            ((Home)getActivity()).articleview.add(_textname.getText().toString());
                            ((Home)getActivity()).articleview.add(_textclass.getText().toString());
                            ((Home)getActivity()).articleview.add(_texttitle.getTag().toString());

                            ((Home)getActivity()).toolbarTitle.setText(_texttitle.getText().toString());

                            Fragment frag = new FragmentArticleView();

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                            fragmentTransaction.replace(R.id._frame_home, frag, "fragarticleview");
                            fragmentTransaction.commit();

                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));

                    //count++;
                } while (cursor.moveToNext());
            }else{//if db is empty, retrieve from server
                if(((Home)getActivity()).netCheck.isConnected()) {
                   getArticles();
                }else{
                    ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "fragmteachers2");
                }
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void getArticles(){
        ((Home)getActivity()).progressbarStartRetrieve("Articles");
        String tag_json_obj_article = "json_obj_req_article";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Articles Retrieval", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar
                        mSwipeRefreshLayout.setRefreshing(false);

                        if(response != null){
                            Log.e("Articles", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Articles", response.toString());

                                storeArticleDb(data);

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("No articles found. Please check back later!", "article");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "article");
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
                ((Home)getActivity()).dialogError("Please try again!", "article");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("lastdate", "all");
                params.put("api_target", "6");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_article);
    }


    private void storeArticleDb(final JSONObject data) throws JSONException {
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        db.clearData(5);

        JSONArray response = null;
        try {
            Log.e("DATA STORAGE", "JSON DATA TO DB"+data.toString());
            response = data.getJSONArray("message");
            Log.e("Response", "JSON RESPONSE TO DB"+response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(db.addArticleData(response)) {

            Fragment frag = new FragmentArticle();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.zoomin, 0);
            fragmentTransaction.replace(R.id._frame_home, frag, "fragarticle");
            fragmentTransaction.commit();
        }

        //getArticlesFromDb();
    }


    /*private void showListview(JSONObject data) throws JSONException {

        JSONArray response = null;
        try {
            response = data.getJSONArray("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < response.length(); i++) {
            JSONObject c = response.getJSONObject(i);

            // Storing JSON item in a Variable
            String date = c.getString(TAG_DATE);
            date = getCurrentTimeStamp(date);
            String title = c.getString(TAG_TITLE);
            String stname = c.getString(TAG_NAME);
            String stclass = c.getString(TAG_CLASS);

            String stcontent = c.getString(TAG_CONTENT);
            String art_type = c.getString(TAG_TYPE);
            //add article content to arraylist, to be retrieved later
            //articleContent.add(stcontent);


            RowItems items = new RowItems();
            items.setDate(date);
            items.setTitle(title);
            items.setStud_name(stname);
            items.setStud_class(stclass);
            items.setArticle_type(art_type);
            items.setArticle_content(stcontent);
            histlist.add(items);

            adapter = new RecyclerViewAdapter(getActivity(), histlist);
            recyclerView.setAdapter(adapter);

            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            //recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));//set height of 2 between row items

            // set the adapter
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //Movie movie = movieList.get(position);
                    //((Home)getActivity()).articleview.add();

                    ((Home)getActivity()).articleview.clear();

                    TextView _textdate = (TextView)recyclerView.getChildAt(position).findViewById(R.id._date);
                    TextView _texttype = (TextView)recyclerView.getChildAt(position).findViewById(R.id._type);

                    TextView _texttitle = (TextView)recyclerView.getChildAt(position).findViewById(R.id._title);
                    TextView _textname = (TextView)recyclerView.getChildAt(position).findViewById(R.id._name);

                    TextView _textclass = (TextView)recyclerView.getChildAt(position).findViewById(R.id._class);
                    //TextView _textname = (TextView)view.findViewById(R.id._name);

                    ((Home)getActivity()).articleview.add(_textdate.getText().toString());
                    ((Home)getActivity()).articleview.add(_texttype.getText().toString());
                    ((Home)getActivity()).articleview.add(_texttitle.getText().toString());
                    ((Home)getActivity()).articleview.add(_textname.getText().toString());
                    ((Home)getActivity()).articleview.add(_textclass.getText().toString());
                    ((Home)getActivity()).articleview.add(_texttitle.getTag().toString());

                    Fragment frag = new FragmentArticleView();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.popup_enter,0);
                    fragmentTransaction.replace(R.id._frame_home, frag, "fragarticleview");
                    fragmentTransaction.commit();

//                    Toast.makeText(getActivity().getApplicationContext(), "Any Title >> " + _texttit.getText().toString() + " >> is selected!  Position :: "+position+" :: "+_textv.getText().toString(), Toast.LENGTH_SHORT).show();
                    //recyclerView.getChildLayoutPosition(view).
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
        SimpleDateFormat sdfDate = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }


   /* public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
           // outRect.bottom = mVerticalSpaceHeight;
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = mVerticalSpaceHeight;
            }
        }
    }*/

}
