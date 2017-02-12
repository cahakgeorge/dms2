package com.daniellasmontesssorischool.dms.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniellasmontesssorischool.dms.R;

import java.util.ArrayList;


//gotten from http://www.appifiedtech.net/2015/02/15/android-recyclerview-example/
public class RecyclerViewAdapterCalendar extends RecyclerView.Adapter<RowViewHolderCalendar> {
    Context context;
    ArrayList<RowItemsCalendar> itemsList;

    public RecyclerViewAdapterCalendar(Context context, ArrayList<RowItemsCalendar> itemsList) {
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
    public RowViewHolderCalendar onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_calendar, null);
        RowViewHolderCalendar viewHolder = new RowViewHolderCalendar(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RowViewHolderCalendar rowViewHolder, int position) {
        RowItemsCalendar items = itemsList.get(position);

        rowViewHolder.calimage.setBackgroundResource(Integer.valueOf(items.getBackpic()));
        rowViewHolder.textViewDay.setText(String.valueOf(items.getCal_day()));
        rowViewHolder.textViewMonth.setText(String.valueOf(items.getCal_month()));
        rowViewHolder.textViewImgTitle.setText(String.valueOf(items.getCalimg_title()));

        rowViewHolder.textViewHeader.setText(String.valueOf(items.getCal_title()));
        rowViewHolder.textViewDetail.setText(String.valueOf(items.getCal_detail()));
        rowViewHolder.textViewInterval.setText(String.valueOf(items.getCal_interval()));
    }
}