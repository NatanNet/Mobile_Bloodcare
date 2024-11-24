package com.example.bloodcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Page_dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Ambil intent dari user_id dan target_fragment
        int userId = getIntent().getIntExtra("user_id", -1); // Jika tidak ada, default -1
        int targetFragment = getIntent().getIntExtra("target_fragment", -1); // Jika tidak ada, default -1

        // Handle fragment default yang akan dibuka saat pertama kali
        if (savedInstanceState == null) {
            if (userId != 1) {
                // Jika user_id ada, buka fragment detail akun
                Fragment detailAkunFragment = new Page_detail_akun();
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", userId);
                detailAkunFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, detailAkunFragment)
                        .commit();
            } else if (targetFragment != 2) {
                // Jika target_fragment ada, buka fragment acara
                navigateToTargetFragment(targetFragment);
            } else {
                // Jika tidak ada intent, buka fragment beranda
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new Page_beranda())
                        .commit();
            }
        }

        // Atur listener untuk navigasi BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new Page_beranda();
                } else if (item.getItemId() == R.id.navigation_acara) {
                    selectedFragment = new Page_acara();
                } else if (item.getItemId() == R.id.navigation_akun) {
                    selectedFragment = new Page_akun();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    // Fungsi untuk navigasi ke target_fragment
    private void navigateToTargetFragment(int targetFragment) {
        Fragment selectedFragment = null;

        if (targetFragment == 2) { // 2 mengacu pada Page_acara
            selectedFragment = new Page_acara();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, selectedFragment)
                    .commit();

            // Perbarui item yang dipilih di BottomNavigationView
            if (targetFragment == 2) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_acara);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        if (currentFragment instanceof Page_acara || currentFragment instanceof Page_akun) {
            // Kembali ke beranda jika bukan di beranda
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new Page_beranda())
                    .commit();

            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            // Keluar jika sudah di beranda
            super.onBackPressed();
        }
    }
}
