package com.example.buddycopwow.police;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddycopwow.R;


public class FireViewHoldWantedList extends RecyclerView.ViewHolder {
    TextView name, crime, place;
    ImageView img;

    public FireViewHoldWantedList(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.wantedNameRecycler);
        crime = itemView.findViewById(R.id.wantedCrimeRecycler);
        place = itemView.findViewById(R.id.wantedPlaceRecycler);
        img = itemView.findViewById(R.id.wantedImageRecycler);
    }
}
