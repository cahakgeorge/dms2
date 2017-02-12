package com.daniellasmontesssorischool.dms;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.utils.RecyclerViewAdapterCalendar;
import com.daniellasmontesssorischool.dms.utils.RowItemsCalendar;
import com.daniellasmontesssorischool.dms.utils.RowItemsCurriculum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * kayode - swift
 * ola - python
 * iyanu - java
 */
public class FragmentCurriculum extends Fragment {

    ArrayList<RowItemsCurriculum> histlist = new ArrayList<>();
    private static final String TAG_DAY = "dates";
    private static final String TAG_MONTH = "dates";
    private static final String TAG_PICTITLE = "dates";
    private static final String TAG_HEADER = "title";
    private static final String TAG_DETAIL = "added_by";
    private static final String TAG_INTERVAL = "added_by";

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";

    //ArrayList<String> calendarContent = new ArrayList<>();


    RecyclerView recyclerView;
    ArrayList<RowItemsCalendar> itemsList = new ArrayList<>();
    RecyclerViewAdapterCalendar adapter;
    LinearLayout _sub1, _sub2, _sub3, _sub4, _sub5, _sub6, _sub7, _sub8, _sub9;

    Button _upload;

    NetworkCheck netCheck;
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_curriculum, container, false);

        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());

        return rootView;
    }

    public static String getCurrentTimeStamp(String dates) {
        long date = Long.parseLong(dates);
        Date d=new Date(((long)date)*1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }

}
