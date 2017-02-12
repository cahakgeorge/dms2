package com.daniellasmontesssorischool.dms;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsView extends Fragment {

    TextView _newstitle, _newsdate, _newsdetail, _newstype;

    Intent calldms;

    Button _upload, _directions;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_newsview, container, false);

        _newstitle = (TextView)rootView.findViewById(R.id._title);
        _newsdate = (TextView)rootView.findViewById(R.id._news_date);
        _newsdetail = (TextView)rootView.findViewById(R.id._detail);
        _newstype = (TextView)rootView.findViewById(R.id._type);

        _newstitle.setText(((Home)getActivity()).newsview.get(0));
        _newsdate.setText(((Home)getActivity()).newsview.get(2));
        _newsdetail.setText(((Home)getActivity()).newsview.get(1));
        _newstype.setText(((Home)getActivity()).newsview.get(3));

        return rootView;
    }

    /*public static String getCurrentTimeStamp(String dates) {
        long date = Long.parseLong(dates);
        Date d=new Date(((long)date)*1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }*/

}
