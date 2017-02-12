package com.daniellasmontesssorischool.dms.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daniellasmontesssorischool.dms.R;

public class RowViewHolderCurriculum extends RecyclerView.ViewHolder {

    TextView textViewWeek;
    TextView textViewTitle;
    TextView textViewExtra;


    public RowViewHolderCurriculum(View view) {
        super(view);
        this.textViewWeek = (TextView) view.findViewById(R.id._curr_day);
        this.textViewTitle = (TextView) view.findViewById(R.id._curr_header);
        this.textViewExtra = (TextView) view.findViewById(R.id._curr_extra);
        //this.imageView = (ImageView) view.findViewById(R.id.image);
    }
}
