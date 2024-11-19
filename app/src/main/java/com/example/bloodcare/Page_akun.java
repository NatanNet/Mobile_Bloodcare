package com.example.bloodcare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class Page_akun extends Fragment {

    public Page_akun() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard3, container, false);

        // Inisialisasi tombol
        Button buttonNavigate = view.findViewById(R.id.btneditpp);
        ImageButton buttonBack = view.findViewById(R.id.imageButton5);

        // Listener untuk buttonNavigate
        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke fragment kedua
                Fragment secondFragment = new Page_detail_akun();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, secondFragment); // Ganti `frameLayout` dengan ID container fragment Anda
                transaction.addToBackStack(null); // Menambahkan ke back stack agar bisa kembali
                transaction.commit();
            }
        });

        // Listener untuk buttonBack
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke fragment beranda
                Fragment berandaFragment = new Page_beranda(); // Ganti dengan fragment beranda Anda
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, berandaFragment); // Ganti `frameLayout` dengan ID container fragment Anda
                transaction.commit(); // Pastikan fragment langsung diganti tanpa back stack
            }
        });

        return view; // Mengembalikan view dari fragment
    }
}