package com.example.buddycopwow.police;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buddycopwow.R;

import java.util.Calendar;

public class AdminReport extends AppCompatActivity {
    TextView mStartDate;
    String startDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Reports");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mStartDate = findViewById(R.id.tv_startDate);

        final DialogFragment dialogFragment = new AdminReport.DatePickerDialogTheme4();

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminReport.DatePickerDialogTheme4.id = "start";
                dialogFragment.show(getSupportFragmentManager(), "theme 4");
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminReport.this, AdminHomeScreen.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onShowReport(View view) {
        startDate = mStartDate.getText().toString();

        if (startDate.equals("") ) {
            Toast.makeText(AdminReport.this, "Select Date First", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(AdminReport.this, AdminReport2.class);
            intent.putExtra("start", startDate);
            startActivity(intent);
            finish();
        }
    }

    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public static String id = null;
        String date;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int month2 = month + 1;
            String formattedMonth = "" + month2;
            String formattedDayOfMonth = "" + day;

            if (month2 < 10) {
                formattedMonth = "0" + month2;
            }
            if (day < 10) {
                formattedDayOfMonth = "0" + day;
            }

            TextView textView = getActivity().findViewById(R.id.tv_startDate);
            textView.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + year);
            date = textView.getText().toString().trim();

        }
    }

}