package com.example.buddycopwow.police;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.PoliceRegestrationUpload;
import com.example.buddycopwow.Uploads.UploadAttendance;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TakeAttendance extends AppCompatActivity {
    DatabaseReference reference, reference2, reference3;
    RecyclerView recyclerView;
    ArrayList<PoliceRegestrationUpload> arrayListHistory;
    FirebaseRecyclerOptions<PoliceRegestrationUpload> options;
    FirebaseRecyclerAdapter<PoliceRegestrationUpload, FireViewHoldOfficerList> adapterHistory;
    EditText searchOfficer;
    String sectorName, sectorHeadUid, latitude = "-", longitude = "-";
    String tempFirstName, tempLastName, tempDesignation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Take Attendance");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference("credentials").child("police");
        reference2 = FirebaseDatabase.getInstance().getReference("duty");
        reference3 = FirebaseDatabase.getInstance().getReference("attendance");
        searchOfficer = findViewById(R.id.searchViewOfficer);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(TakeAttendance.this));
        recyclerView.setHasFixedSize(true);
        arrayListHistory = new ArrayList<PoliceRegestrationUpload>();

        sectorName = getIntent().getStringExtra("sectorName");
        sectorHeadUid = getIntent().getStringExtra("sectorHeadUid");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(ContextCompat.checkSelfPermission(TakeAttendance.this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TakeAttendance.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        else {
            getCurrentLoacation();
        }

        LoadData(sectorName);

    }

    private void LoadData(final String data) {
        Query query1 = reference2.orderByChild("sectorName").startAt(data).endAt(data + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<PoliceRegestrationUpload>().setQuery(query1, PoliceRegestrationUpload.class).build();
        adapterHistory = new FirebaseRecyclerAdapter<PoliceRegestrationUpload, FireViewHoldOfficerList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FireViewHoldOfficerList holder, int position, @NonNull final PoliceRegestrationUpload model) {

                reference.child(model.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tempFirstName = snapshot.child("firstName").getValue().toString();
                        tempLastName = snapshot.child("lastName").getValue().toString();
                        tempDesignation = snapshot.child("designation").getValue().toString();

                        if (tempDesignation.equals("Constable")) {
                            holder.itemView.setVisibility(View.VISIBLE);
                            holder.Name.setText(tempFirstName + " " + tempLastName);
                            holder.Designation.setText(tempDesignation);
                            String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                            reference3.child(date1).child(model.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String tempStatus = snapshot.child("status").getValue().toString();

                                        if(tempStatus.equals("present")){
                                            holder.itemView.setBackground(getResources().getDrawable(R.drawable.button4));
                                        }
                                        else if(tempStatus.equals("absent")){
                                            holder.itemView.setBackground(getResources().getDrawable(R.drawable.button3));
                                        }

                                    } else {
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                                                popupMenu.inflate(R.menu.popup_menu);
                                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                    @Override
                                                    public boolean onMenuItemClick(MenuItem item) {
                                                        switch (item.getItemId()) {
                                                            case R.id.open:
                                                                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                                                Long tsLong = System.currentTimeMillis() / 1000;
                                                                String ts = tsLong.toString();

                                                                final UploadAttendance u = new UploadAttendance(sectorHeadUid, model.getUid(), sectorName, sectorHeadUid,
                                                                        date, ts, latitude, longitude, "present");

                                                                reference3.child(date).child(model.getUid()).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(TakeAttendance.this, "Attendance Marked successfully..", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(TakeAttendance.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                                return true;
                                                            case R.id.delete:
                                                                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                                                Long tsLong1 = System.currentTimeMillis() / 1000;
                                                                String ts1 = tsLong1.toString();

                                                                final UploadAttendance u1 = new UploadAttendance(sectorHeadUid, model.getUid(), sectorName, sectorHeadUid,
                                                                        date1, ts1, latitude, longitude, "absent");

                                                                reference3.child(date1).child(model.getUid()).setValue(u1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(TakeAttendance.this, "Attendance Marked successfully..", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(TakeAttendance.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                                return true;
                                                        }
                                                        return true;
                                                    }
                                                });
                                                popupMenu.show();
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


            @NonNull
            @Override
            public FireViewHoldOfficerList onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FireViewHoldOfficerList(LayoutInflater.from(TakeAttendance.this).inflate(R.layout.row_officer, viewGroup, false));
            }
        };
        adapterHistory.startListening();
        recyclerView.setAdapter(adapterHistory);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLoacation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(TakeAttendance.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                        latitude = String.valueOf(addresses.get(0).getLatitude());
                        longitude = String.valueOf(addresses.get(0).getLongitude());

                        Toast.makeText(TakeAttendance.this, ""+latitude+""+longitude, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TakeAttendance.this, PoliceHomeScreen.class));
        finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}