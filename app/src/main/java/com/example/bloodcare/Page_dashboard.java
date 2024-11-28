package com.example.bloodcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class  Page_dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Ambil data dari Intent
        int userId = getIntent().getIntExtra("user_id", -1);
        int targetFragment = getIntent().getIntExtra("target_fragment", -1);
        String usernameOrEmail = getIntent().getStringExtra("username_or_email"); // Ambil username_or_email

        // Tentukan fragment awal berdasarkan intent
        if (savedInstanceState == null) {
            if (targetFragment == 2) {
                navigateToTargetFragment(targetFragment);  // Navigasi ke Page_acara
            } else if (userId == 1) {
                openDetailAkunFragment(userId, usernameOrEmail);  // Buka Page_detail_akun untuk userId == 1
            } else {
                loadFragment(new Page_beranda());  // Buka Page_beranda jika kondisi lain
            }
        }

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Bottom Navigation Item Select Listener
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    // Handle item yang dipilih di BottomNavigationView
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        // Tentukan fragment berdasarkan item yang dipilih
        if (item.getItemId() == R.id.navigation_home) {
            selectedFragment = new Page_beranda();
        } else if (item.getItemId() == R.id.navigation_acara) {
            selectedFragment = new Page_acara();
        } else if (item.getItemId() == R.id.navigation_akun) {
            selectedFragment = new Page_akun();
        }

        // Ganti fragment jika fragment yang dipilih valid
        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        return true;
    }

    // Fungsi untuk mengganti fragment di frameLayout
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        // Dapatkan fragment saat ini
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        if (currentFragment instanceof Page_acara || currentFragment instanceof Page_akun) {
            // Jika berada di fragment selain beranda, kembali ke Page_beranda
            loadFragment(new Page_beranda());

            // Update BottomNavigationView untuk memilih item navigation_acara
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else if (currentFragment instanceof Page_beranda) {
            // Jika berada di fragment beranda, keluar aplikasi
            super.onBackPressed();
        } else {
            // Jika fragment lainnya (misalnya DetailAkun), kembali ke Page_acara
            loadFragment(new Page_beranda());
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    // Fungsi untuk membuka fragment Page_detail_akun
    private void openDetailAkunFragment(int userId, String usernameOrEmail) {
        if (usernameOrEmail != null && !usernameOrEmail.isEmpty()) {
            Fragment detailAkunFragment = new Page_detail_akun();

            Bundle bundle = new Bundle();
            bundle.putInt("user_id", userId);
            bundle.putString("username_or_email", usernameOrEmail);
            detailAkunFragment.setArguments(bundle);

            // Gantikan fragment di frameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, detailAkunFragment)
                    .addToBackStack(null) // Menambahkan ke back stack agar back button berfungsi dengan benar
                    .commit();

            // Perbarui item yang dipilih di BottomNavigationView
            bottomNavigationView.setSelectedItemId(R.id.navigation_akun);
        } else {
            // Tampilkan pesan error jika data tidak ditemukan
            Toast.makeText(this, "Data username atau email tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi untuk membuka fragment Page_akun
    private void open_akun(int userId) {
        Fragment akunFragment = new Page_akun();

        // Ambil data dari Intent
        String usernameOrEmail = getIntent().getStringExtra("username_or_email");

        // Periksa apakah data usernameOrEmail ada
        if (usernameOrEmail != null && !usernameOrEmail.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString("username_or_email", usernameOrEmail);
            akunFragment.setArguments(bundle);

            // Gantikan fragment di frameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, akunFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            // Tampilkan pesan error jika data tidak ditemukan
            Toast.makeText(this, "Data username_or_email tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi untuk navigasi ke target_fragment berdasarkan intent
    private void navigateToTargetFragment(int targetFragment) {
        if (targetFragment == 2) {
            loadFragment(new Page_acara());
            bottomNavigationView.setSelectedItemId(R.id.navigation_acara);  // Perbarui item yang dipilih
        }
    }
}
