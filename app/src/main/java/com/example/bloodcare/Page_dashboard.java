package com.example.bloodcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Page_dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Ambil intent dari user_id dan target_fragment
        int userId = getIntent().getIntExtra("user_id", -1); // Default -1 jika tidak ada
        int targetFragment = getIntent().getIntExtra("target_fragment", -1); // Default -1 jika tidak ada

        // Tambahkan log untuk memeriksa nilai target_fragment
        android.util.Log.d("Page_dashboard", "Received target_fragment: " + targetFragment);

        // Tentukan fragment awal berdasarkan intent
        if (savedInstanceState == null) {
            if (targetFragment == 2) {
                // Jika target_fragment valid, buka Page_acara
                navigateToTargetFragment(targetFragment);
            } else if (userId == 1) {
                // Jika userId = 1, buka Page_detail_akun
                openDetailAkunFragment(userId);
            } else {
                // Jika target_fragment tidak ada atau tidak lengkap, buka Page_beranda
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
                            .addToBackStack(null) // Add to back stack for proper back navigation
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    // Fungsi untuk membuka fragment Page_detail_akun
    private void openDetailAkunFragment(int userId) {
        Fragment detailAkunFragment = new Page_detail_akun();

        // Ambil data dari intent
        String usernameOrEmail = getIntent().getStringExtra("username_or_email");

        // Periksa jika usernameOrEmail tidak null atau kosong
        if (usernameOrEmail != null && !usernameOrEmail.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putInt("user_id", userId);
            bundle.putString("username_or_email", usernameOrEmail);  // Passing the single value (username or email)

            detailAkunFragment.setArguments(bundle);

            // Gantikan fragment di frameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, detailAkunFragment)
                    .addToBackStack(null) // Add to back stack for proper back navigation
                    .commit();

            // Perbarui item yang dipilih di BottomNavigationView (jika perlu)
            bottomNavigationView.setSelectedItemId(R.id.navigation_akun);
        } else {
            // Jika tidak ada data, tampilkan pesan error atau lakukan penanganan lain
            Toast.makeText(this, "Data username atau email tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }


    // Fungsi untuk membuka fragment Page_akun
    private void open_akun(int userId) {
        Fragment detailAkunFragment = new Page_akun();

        // Ambil data dari Intent
        String usernameOrEmail = getIntent().getStringExtra("username_or_email");

        // Periksa jika usernameOrEmail tidak null atau kosong
        if (usernameOrEmail != null && !usernameOrEmail.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString("username_or_email", usernameOrEmail);
            detailAkunFragment.setArguments(bundle);

            // Gantikan fragment di frameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, detailAkunFragment)
                    .addToBackStack(null) // Add to back stack for proper back navigation
                    .commit();
        } else {
            // Jika tidak ada data, tampilkan pesan error atau lakukan penanganan lain
            Toast.makeText(this, "Data username_or_email tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi untuk navigasi ke target_fragment berdasarkan intent
    private void navigateToTargetFragment(int targetFragment) {
        Fragment selectedFragment = null;

        // Tentukan fragment berdasarkan targetFragment
        if (targetFragment == 2) { // 2 mengacu pada Page_acara
            selectedFragment = new Page_acara();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, selectedFragment)
                    .addToBackStack(null) // Add to back stack for proper back navigation
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
