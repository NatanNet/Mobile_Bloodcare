package com.example.bloodcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Page_dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Ambil user_id dari Intent
        int userId = getIntent().getIntExtra("user_id", -1);

        // Handle fragment default yang akan dibuka saat pertama kali
        if (savedInstanceState == null) {
            if (userId != -1) {
                // Jika ada user_id, langsung buka fragment Page_edit_akun
                Fragment pageEditAkunFragment = new Page_edit_akun();
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", userId);
                pageEditAkunFragment.setArguments(bundle);

                // Ganti fragment dengan Page_edit_akun
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, pageEditAkunFragment)
                        .commit();
            } else {
                // Jika tidak ada user_id, tampilkan fragment beranda
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new Page_beranda())
                        .commit();
            }
        }

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Bottom Navigation Item Select Listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Menggunakan if-else untuk memeriksa item yang dipilih
                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new Page_beranda();
                } else if (item.getItemId() == R.id.navigation_acara) {
                    selectedFragment = new Page_acara();
                } else if (item.getItemId() == R.id.navigation_akun) {
                    selectedFragment = new Page_akun();
                }

                // Ganti fragment jika fragment yang dipilih valid
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, selectedFragment)
                            .commit();
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Handle back press untuk navigasi fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        if (currentFragment instanceof Page_acara || currentFragment instanceof Page_akun) {
            // Jika berada di fragment selain beranda, kembali ke beranda
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new Page_beranda())
                    .commit();

            // Update BottomNavigationView untuk memilih item navigation_home
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            // Jika sudah di fragment beranda, keluar aplikasi
            super.onBackPressed();
        }
    }
}
