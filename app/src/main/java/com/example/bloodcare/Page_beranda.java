package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Page_beranda extends Fragment {

    public Page_beranda() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout untuk fragment
        View view = inflater.inflate(R.layout.activity_dashboard1, container, false);

        // Inisialisasi LinearLayout dengan ID yang sesuai
        LinearLayout layoutKlik = view.findViewById(R.id.acaraklik);
        LinearLayout dataKlik = view.findViewById(R.id.datadonorklik); // Pastikan ID ini berbeda di XML
        LinearLayout stokKlik = view.findViewById(R.id.stokklik);
        LinearLayout laporanKlik = view.findViewById(R.id.laporanklik);
        TextView date = view.findViewById(R.id.dateTextView);

        // New ImageView initializations
        ImageView imageViewakun = view.findViewById(R.id.profil);

        // Mendapatkan tanggal saat ini menggunakan Calendar
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy"); // Format: Hari, Tanggal Bulan Tahun

        // Format tanggal dan set ke TextView
        String currentDate = sdf.format(calendar.getTime());
        date.setText(currentDate);

        imageViewakun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke fragment akun
                Fragment secondFragment = new Page_akun();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, secondFragment); // ganti `fragment_container` dengan ID dari layout container fragment Anda
                transaction.addToBackStack(null); // Menambahkan ke back stack agar bisa kembali
                transaction.commit();
            }
        });

        // Set onClickListener pada LinearLayout untuk berpindah ke Activity Page_acaradonor
        layoutKlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Page_acaradonor.class);
                startActivity(intent);
            }
        });

        // Set onClickListener pada LinearLayout untuk berpindah ke Activity Page_datadonor
        dataKlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Page_datapendonor2.class);
                startActivity(intent);
            }
        });

        stokKlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Page_stokdarah.class);
                startActivity(intent);
            }
        });


        laporanKlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Page_laporan.class);
                startActivity(intent);
            }
        });
//

        return view;
    }
}
