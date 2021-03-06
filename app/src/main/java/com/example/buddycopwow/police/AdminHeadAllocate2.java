package com.example.buddycopwow.police;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.UploadDuty;
import com.example.buddycopwow.Uploads.UploadSector;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class AdminHeadAllocate2 extends AppCompatActivity {
    DatabaseReference reference, reference2;
    RecyclerView recyclerView;
    ArrayList<UploadSector> arrayListHistory;
    FirebaseRecyclerOptions<UploadSector> options;
    FirebaseRecyclerAdapter<UploadSector, FireViewHoldSectorList> adapterHistory;
    EditText searchOfficer;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_head_allocate2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Head Allocate To Sector");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference("sector");
        reference2 = FirebaseDatabase.getInstance().getReference("duty");

        uid = getIntent().getStringExtra("uid");

        searchOfficer = findViewById(R.id.searchViewOfficer);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminHeadAllocate2.this));
        recyclerView.setHasFixedSize(true);
        arrayListHistory = new ArrayList<UploadSector>();


        LoadData("");
        searchOfficer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    LoadData(s.toString());
                } else {
                    LoadData("");
                }
            }
        });
    }

    private void LoadData(final String data) {
        Query query1 = reference.orderByChild("sectorName").startAt(data).endAt(data + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<UploadSector>().setQuery(query1, UploadSector.class).build();
        adapterHistory = new FirebaseRecyclerAdapter<UploadSector, FireViewHoldSectorList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FireViewHoldSectorList holder, int position, @NonNull final UploadSector model) {

                String temp = model.getSectorHeadName();

                if (temp.equals("-")) {
                    holder.sectorName.setText(model.getSectorName());
                    holder.itemView.setBackground(getResources().getDrawable(R.drawable.button2));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AdminHeadAllocate2.this);
                            builder.setMessage("Are you sure want to allocate sector head here..");
                            builder.setCancelable(false);
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reference.child(model.getSectorName()).child("sectorHeadName").setValue(uid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            final UploadDuty u = new UploadDuty(model.getSectorName(), "Sector Head", uid);
                                            reference2.child(uid).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(AdminHeadAllocate2.this, "Successfully allocated..", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AdminHeadAllocate2.this, AdminHomeScreen.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                    });
                }
                else {
                    holder.itemView.setBackground(getResources().getDrawable(R.drawable.button3));
                    holder.sectorName.setText(model.getSectorName());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(AdminHeadAllocate2.this, "This sector have allocated officer", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }


            @NonNull
            @Override
            public FireViewHoldSectorList onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FireViewHoldSectorList(LayoutInflater.from(AdminHeadAllocate2.this).inflate(R.layout.row_sector, viewGroup, false));
            }
        };
        adapterHistory.startListening();
        recyclerView.setAdapter(adapterHistory);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminHeadAllocate2.this, AdminHeadAllocate.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}