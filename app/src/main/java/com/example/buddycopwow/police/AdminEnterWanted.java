package com.example.buddycopwow.police;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.UploadWanted;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class AdminEnterWanted extends AppCompatActivity {
    private static final int GalaryPick = 1;
    LoadingDialog loadingDialog;
    DatabaseReference reference;
    StorageReference UserProfileImageReference;
    SearchableSpinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;
    EditText mName, mPlace;
    String name, crime, place, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_enter_wanted);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Enter Wanted Data");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadingDialog = new LoadingDialog(AdminEnterWanted.this);
        spinner = findViewById(R.id.offencedesc);

        reference = getInstance().getReference("wanted");
        UserProfileImageReference = FirebaseStorage.getInstance().getReference().child("Wanted Image");

        mName = findViewById(R.id.etWantedName);
        mPlace = findViewById(R.id.etPlaceName);

        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList = new ArrayList<>();
        spinnerDataList.add(0, "Select Crime Description");
        spinnerDataList.add("Money theft");
        spinnerDataList.add("Drunk and drive");
        spinnerDataList.add("Burglary");
        spinnerDataList.add("Vehicle Accident");

        adapter = new ArrayAdapter<String>(AdminEnterWanted.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                crime = spinner.getSelectedItem().toString();
                Toast.makeText(AdminEnterWanted.this, "" + crime, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    public void onEnterWantedPic(View view) {
        name = mName.getText().toString();
        place = mPlace.getText().toString();

        if (name.equals("") || place.equals("") || crime.equals("Select Crime Description")) {
            if (name.equals("")) {
                mName.setError("Name is required");
            } else if (place.equals("")) {
                mPlace.setError("Place is required");
            } else if (crime.equals("Select Crime Description")) {
                Toast.makeText(AdminEnterWanted.this, "Select Description", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent galaryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galaryIntent, GalaryPick);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalaryPick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Please wait wanted image is uploading..");

                Uri resultUri = result.getUri();
                final File file = new File(SiliCompressor.with(AdminEnterWanted.this)
                        .compress(FileUtils.getPath(AdminEnterWanted.this, resultUri), new File(AdminEnterWanted.this.getCacheDir(), "temp")));
                Uri uri = Uri.fromFile(file);

                StorageReference filePath = UserProfileImageReference.child(name + ".jpg");
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        file.delete();
                        Toast.makeText(AdminEnterWanted.this, "Wanted Image Uploaded successfully..", Toast.LENGTH_SHORT).show();

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri url1 = uri.getResult();

                        UploadWanted u = new UploadWanted(url1.toString(), name, crime, place);

                        reference.child(name)
                                .setValue(u)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AdminEnterWanted.this, "Data saved in database", Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismissDialog();
                                            startActivity(new Intent(AdminEnterWanted.this, AdminHomeScreen.class));
                                            finish();
                                        } else {
                                            String message = task.getException().toString();
                                            Toast.makeText(AdminEnterWanted.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismissDialog();
                                        }
                                    }
                                });

                    }
                });
            }


        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminEnterWanted.this, AdminHomeScreen.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}