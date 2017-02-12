package com.daniellasmontesssorischool.dms.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniellasmontesssorischool.dms.R;

import java.util.ArrayList;


//gotten from http://www.appifiedtech.net/2015/02/15/android-recyclerview-example/
public class RecyclerViewAdapterNews extends RecyclerView.Adapter<RowViewHolderNews> {
    Context context;
    ArrayList<RowItemsNews> itemsList;

    public RecyclerViewAdapterNews(Context context, ArrayList<RowItemsNews> itemsList) {
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
    public RowViewHolderNews onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_news, null);
        RowViewHolderNews viewHolder = new RowViewHolderNews(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolderNews rowViewHolder, int position) {
        RowItemsNews items = itemsList.get(position);
        rowViewHolder.textViewTitle.setTextColor(Integer.valueOf(items.getNewstitle_col()));
        rowViewHolder.textViewContent.setTextColor(Integer.valueOf(items.getNewsdetail_col()));
        rowViewHolder.textViewDate.setTextColor(Integer.valueOf(items.getNewsbottom_col()));
        rowViewHolder.textViewType.setTextColor(Integer.valueOf(items.getNewsbottom_col()));

        rowViewHolder.textViewDate.setText(String.valueOf(items.getDate()));
        rowViewHolder.textViewTitle.setText(String.valueOf(items.getNews_title()));
        rowViewHolder.textViewContent.setText(String.valueOf(items.getNews_content()));
        rowViewHolder.textViewType.setText(String.valueOf(items.getNews_type()));

    }
}