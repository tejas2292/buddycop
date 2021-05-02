package com.example.buddycopwow.police;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.PoliceRegestrationUpload;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

public class AdminHeadAllocate extends AppCompatActivity {
    DatabaseReference reference, reference2;
    RecyclerView recyclerView;
    ArrayList<PoliceRegestrationUpload> arrayListHistory;
    FirebaseRecyclerOptions<PoliceRegestrationUpload> options;
    FirebaseRecyclerAdapter<PoliceRegestrationUpload, FireViewHoldOfficerList> adapterHistory;
    EditText searchOfficer;
    SearchableSpinner spinner;
    String designation;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_head_allocate);

        reference = FirebaseDatabase.getInstance().getReference("credentials").child("police");
        reference2 = FirebaseDatabase.getInstance().getReference("duty");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Head Allocate");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminHeadAllocate.this));
        recyclerView.setHasFixedSize(true);

        searchOfficer = findViewById(R.id.searchViewOfficer);

        spinner = findViewById(R.id.spinner_designation_name);
        arrayListHistory = new ArrayList<PoliceRegestrationUpload>();

        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList = new ArrayList<>();
        spinnerDataList.add(0, "DSP");
        spinnerDataList.add("PI");
        spinnerDataList.add("Constable");

        adapter = new ArrayAdapter<String>(AdminHeadAllocate.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = spinner.getSelectedItem().toString();
                Toast.makeText(AdminHeadAllocate.this, "" + designation, Toast.LENGTH_SHORT).show();
                LoadData(designation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        LoadData(designation);
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
        Query query1 = reference.orderByChild("designation").startAt(data).endAt(data + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<PoliceRegestrationUpload>().setQuery(query1, PoliceRegestrationUpload.class).build();
        adapterHistory = new FirebaseRecyclerAdapter<PoliceRegestrationUpload, FireViewHoldOfficerList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FireViewHoldOfficerList holder, int position, @NonNull final PoliceRegestrationUpload model) {
                String tempDesignation = model.getDesignation();

                if (tempDesignation.equals(designation)) {
                    holder.itemView.setVisibility(View.VISIBLE);

                    reference2.child(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                holder.Name.setText(model.getFirstName() + " " + model.getLastName());
                                holder.Designation.setText(model.getDesignation());

                                holder.itemView.setBackground(getResources().getDrawable(R.drawable.button3));
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(AdminHeadAllocate.this, "This officer is already allocated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                holder.Name.setText(model.getFirstName() + " " + model.getLastName());
                                holder.Designation.setText(model.getDesignation());
                                holder.itemView.setBackground(getResources().getDrawable(R.drawable.button2));

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(AdminHeadAllocate.this, AdminHeadAllocate2.class);
                                        intent.putExtra("uid", model.getUid());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }


            @NonNull
            @Override
            public FireViewHoldOfficerList onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FireViewHoldOfficerList(LayoutInflater.from(AdminHeadAllocate.this).inflate(R.layout.row_officer, viewGroup, false));
            }
        };
        adapterHistory.startListening();
        recyclerView.setAdapter(adapterHistory);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminHeadAllocate.this, AdminHomeScreen.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}