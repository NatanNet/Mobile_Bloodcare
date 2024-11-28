package com.example.bloodcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DonorAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> dataList; // Field anggota kelas

    // Konstruktor
    public DonorAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.status, parent, false);
        }

        TextView tvNama = convertView.findViewById(R.id.textNama);
        TextView tvStatus = convertView.findViewById(R.id.statusText);

        HashMap<String, String> item = dataList.get(position);

        tvNama.setText(item.get("nama"));
        String status = item.get("status");
        tvStatus.setText(status);

        if (status.equalsIgnoreCase("Lengkap")) {
            tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        return convertView;
    }


}
