package com.daniellasmontesssorischool.dms.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daniellasmontesssorischool.dms.R;

public class RowViewHolderAssignment extends RecyclerView.ViewHolder {
    TextView textViewDate;
    TextView textViewTitle;
    TextView textViewClass;
    TextView textViewContent;
    TextView textViewTeacher;
    TextView textViewInitials;
    LinearLayout assback;


    public RowViewHolderAssignment(View view) {
        super(view);
        this.assback = (LinearLayout) view.findViewById(R.id._ini_view);
        this.textViewDate = (TextView) view.findViewById(R.id._assign_date_cont);
        this.textViewTitle = (TextView) view.findViewById(R.id._assign_title);
        this.textViewClass = (TextView) view.findViewById(R.id._assign_class);
        this.textViewContent = (TextView) view.findViewById(R.id._assign_details);
        this.textViewTeacher = (TextView) view.findViewById(R.id._assigner);
        this.textViewInitials = (TextView) view.findViewById(R.id._label);
        //this.imageView = (ImageView) view.findViewById(R.id.image);
    }
}
