package com.example.buddycopwow.police;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.UploadFir;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class PoliceCreateFir extends AppCompatActivity {
    EditText mFirNo, mVictimName, mPlace, mLat, mLan, mWitness;
    String firNo, victimName, description, place, lat = "-", lan = "-", witness;
    DatabaseReference reference;
    SearchableSpinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_create_fir);
        reference = getInstance().getReference("fir");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("File Fir");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFirNo = findViewById(R.id.firno);
        mVictimName = findViewById(R.id.victim_name);
        spinner = findViewById(R.id.offencedesc);
        mPlace = findViewById(R.id.offenceplace);
        mLat = findViewById(R.id.latitude);
        mLan = findViewById(R.id.longitude);
        mWitness = findViewById(R.id.witness);

        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList = new ArrayList<>();
        spinnerDataList.add(0, "Choose Your Description");
        spinnerDataList.add("Money theft");
        spinnerDataList.add("Drunk and drive");
        spinnerDataList.add("Burglary");
        spinnerDataList.add("Vehicle Accident");

        adapter = new ArrayAdapter<String>(PoliceCreateFir.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                description = spinner.getSelectedItem().toString();
                Toast.makeText(PoliceCreateFir.this, "" + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    public void onFileFir(View view) {
        firNo = mFirNo.getText().toString();
        victimName = mVictimName.getText().toString();
        place = mPlace.getText().toString();
        lat = mLat.getText().toString();
        lan = mLan.getText().toString();
        witness = mWitness.getText().toString();

        if (firNo.equals("") || victimName.equals("") || description.equals("Choose Your Description")
                || place.equals("") || witness.equals("")) {
            if (firNo.equals("")) {
                mFirNo.setError("Fir No is required.");
            } else if (victimName.equals("")) {
                mVictimName.setError("Victim Name or Reporter Name is required.");
            } else if (description.equals("Choose Your Description")) {
                Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
            } else if (place.equals("")) {
                mPlace.setError("Place is required.");
            } else if (witness.equals("")) {
                mWitness.setError("Witness is required");
            }
        } else {
            final UploadFir u = new UploadFir(firNo, victimName, description, place, lat, lan, witness);
            reference.child(firNo).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(PoliceCreateFir.this, PoliceHomeScreen.class));
                    finish();
                    Toast.makeText(PoliceCreateFir.this, "Success", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PoliceCreateFir.this, PoliceHomeScreen.class));
        finish();
        //here exit app alert close............................................
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}