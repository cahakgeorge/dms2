package com.daniellasmontesssorischool.dms.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daniellasmontesssorischool.dms.R;

import java.util.ArrayList;


//gotten from http://www.appifiedtech.net/2015/02/15/android-recyclerview-example/
public class RecyclerViewAdapterMTeacher extends RecyclerView.Adapter<RowViewHolderMTeachers> {
    Context context;
    ArrayList<RowItemsMTeachers> itemsList;

    public RecyclerViewAdapterMTeacher(Context context, ArrayList<RowItemsMTeachers> itemsList) {
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
    public RowViewHolderMTeachers onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_teachers, null);
        RowViewHolderMTeachers viewHolder = new RowViewHolderMTeachers(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolderMTeachers rowViewHolder, int position) {
        RowItemsMTeachers items = itemsList.get(position);
        rowViewHolder.textViewDateofBirth.setText(String.valueOf(items.getDateofbirth()));
        rowViewHolder.textViewAccess.setText(String.valueOf(items.getTeacher_access()));
        rowViewHolder.textViewTName.setText(String.valueOf(items.getTeacher_name()));
        rowViewHolder.textViewUsername.setText(String.valueOf(items.getUsername()));
        rowViewHolder.textViewGender.setText(String.valueOf(items.getGender()));
    }
}