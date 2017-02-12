package com.daniellasmontesssorischool.dms.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniellasmontesssorischool.dms.R;

import java.util.ArrayList;


//gotten from http://www.appifiedtech.net/2015/02/15/android-recyclerview-example/
public class RecyclerViewAdapter extends RecyclerView.Adapter<RowViewHolder> {
    Context context;
    ArrayList<RowItems> itemsList;

    public RecyclerViewAdapter(Context context, ArrayList<RowItems> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        } else {
            return itemsList.size();
        }
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_articles, null);
        RowViewHolder viewHolder = new RowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolder rowViewHolder, int position) {
        RowItems items = itemsList.get(position);
        rowViewHolder.textViewDate.setText(String.valueOf(items.getDate()));
        rowViewHolder.textViewTitle.setText(String.valueOf(items.getTitle()));
        rowViewHolder.textViewTitle.setTag(String.valueOf(items.getArticle_content()));
        rowViewHolder.textViewName.setText(String.valueOf(items.getStud_name()));
        rowViewHolder.textViewClass.setText(String.valueOf(items.getStud_class()));
        rowViewHolder.textViewType.setText(String.valueOf(items.getArticle_type()));
    }
}