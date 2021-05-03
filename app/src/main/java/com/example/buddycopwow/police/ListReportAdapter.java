package com.example.buddycopwow.police;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buddycopwow.R;
import com.example.buddycopwow.Uploads.UploadAttendance;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ListReportAdapter extends ArrayAdapter {
    private Activity mContext;
    List<UploadAttendance> clientList;
    DatabaseReference referenceLeads, referenceCred;
    public ListReportAdapter(Activity mContext, List<UploadAttendance> clientList){

        super(mContext, R.layout.list_report,clientList);
        this.mContext = mContext;
        this.clientList = clientList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("ViewHolder") View listItemView = inflater.inflate(R.layout.list_report, null, false);

        TextView tvName = listItemView.findViewById(R.id.rowName);
        //TextView tvClientType = listItemView.findViewById(R.id.rowClientType);
        TextView tvDate = listItemView.findViewById(R.id.rowDate);
        TextView tvTakenBy = listItemView.findViewById(R.id.rowTakenBy);
        TextView tvTime = listItemView.findViewById(R.id.rowTime);
        TextView tvSectorHeadName = listItemView.findViewById(R.id.rowSectorHeadName);

        UploadAttendance employee = clientList.get(position);

        tvName.setText(employee.getTakenOf());
        tvDate.setText(employee.getStatus());
        tvTakenBy.setText(employee.getTakenBy());
        tvTime.setText(employee.getTime());
        tvSectorHeadName.setText(employee.getSectorHeadName());

        String status = employee.getStatus();
        if(status.equals("present")){
            String color = "#84E488";
            listItemView.setBackgroundColor(Color.parseColor(color));
        }
        else {
            String color = "#C86E6E";
            listItemView.setBackgroundColor(Color.parseColor(color));
        }
        return listItemView;
    }

}
