package com.example.buddycopwow.police;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buddycopwow.R;
import com.google.firebase.auth.FirebaseAuth;


public class AdminHomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Login");
        setSupportActionBar(toolbar);
    }


    public void onCreateSector(View view) {
        startActivity(new Intent(AdminHomeScreen.this, AdminCreateSector.class));
    }

    public void onAllocateOfficers(View view) {
        startActivity(new Intent(AdminHomeScreen.this, AdminOfficerAllocate.class));
    }

    public void onAllocateHeadOfficer(View view) {
        startActivity(new Intent(AdminHomeScreen.this, AdminHeadAllocate.class));
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

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeScreen.this);
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

    public void onEnterWantedData(View view) {
        startActivity(new Intent(AdminHomeScreen.this, AdminEnterWanted.class));
        finish();
    }
}