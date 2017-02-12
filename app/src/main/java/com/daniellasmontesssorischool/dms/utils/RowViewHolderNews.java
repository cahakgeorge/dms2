package com.daniellasmontesssorischool.dms.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daniellasmontesssorischool.dms.R;

public class RowViewHolderNews extends RecyclerView.ViewHolder {
    TextView textViewDate;
    TextView textViewTitle;
    TextView textViewContent;
    TextView textViewType;


    public RowViewHolderNews(View view) {
        super(view);
        this.textViewDate = (TextView) view.findViewById(R.id._news_date);
        this.textViewTitle = (TextView) view.findViewById(R.id._news_header);
        this.textViewContent = (TextView) view.findViewById(R.id._news_detail);
        this.textViewType = (TextView) view.findViewById(R.id._news_type);
        //this.imageView = (ImageView) view.findViewById(R.id.image);
    }
}
