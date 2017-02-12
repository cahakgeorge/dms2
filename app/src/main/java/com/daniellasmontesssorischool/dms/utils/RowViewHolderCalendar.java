package com.daniellasmontesssorischool.dms.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daniellasmontesssorischool.dms.R;

public class RowViewHolderCalendar extends RecyclerView.ViewHolder {

    TextView textViewDay;
    TextView textViewMonth;
    TextView textViewImgTitle;
    TextView textViewHeader;
    TextView textViewDetail;
    TextView textViewInterval;
    LinearLayout calimage;


    public RowViewHolderCalendar(View view) {
        super(view);
        this.calimage = (LinearLayout) view.findViewById(R.id._picimage);
        this.textViewDay = (TextView) view.findViewById(R.id._pic_day);
        this.textViewMonth = (TextView) view.findViewById(R.id._pic_month);
        this.textViewImgTitle = (TextView) view.findViewById(R.id._pic_title);

        this.textViewHeader = (TextView) view.findViewById(R.id._cal_header);
        this.textViewDetail = (TextView) view.findViewById(R.id._cal_details);
        this.textViewInterval = (TextView) view.findViewById(R.id._cal_interval);
    }
}
