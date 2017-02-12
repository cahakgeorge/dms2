package com.daniellasmontesssorischool.dms.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daniellasmontesssorischool.dms.R;

public class RowViewHolder extends RecyclerView.ViewHolder {

    TextView textViewDate;
    TextView textViewTitle;
    TextView textViewName;
    TextView textViewClass;
    TextView textViewType;

    public RowViewHolder(View view) {
        super(view);
        this.textViewDate = (TextView) view.findViewById(R.id._date);
        this.textViewTitle = (TextView) view.findViewById(R.id._title);
        this.textViewName = (TextView) view.findViewById(R.id._name);
        this.textViewClass = (TextView) view.findViewById(R.id._class);
        this.textViewType = (TextView) view.findViewById(R.id._type);
        //this.imageView = (ImageView) view.findViewById(R.id.image);
    }
}
