package com.daniellasmontesssorischool.dms.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daniellasmontesssorischool.dms.R;

public class RowViewHolderEvent extends RecyclerView.ViewHolder {

    TextView textViewParticipant;
    TextView textViewHeader;
    TextView textViewDetail;
    TextView textViewMonth;
    TextView textViewDay;
    LinearLayout l2;


    public RowViewHolderEvent(View view) {
        super(view);
        this.l2 = (LinearLayout)view. findViewById(R.id.eventleft);//for changing row colors
        this.textViewParticipant = (TextView) view.findViewById(R.id._event_participants);
        this.textViewHeader = (TextView) view.findViewById(R.id._event_header);
        this.textViewDetail = (TextView) view.findViewById(R.id._event_detail);
        this.textViewMonth = (TextView) view.findViewById(R.id._event_month);
        this.textViewDay = (TextView) view.findViewById(R.id._event_day);
    }
}
