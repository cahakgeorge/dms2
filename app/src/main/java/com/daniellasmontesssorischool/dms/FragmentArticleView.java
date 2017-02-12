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
public class FragmentArticleView extends Fragment {

    TextView _artname, _stclass, _arttitle, _artcontent, _artdate;

    Intent calldms;

    Button _upload, _directions;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_article_view, container, false);

        _artname = (TextView)rootView.findViewById(R.id._name);
        _stclass = (TextView)rootView.findViewById(R.id._class);
        _arttitle = (TextView)rootView.findViewById(R.id._title);
        _artcontent = (TextView)rootView.findViewById(R.id._content);
        _artdate = (TextView)rootView.findViewById(R.id._date);

        _artname.setText(((Home)getActivity()).articleview.get(3));
        _stclass.setText(((Home)getActivity()).articleview.get(4));
        _arttitle.setText(((Home)getActivity()).articleview.get(2));
        _artcontent.setText(((Home)getActivity()).articleview.get(5));
        _artdate.setText(" ("+((Home)getActivity()).articleview.get(0)+")");

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
