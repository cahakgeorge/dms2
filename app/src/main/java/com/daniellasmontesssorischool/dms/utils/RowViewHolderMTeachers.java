package com.daniellasmontesssorischool.dms.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daniellasmontesssorischool.dms.R;

public class RowViewHolderMTeachers extends RecyclerView.ViewHolder {
    TextView textViewDateofBirth;
    TextView textViewAccess;
    TextView textViewTName;
    TextView textViewUsername;
    TextView textViewGender;

    public RowViewHolderMTeachers(View view) {
        super(view);
        this.textViewDateofBirth = (TextView) view.findViewById(R.id._tdob);
        this.textViewAccess = (TextView) view.findViewById(R.id._taccess);
        this.textViewTName = (TextView) view.findViewById(R.id._teacher_name);
        this.textViewUsername = (TextView) view.findViewById(R.id._tusername);
        this.textViewGender = (TextView) view.findViewById(R.id._tgender);
    }
}
