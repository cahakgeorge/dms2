package com.daniellasmontesssorischool.dms.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniellasmontesssorischool.dms.R;

import java.util.ArrayList;


//gotten from http://www.appifiedtech.net/2015/02/15/android-recyclerview-example/
public class RecyclerViewAdapterCurriculum extends RecyclerView.Adapter<RowViewHolderCurriculum> {
    Context context;
    ArrayList<RowItemsCurriculum> itemsList;

    public RecyclerViewAdapterCurriculum(Context context, ArrayList<RowItemsCurriculum> itemsList) {
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
    public RowViewHolderCurriculum onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_curriculum, null);
        RowViewHolderCurriculum viewHolder = new RowViewHolderCurriculum(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolderCurriculum rowViewHolder, int position) {
        RowItemsCurriculum items = itemsList.get(position);
        rowViewHolder.textViewWeek.setText(String.valueOf(items.getWeek()));
        rowViewHolder.textViewTitle.setText(String.valueOf(items.getCurr_title()));
        rowViewHolder.textViewExtra.setText(String.valueOf(items.getCurr_extra()));
    }
}