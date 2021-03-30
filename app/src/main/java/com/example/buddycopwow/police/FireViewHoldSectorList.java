package com.example.buddycopwow.police;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddycopwow.R;


public class FireViewHoldSectorList extends RecyclerView.ViewHolder {
    TextView sectorName;

    public FireViewHoldSectorList(@NonNull View itemView) {
        super(itemView);
        sectorName = itemView.findViewById(R.id.sectorNameRecycler);
    }
}
