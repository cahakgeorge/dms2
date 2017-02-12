package com.daniellasmontesssorischool.dms.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniellasmontesssorischool.dms.R;

import java.util.ArrayList;


//gotten from http://www.appifiedtech.net/2015/02/15/android-recyclerview-example/
public class RecyclerViewAdapterEvent extends RecyclerView.Adapter<RowViewHolderEvent> {
    Context context;
    ArrayList<RowItemsEvent> itemsList;

    public RecyclerViewAdapterEvent(Context context, ArrayList<RowItemsEvent> itemsList) {
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
    public RowViewHolderEvent onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_event, null);
        RowViewHolderEvent viewHolder = new RowViewHolderEvent(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolderEvent rowViewHolder, int position) {
        RowItemsEvent items = itemsList.get(position);

        rowViewHolder.l2.setBackgroundColor(Integer.valueOf(items.getEvent_col()));
        rowViewHolder.textViewParticipant.setText(String.valueOf(items.getParticipants()));
        rowViewHolder.textViewHeader.setText(String.valueOf(items.getEvent_header()));
        rowViewHolder.textViewDetail.setText(String.valueOf(items.getEvent_detail()));
        rowViewHolder.textViewMonth.setText(String.valueOf(items.getEvent_month()));
        rowViewHolder.textViewDay.setText(String.valueOf(items.getEvent_day()));
    }
}