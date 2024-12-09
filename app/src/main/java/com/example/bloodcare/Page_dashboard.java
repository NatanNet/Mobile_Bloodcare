package com.example.bloodcare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Page_dashboard extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Periksa dan minta izin
        checkStoragePermission();

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Retrieve data from Intent
        int userId = getIntent().getIntExtra("user_id", -1);
        String usernameOrEmail = getIntent().getStringExtra("username_or_email");
        String redirectTo = getIntent().getStringExtra("redirect_to");

        // Handle redirection logic
        if ("page_editprofil".equals(redirectTo)) {
            Toast.makeText(this, "Redirecting to Edit Profile Page", Toast.LENGTH_SHORT).show();
            openEditProfileFragment(userId, usernameOrEmail);
        } else if ("page_akun".equals(redirectTo)) {
            Toast.makeText(this, "Redirecting to Account Page", Toast.LENGTH_SHORT).show();
            openAccountPageFragment(usernameOrEmail);
        } else if (savedInstanceState == null) {
            loadDefaultFragment(savedInstanceState, usernameOrEmail);
        } else {
            Toast.makeText(this, "Redirecting to Default Dashboard", Toast.LENGTH_SHORT).show();
            loadFragment(new Page_beranda(), usernameOrEmail);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }

        // Set up BottomNavigationView item selection listener
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+: Cek izin READ_MEDIA_IMAGES dan READ_MEDIA_VIDEO
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission();
            } else {
                onStoragePermissionGranted();
            }
        } else {
            // Android di bawah 13: Gunakan izin READ_EXTERNAL_STORAGE dan WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission();
            } else {
                onStoragePermissionGranted();
            }
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+: Minta izin media untuk gambar dan video saja
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO
                    },
                    PERMISSION_REQUEST_CODE
            );
        } else {
            // Android di bawah 13: Minta izin penyimpanan eksternal
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE
            );
        }
    }


    private void onStoragePermissionGranted() {
        // Logika setelah izin diberikan
        Toast.makeText(this, "Izin diberikan. Anda dapat mengakses file.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean allPermissionsGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
                if (allPermissionsGranted) {
                    onStoragePermissionGranted();
                } else {
                    Toast.makeText(this, "Izin akses penyimpanan ditolak", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Load the default fragment based on the intent's targetFragment or savedInstanceState
    private void loadDefaultFragment(Bundle savedInstanceState, String usernameOrEmail) {
        if (savedInstanceState == null) {
            int targetFragment = getIntent().getIntExtra("target_fragment", -1);
            if (targetFragment == 2) {
                loadFragment(new Page_acara(), usernameOrEmail); // Send usernameOrEmail to Page_acara
                bottomNavigationView.setSelectedItemId(R.id.navigation_acara);
            } else {
                loadFragment(new Page_beranda(), usernameOrEmail); // Send usernameOrEmail to Page_beranda
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }
        }
    }

    // Handle BottomNavigationView item selection
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        String usernameOrEmail = getIntent().getStringExtra("username_or_email");

        if (item.getItemId() == R.id.navigation_home) {
            selectedFragment = new Page_beranda();
            loadFragment(selectedFragment, usernameOrEmail); // Send usernameOrEmail to Page_beranda
        } else if (item.getItemId() == R.id.navigation_acara) {
            selectedFragment = new Page_acara();
            loadFragment(selectedFragment, usernameOrEmail); // Send usernameOrEmail to Page_acara
        } else if (item.getItemId() == R.id.navigation_akun) {
            openAccountPageFragment(usernameOrEmail);
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment, usernameOrEmail);
        }

        return true;
    }

    // Replace the current fragment in the FrameLayout
    private void loadFragment(Fragment fragment, String usernameOrEmail) {
        Bundle bundle = new Bundle();
        bundle.putString("username_or_email", usernameOrEmail);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    // Handle back button behavior
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        if (currentFragment instanceof Page_acara || currentFragment instanceof Page_akun) {
            loadFragment(new Page_beranda(), getIntent().getStringExtra("username_or_email"));
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else if (currentFragment instanceof Page_beranda) {
            super.onBackPressed();
        } else {
            loadFragment(new Page_beranda(), getIntent().getStringExtra("username_or_email"));
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    // Open Edit Profile Fragment
    private void openEditProfileFragment(int userId, String usernameOrEmail) {
        Log.d("Page_dashboard", "Opening Edit Profile Fragment: userId=" + userId + ", usernameOrEmail=" + usernameOrEmail);

        Page_edit_akun fragment = new Page_edit_akun();
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);
        bundle.putString("username_or_email", usernameOrEmail);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    private void openAccountPageFragment(String usernameOrEmail) {
        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
            Log.e("openAccountPageFragment", "usernameOrEmail is null or empty!");
            Toast.makeText(this, "Invalid username or email", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("openAccountPageFragment", "Sending usernameOrEmail to Page_akun: " + usernameOrEmail);

        // Buat instance fragment
        Page_akun pageAkunFragment = new Page_akun();

        // Kirimkan data menggunakan Bundle
        Bundle bundle = new Bundle();
        bundle.putString("usernameOrEmail", usernameOrEmail); // Kirim data ke fragment
        pageAkunFragment.setArguments(bundle);

        // Ganti fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, pageAkunFragment)
                .commit();
    }

}
