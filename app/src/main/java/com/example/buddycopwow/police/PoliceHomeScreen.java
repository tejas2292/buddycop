package com.example.buddycopwow.police;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.buddycopwow.CitizenCrimeMapActivity;
import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.UploadAttendance;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PoliceHomeScreen extends AppCompatActivity {
    public DatabaseReference reference, referenceCred;
    DatabaseReference reference2, reference3;
    FirebaseAuth fAuth;
    String uid, selfName;
    String role;
    String sectorName;
    String latitude = "-";
    String longitude = "-", isInside = "false";
    TextView mRole, mSectorName;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLng;
    double circle_x, circle_y, radius = 0.01, lat, lon;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_home_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Screen Police");
        setSupportActionBar(toolbar);

        loadingDialog = new LoadingDialog(PoliceHomeScreen.this);
        fAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("duty");
        reference2 = FirebaseDatabase.getInstance().getReference("sector");
        reference3 = FirebaseDatabase.getInstance().getReference("attendance");
        referenceCred = FirebaseDatabase.getInstance().getReference("credentials").child("police");


        uid = fAuth.getCurrentUser().getUid();

        mRole = findViewById(R.id.roleName);
        mSectorName = findViewById(R.id.sectorName);
        linearLayout = findViewById(R.id.linearLayout1);
        relativeLayout = findViewById(R.id.relativeLayout1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    role = snapshot.child("role").getValue().toString();
                    sectorName = snapshot.child("sectorName").getValue().toString();

                    reference2.child(sectorName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String tempLat, tempLan;
                                tempLat = snapshot.child("lat").getValue().toString();
                                tempLan = snapshot.child("lan").getValue().toString();

                                circle_x = Double.parseDouble(tempLat);
                                circle_y = Double.parseDouble(tempLan);

                                mRole.setText(role);
                                mSectorName.setText(sectorName);

                                linearLayout.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referenceCred.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String t1=  snapshot.child("firstName").getValue().toString();
                    String t2 = snapshot.child("lastName").getValue().toString();

                    selfName = t1 +" "+ t2;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onTakeAttendance(View view) {
        if (role.equals("Sector Head")) {
            Intent intent = new Intent(PoliceHomeScreen.this, TakeAttendance.class);
            intent.putExtra("sectorName", sectorName);
            intent.putExtra("sectorHeadUid", uid);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "You don't have authority to take others officers attendance..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PoliceHomeScreen.this);
        builder.setMessage("Are you sure want to exit from app?");
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
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //here exit app alert close............................................
    }

    public void onSelfAttendance(View view) {
        if (ContextCompat.checkSelfPermission(PoliceHomeScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PoliceHomeScreen.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        } else {
            String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            reference3.child(date1).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String tempStatus = snapshot.child("status").getValue().toString();


                        if (tempStatus.equals("present")) {
                            Toast.makeText(PoliceHomeScreen.this, "Attendance is already done", Toast.LENGTH_SHORT).show();
                        } else if (tempStatus.equals("absent")) {
                            Toast.makeText(PoliceHomeScreen.this, "Attendance is already done", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        getCurrentLoacation();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }


    @SuppressLint("MissingPermission")
    private void getCurrentLoacation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(PoliceHomeScreen.this, Locale.getDefault());

                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                        latitude = String.valueOf(addresses.get(0).getLatitude());
                        longitude = String.valueOf(addresses.get(0).getLongitude());

                        lat = Double.parseDouble(latitude);
                        lon = Double.parseDouble(longitude);
                        isInside(circle_x, circle_y, radius, lat, lon);
                        Toast.makeText(PoliceHomeScreen.this, "" + latitude + "" + longitude, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void isInside(double circle_x, double circle_y, double radius, double x, double y) {
        if ((x - circle_x) * (x - circle_x) + (y - circle_y) * (y - circle_y) <= radius * radius) {
            isInside = "true";
            loadingDialog.startLoadingDialog();
            loadingDialog.setText("Creating Sector..");

            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            final UploadAttendance u = new UploadAttendance("self", selfName, sectorName, "-",
                    date, ts, latitude, longitude, "present", uid);

            reference3.child(date).child(uid).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(PoliceHomeScreen.this, "Attendance Marked successfully..", Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(PoliceHomeScreen.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "You are not present at sector.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCreateFir(View view) {
        startActivity(new Intent(PoliceHomeScreen.this, PoliceCreateFir.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), PoliceLogin.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSendCrimeMap(View view) {
        startActivity(new Intent(PoliceHomeScreen.this, CitizenCrimeMapActivity.class));
        finish();
    }

    public void onWantedDatabase(View view) {
        startActivity(new Intent(PoliceHomeScreen.this, PoliceWantedDB.class));
        finish();
    }
}