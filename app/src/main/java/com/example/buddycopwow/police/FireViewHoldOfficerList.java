package com.example.buddycopwow.police;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddycopwow.R;


public class FireViewHoldOfficerList extends RecyclerView.ViewHolder {
    TextView Name, Designation;

    public FireViewHoldOfficerList(@NonNull View itemView) {
        super(itemView);
        Name = itemView.findViewById(R.id.officerNameRecycler);
        Designation = itemView.findViewById(R.id.officerDesignationRecycler);
    }
}
