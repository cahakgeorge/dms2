package com.daniellasmontesssorischool.dms;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAssignmentView extends Fragment {

    TextView _asssubject,_asstopic, _assclass, _asssubdate, _assquestion, _assqextra, _byteacher;

    //Button _upload, _directions;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_assignmentview, container, false);

        _asssubject = (TextView)rootView.findViewById(R.id._subject);
        _asstopic = (TextView)rootView.findViewById(R.id._topic);
        _assclass = (TextView)rootView.findViewById(R.id._class);
        _asssubdate = (TextView)rootView.findViewById(R.id._submDate);
        _assquestion = (TextView)rootView.findViewById(R.id._qdetail);
        _assqextra = (TextView)rootView.findViewById(R.id._qextra);
        _byteacher = (TextView)rootView.findViewById(R.id._qteacher);


        _asssubject.setText(((Home)getActivity()).assignmentview.get(0));
        _asstopic.setText(((Home)getActivity()).assignmentview.get(1));
        _assclass.setText(((Home)getActivity()).assignmentview.get(6));
        _asssubdate.setText(((Home)getActivity()).assignmentview.get(4));
        _assquestion.setText(((Home)getActivity()).assignmentview.get(2));
        _assqextra.setText(((Home)getActivity()).assignmentview.get(3));
        _byteacher.setText(((Home)getActivity()).assignmentview.get(5));

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
