package com.example.bloodcare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Page_laporan3 extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> dataList;

    // Konstruktor
    public Page_laporan3(Context context, ArrayList<HashMap<String, String>> dataList) {
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_page_laporan3, parent, false);
            holder = new ViewHolder();
            holder.tvNama = convertView.findViewById(R.id.textNama1);
            holder.tvLihat = convertView.findViewById(R.id.textlihat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> item = dataList.get(position);

        // Mengatur nama dari data
        holder.tvNama.setText(item.get("nama"));

        // Set OnClickListener untuk tvlihat
        holder.tvLihat.setOnClickListener(v -> {
            Intent intent = new Intent(context, Page_laporan2.class);
            intent.putExtra("id_laporan", item.get("id_laporan"));
            intent.putExtra("nama_pendonor", item.get("nama"));
            intent.putExtra("nohp", item.get("nohp"));
            intent.putExtra("lokasi_donor", item.get("lokasi_donor"));
            intent.putExtra("goldar", item.get("goldar"));
            intent.putExtra("tekanan_darah", item.get("tekanan"));
            intent.putExtra("berat_badan", item.get("beratbadan"));
            intent.putExtra("rhesus", item.get("rhesus"));

            // Jalankan intent
            context.startActivity(intent);
        });

        return convertView;
    }

    // ViewHolder untuk optimasi
    static class ViewHolder {
        TextView tvNama;
        TextView tvLihat;
    }
}
