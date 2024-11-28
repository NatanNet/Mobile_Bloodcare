package com.example.bloodcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri; // Import Uri untuk membuka link
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
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog; // Import AlertDialog

public class Page_akun extends Fragment {

    private SharedPreferencess sharedPreferences; // Tambahkan SharedPreferencess untuk logout

    public Page_akun() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard3, container, false);

        // Inisialisasi SharedPreferencess untuk logout
        sharedPreferences = new SharedPreferencess(getActivity()); // Gunakan getActivity() untuk konteks di fragment

        // Inisialisasi tombol
        Button buttonNavigate = view.findViewById(R.id.btneditpp); // Tombol untuk pindah ke Page_detail_akun
        ImageView buttonBack = view.findViewById(R.id.keluar); // Tombol untuk logout
        ImageView imageViewGoToOTP = view.findViewById(R.id.gantipss); // ImageView untuk menuju ke Page_otpsend
        ImageView imageViewGoogle = view.findViewById(R.id.tentangkami1); // ImageView untuk membuka Google atau URL lain
        ImageView imageViewPusatBantuan = view.findViewById(R.id.bantuan1); // Tombol pusat bantuan

        // Listener untuk buttonNavigate
        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke fragment kedua (Page_detail_akun)
                Fragment secondFragment = new Page_detail_akun();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, secondFragment); // Ganti `frameLayout` dengan ID container fragment Anda
                transaction.addToBackStack(null); // Menambahkan ke back stack agar bisa kembali
                transaction.commit();
            }
        });

        // Listener untuk buttonBack (ImageView untuk logout dan kembali ke Page_login)
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menampilkan pop-up konfirmasi keluar
                new AlertDialog.Builder(getContext())
                        .setTitle("Keluar")
                        .setMessage("Apakah Anda yakin ingin keluar?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Jika memilih "Ya", lakukan logout dengan SharedPreferencess
                                sharedPreferences.logout();

                                // Setelah logout, buka Page_login menggunakan Intent
                                Intent intent = new Intent(getActivity(), Page_login.class);
                                startActivity(intent);

                                // Tutup Page_akun agar tidak bisa kembali ke fragment ini
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Tidak", null) // Jika memilih "Tidak", hanya menutup dialog
                        .show(); // Menampilkan dialog
            }
        });

        // Listener untuk imageViewGoToOTP (untuk berpindah ke Page_otpsend)
        imageViewGoToOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuka Page_otpsend
                Intent intent = new Intent(getActivity(), Page_otpsend.class);
                startActivity(intent); // Memulai activity untuk membuka Page_otpsend
            }
        });

        // Listener untuk ImageView (untuk membuka link ke Google)
        imageViewGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuka halaman Google
                Uri googleUri = Uri.parse("https://bloodcare.my.id/website_bloodcare/website/public_html/");  // Ganti URL sesuai kebutuhan
                Intent intent = new Intent(Intent.ACTION_VIEW, googleUri);
                startActivity(intent);  // Membuka URL di browser
            }
        });

        // Listener untuk pusat bantuan (untuk membuka WhatsApp)
        imageViewPusatBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ganti nomor dengan nomor WhatsApp tujuan
                String phoneNumber = "+6282142141480"; // Ganti dengan nomor WhatsApp yang diinginkan
                String message = "Halo, saya membutuhkan bantuan.";

                // Membuka WhatsApp dengan nomor dan pesan tertentu
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message)));
                startActivity(intent);
            }
        });

        return view; // Mengembalikan view dari fragment
    }
}
