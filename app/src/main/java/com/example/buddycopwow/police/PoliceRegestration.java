package com.example.buddycopwow.police;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.PoliceRegestrationUpload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

public class PoliceRegestration extends AppCompatActivity {
    SearchableSpinner spinner;
    DatabaseReference reference, referenceStudent;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;
    EditText mFirstName, mLastName, mPassword, mConfirmPassword, mMobileNo, mEmail;
    String firstName, lastName, mobileNo, designation = "", password, confirmPassword,
            email;
    Button mBtnSignUp;
    LoadingDialog loadingDialog;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_regestration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Police Registration");
        setSupportActionBar(toolbar);

        loadingDialog = new LoadingDialog(PoliceRegestration.this);

        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmpassword);
        spinner = findViewById(R.id.spinner_designation_name);
        mBtnSignUp = findViewById(R.id.btnSignUp);
        mMobileNo = findViewById(R.id.mobile_no);
        mEmail = findViewById(R.id.email);

        reference = FirebaseDatabase.getInstance().getReference("credentials").child("police");
        fAuth = FirebaseAuth.getInstance();

        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList = new ArrayList<>();
        spinnerDataList.add(0, "Choose Your Designation");
        spinnerDataList.add("DSP");
        spinnerDataList.add("PI");
        spinnerDataList.add("Constable");

        adapter = new ArrayAdapter<String>(PoliceRegestration.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = spinner.getSelectedItem().toString();
                Toast.makeText(PoliceRegestration.this, "" + designation, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Creating Account..");
                firstName = mFirstName.getText().toString();
                lastName = mLastName.getText().toString();
                mobileNo = mMobileNo.getText().toString();
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                confirmPassword = mConfirmPassword.getText().toString();

                if (firstName.equals("") || lastName.equals("") || mobileNo.equals("")
                        || password.equals("") || confirmPassword.equals("") || designation.equals("Choose Your Designation")
                        || password.length() < 6 || email.equals("")) {
                    if (firstName.equals("")) {
                        mFirstName.setError("First name is required.");
                        loadingDialog.dismissDialog();
                    } else if (lastName.equals("")) {
                        mLastName.setError("Last name is required.");
                        loadingDialog.dismissDialog();
                    } else if (designation.equals("Choose Your College")) {
                        Toast.makeText(PoliceRegestration.this, "Please select the college..", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    } else if (password.equals("")) {
                        mPassword.setError("Password is required.");
                        loadingDialog.dismissDialog();
                    } else if (confirmPassword.equals("")) {
                        mConfirmPassword.setError("Confirmation of password is required.");
                        loadingDialog.dismissDialog();
                    } else if (password.length() < 6) {
                        mPassword.setError("Password minimum length should be 6.");
                        loadingDialog.dismissDialog();
                    } else if (email.equals("")) {
                        mEmail.setError("Enter your email");
                        loadingDialog.dismissDialog();
                    }
                } else {
                    if (confirmPassword.equals(password)) {
                        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    final PoliceRegestrationUpload u = new PoliceRegestrationUpload(firstName, lastName,mobileNo, designation, email,
                                            confirmPassword, fAuth.getCurrentUser().getUid());

                                    reference.child(fAuth.getCurrentUser().getUid()).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(PoliceRegestration.this, PoliceHomeScreen.class));
                                            finish();
                                            loadingDialog.dismissDialog();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PoliceRegestration.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(PoliceRegestration.this, "Error..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        loadingDialog.dismissDialog();
                        mConfirmPassword.setError("Password doesn't match.");
                    }
                }

                ////////////////////////////////////////////////////////////////////////////////////////////


            }
        });
    }

    public void sendToPoliceLogin(View view) {
        startActivity(new Intent(PoliceRegestration.this, PoliceLogin.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PoliceRegestration.this, PoliceLogin.class));
        finish();
        //here exit app alert close............................................
    }
}