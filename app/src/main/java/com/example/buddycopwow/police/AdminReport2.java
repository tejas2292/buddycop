package com.example.buddycopwow.police;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.UploadAttendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminReport2 extends AppCompatActivity {
    DatabaseReference reference;
    List<UploadAttendance> reportList;
    ListReportAdapter adapterReport;
    ListView myListView;
    String startDate;
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Reports");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        startDate = getIntent().getStringExtra("start");
        reference = FirebaseDatabase.getInstance().getReference().child("attendance").child(startDate);
        mLinearLayout = findViewById(R.id.layoutNotAdded);

        myListView = findViewById(R.id.myListView);
        reportList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mLinearLayout.setVisibility(View.INVISIBLE);
                    reportList.clear();
                    for (DataSnapshot employeeDatasnap : snapshot.getChildren()) {
                        UploadAttendance employee = employeeDatasnap.getValue(UploadAttendance.class);
                        reportList.add(employee);
                    }
                    adapterReport = new ListReportAdapter(AdminReport2.this, reportList);
                    myListView.setAdapter(adapterReport);
                } else {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(AdminReport2.this, "No attendance is taken on this date", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminReport2.this, AdminReport.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}