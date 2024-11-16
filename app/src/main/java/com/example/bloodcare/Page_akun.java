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

public class Page_akun extends Fragment {

    public Page_akun() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard3, container, false);

        Button buttonNavigate = view.findViewById(R.id.btneditpp);
        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke fragment kedua
                Fragment secondFragment = new Page_detail_akun();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, secondFragment); // ganti `fragment_container` dengan ID dari layout container fragment Anda
                transaction.addToBackStack(null); // Menambahkan ke back stack agar bisa kembali
                transaction.commit();
            }
        });

        return view;}
}
