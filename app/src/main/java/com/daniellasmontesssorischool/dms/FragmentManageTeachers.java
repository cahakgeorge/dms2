package com.daniellasmontesssorischool.dms;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.daniellasmontesssorischool.dms.utils.DividerItemDecoration;
import com.daniellasmontesssorischool.dms.utils.RecyclerViewAdapterMTeacher;
import com.daniellasmontesssorischool.dms.utils.RowItemsMTeachers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentManageTeachers extends Fragment {



    ArrayList<RowItemsMTeachers> histlist = new ArrayList<RowItemsMTeachers>();
    private static final String TAG_DOB = "dob";
    private static final String TAG_ACCESS = "access";
    private static final String TAG_FNAME = "firstname";
    private static final String TAG_LNAME = "lastname";

    private static final String TAG_GENDER = "gender";
    private static final String TAG_USRNAME = "username";


    RecyclerView recyclerView;
    ArrayList<RowItemsMTeachers> itemsList = new ArrayList<>();
    RecyclerViewAdapterMTeacher adapter;

    Button _upload;

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";


    String tag_json_obj_image = "json_req_homeimg";

    NetworkCheck netCheck;
    View rootView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    MaterialDialog dialog;

    Boolean showFAB = true;

    //BottomLayout
    public static EditText user,pword, fname, lname, gender, dob, startdate;
    String userSt, pwordSt, fnameSt, lnameSt, genderSt, dobSt, startdateSt;
    Button add;
    public static String which_date = "dob";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_manage_teachers, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());
        final Animation growAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)rootView.findViewById(R.id.coordinatorlayout);
        final FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id._fab);

        final View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (showFAB) {
                            fab.startAnimation(shrinkAnimation);
                            showFAB = false;
                        }
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        showFAB = true;
                        fab.setVisibility(View.VISIBLE);
                        fab.startAnimation(growAnimation);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        showFAB = false;
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                if(behavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else if(behavior.getState()==BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

                if(showFAB){
                    showFAB = false;
                }else{
                    showFAB = true;
                }
            }
        });

        //initialize bottom sheet layout elements
        initBottomLayout();

        /*_upload = (Button)rootView.findViewById(R.id._upload_button);

        _upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new FragmentArticleUpload();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id._frame_home, frag, "fragupload");
                fragmentTransaction.commit();
            }
        });
*/
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_teachers);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if(netCheck.isConnected()) {
                    getTeacherList();
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

        // Refresh items
        if(netCheck.isConnected()) {
            getTeacherList();
        }else{
            mSwipeRefreshLayout.setRefreshing(false);
            ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "manageteachersdismiss");
        }

        return rootView;
    }

    void initBottomLayout(){
        // EditText user, fname, lname, gender, dob, startdate;
        user = (EditText)rootView.findViewById(R.id._username);
        fname = (EditText)rootView.findViewById(R.id._fname);
        lname = (EditText)rootView.findViewById(R.id._lname);
        gender= (EditText)rootView.findViewById(R.id._gender);
        pword = (EditText)rootView.findViewById(R.id._password);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);

        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
            //showDate(year, month + 1, day);

        dob = (EditText)rootView.findViewById(R.id._dob);
        dob.setClickable(false);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_date = "dob";
                DialogFragment dialogfragment = new DatePickerDialogTheme1();

                dialogfragment.show(getFragmentManager(), "Theme 1");
            }
        });

        startdate = (EditText)rootView.findViewById(R.id._startdate);
        startdate.setClickable(false);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_date = "startdate";
                DialogFragment dialogfragment = new DatePickerDialogTheme1();

                dialogfragment.show(getFragmentManager(), "Theme 1");
            }
        });

        add = (Button)rootView.findViewById(R.id._add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatePickerDialog dp = new DatePickerDialog();
                if(gender.getText().toString().equalsIgnoreCase("male") || gender.getText().toString().equalsIgnoreCase("male")){
                    userSt = user.getText().toString();
                    pwordSt = pword.getText().toString();
                    fnameSt = fname.getText().toString();
                    lnameSt = lname.getText().toString();
                    genderSt = gender.getText().toString();
                    dobSt = ""+getUnixTime(dob.getText().toString());
                    startdateSt = ""+getUnixTime(startdate.getText().toString());

                    if(userSt!=null && pwordSt!=null && fnameSt!=null && lnameSt!=null && genderSt!=null && dobSt!=null && startdateSt!=null && dobSt!="" && startdateSt!="" ){
                        addTeachers();
                    }else{
                        Snackbar.make(rootView, "Empty or Invalid fields", Snackbar.LENGTH_LONG)
                                .setAction("Error", null).show();
                    }
                }else{
                    Snackbar.make(rootView, "Gender must be Male or Female!", Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                }

            }
        });
    }

    void getTeacherList(){
        ((Home)getActivity()).progressbarStartRetrieve("Teachers");
        String tag_json_obj_teachers = "json_obj_req_getteachers";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Teachers Retrieval", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Teachers Retrieval", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar
                        mSwipeRefreshLayout.setRefreshing(false);
                        if(response != null){
                            Log.e("Teachers", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Teachers", response.toString());
                                showListview(data);

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("No teachers found. Please check back later!", "manageteachers");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "manageteachers");
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
                ((Home)getActivity()).dialogError("Please try again!", "manageteachers");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "10");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_teachers);
    }


    private void showListview(JSONObject data) throws JSONException {
        JSONArray response = null;
        try {
            response = data.getJSONArray("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        histlist.clear();
        for(int i = 0; i < response.length(); i++) {
            JSONObject c = response.getJSONObject(i);

            // Storing JSON item in a Variable
            String dateofbirth = c.getString(TAG_DOB);
            dateofbirth = getCurrentTimeStamp(dateofbirth);


            String access = c.getString(TAG_ACCESS);
            String teachername = c.getString(TAG_FNAME) + " " + c.getString(TAG_LNAME);
            //Toast.makeText(getActivity().getApplicationContext(), "Teacher name"+ teachername , Toast.LENGTH_SHORT).show();
            String username = c.getString(TAG_USRNAME);

            String gender = c.getString(TAG_GENDER);

            RowItemsMTeachers items = new RowItemsMTeachers();
            items.setDateofbirth(dateofbirth);
            items.setTeacher_access("Status: "+access);
            items.setTeacher_name(teachername);
            items.setUsername(getResources().getString(R.string.atsymbol) + username);
            items.setGender(gender);
            histlist.add(items);

            adapter = new RecyclerViewAdapterMTeacher(getActivity(), histlist);

            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            //recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));//set height of 2 between row items

            // set the adapter
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new Home.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new Home.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //bring up dialog button
                    TextView _hasaccess = (TextView)recyclerView.getChildAt(position).findViewById(R.id._taccess);
                    TextView _username = (TextView)recyclerView.getChildAt(position).findViewById(R.id._tusername);

                    String hasaccess = _hasaccess.getText().toString().substring(8);
                    String username = _username.getText().toString().substring(1);

                    //Toast.makeText(getActivity().getApplicationContext(), "Has Access"+ hasaccess + " >> Username :: "+username, Toast.LENGTH_SHORT).show();
                    if(netCheck.isConnected()) {
                        teacherAction(username);
                    }else{
                        mSwipeRefreshLayout.setRefreshing(false);
                        ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "manageteachersdismiss");
                    }
                }

                @Override
                public void onLongClick(View view, int position) {
                   /* //bring up dialog button
                    TextView _hasaccess = (TextView)recyclerView.getChildAt(position).findViewById(R.id._taccess);
                    TextView _username = (TextView)recyclerView.getChildAt(position).findViewById(R.id._tusername);

                    String hasaccess = _hasaccess.getText().toString().substring(8);
                    String username = _username.getText().toString().substring(1);

                    //Toast.makeText(getActivity().getApplicationContext(), "Has Access"+ hasaccess + " >> Username :: "+username, Toast.LENGTH_SHORT).show();
                    teacherAction(username);*/
                }
            }));
        }

    }

    public static String getCurrentTimeStamp(String dates) {
        long date = Long.parseLong(dates);
        Date d=new Date(((long)date)*1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }


    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

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
    }

    void teacherAction(final String username){
        dialog = new MaterialDialog.Builder(getActivity())
                .title("Perform Action")
                .items(R.array.taction)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            perfTeacherAction("Block", username);
                        }else if (which == 1) {
                            perfTeacherAction("Delete", username);
                        }else if (which == 3) {
                            perfTeacherAction("Reset", username);
                        }
                        dialog.dismiss();
                    }
                })
                //.positiveText("Continue")
                .show();
    }

    void perfTeacherAction(final String action, final String user){
        if (action.equals("Block")) {
            ((Home)getActivity()).progressbarStartAction("Block Teacher", "Blocking...");
        }else if (action.equals("Delete")) {
            ((Home)getActivity()).progressbarStartAction("Delete Teacher", "Deleting...");
        }else if (action.equals("Reset")) {
            ((Home)getActivity()).progressbarStartAction("Reset Teacher", "Resetting...");
        }

        String tag_json_obj_actonteachers = "json_obj_req_actonteachers";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Teachers Retrieval", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Teachers Retrieval", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("Teachers", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Teachers", response.toString());

                                Snackbar.make(rootView, "Action performed successfully!", Snackbar.LENGTH_LONG)
                                        .setAction("Result", null).show();

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("Action couldn't be performed. Please try again!", "actteachers");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "actteachers");
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
                ((Home)getActivity()).dialogError("Please try again!", "actteachers");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "11");
                params.put("action", action);
                params.put("username", user);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_actonteachers);
    }

    void addTeachers(){
        ((Home)getActivity()).progressbarStartAction("Adding teacher","Wait while server performs request...");
        String tag_json_obj_actonteachers = "json_obj_req_addteachers";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Teachers Add", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Teachers Addition", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("Teachers", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Teachers", response.toString());

                                ((Home)getActivity()).dialogDisplay("Result", data.getString(TAG_SERVER_MSG));

                                Snackbar.make(rootView, "Teacher addition was successful!", Snackbar.LENGTH_LONG)
                                        .setAction("Result", null).show();

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("Action couldn't be performed. Please try again!", "addteachers");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "addteachers");
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
                ((Home)getActivity()).dialogError("Please try again!", "addteachers");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "12");

                params.put("username", userSt);
                params.put("password", pwordSt);
                params.put("fname", fnameSt);
                params.put("lname", lnameSt);
                params.put("gender", genderSt);
                params.put("dob", dobSt);
                params.put("startdate", startdateSt);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_actonteachers);
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
            if(which_date.equals("dob"))
                dob.setText(day + "/" + (month+1) + "/" + year);
            else if(which_date.equals("startdate"))
                startdate.setText(day + "/" + (month+1) + "/" + year);
            else
                dob.setText(day + "/" + (month+1) + "/" + year);
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
