package com.daniellasmontesssorischool.dms.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniellasmontesssorischool.dms.R;

import java.util.ArrayList;


//gotten from http://www.appifiedtech.net/2015/02/15/android-recyclerview-example/
public class RecyclerViewAdapterAssignment extends RecyclerView.Adapter<RowViewHolderAssignment> {
    Context context;
    ArrayList<RowItemsAssignment> itemsList;

    public RecyclerViewAdapterAssignment(Context context, ArrayList<RowItemsAssignment> itemsList) {
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
    public RowViewHolderAssignment onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_assignment, null);
        RowViewHolderAssignment viewHolder = new RowViewHolderAssignment(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolderAssignment rowViewHolder, int position) {
        RowItemsAssignment items = itemsList.get(position);

        rowViewHolder.assback.setBackgroundColor(Integer.valueOf(items.getLin_assignleft()));
        rowViewHolder.textViewDate.setText(String.valueOf(items.getDate()));

        rowViewHolder.textViewTitle.setText(String.valueOf(items.getTitle()));
        rowViewHolder.textViewTitle.setTag(String.valueOf(items.getAss_qtopic()));

        rowViewHolder.textViewClass.setText(String.valueOf(items.getAss_class()));

        rowViewHolder.textViewContent.setText(String.valueOf(items.getAss_content()));
        rowViewHolder.textViewContent.setTag(String.valueOf(items.getAss_extrainfo()));

        rowViewHolder.textViewTeacher.setText(String.valueOf(items.getAss_teacher()));
        rowViewHolder.textViewInitials.setText(String.valueOf(items.getAss_initials()));
    }
}