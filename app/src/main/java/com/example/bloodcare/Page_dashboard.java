package com.example.bloodcare;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Page_dashboard extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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
        } else if (savedInstanceState == null) {
            loadDefaultFragment(savedInstanceState);
        } else {
            // Fallback to dashboard or default fragment if no other conditions are met
            Toast.makeText(this, "Redirecting to Default Dashboard", Toast.LENGTH_SHORT).show();
            loadFragment(new Page_beranda());
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }


        // Set up BottomNavigationView item selection listener
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    // Load the default fragment based on the intent's targetFragment or savedInstanceState
    private void loadDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            int targetFragment = getIntent().getIntExtra("target_fragment", -1);
            if (targetFragment == 2) {
                loadFragment(new Page_acara());
                bottomNavigationView.setSelectedItemId(R.id.navigation_acara);
            } else {
                loadFragment(new Page_beranda());
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }
        }
    }

    // Handle BottomNavigationView item selection
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.navigation_home) {
            selectedFragment = new Page_beranda();
        } else if (item.getItemId() == R.id.navigation_acara) {
            selectedFragment = new Page_acara();
        } else if (item.getItemId() == R.id.navigation_akun) {
            selectedFragment = new Page_akun();
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        return true;
    }

    // Replace the current fragment in the FrameLayout
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    // Handle back button behavior
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        if (currentFragment instanceof Page_acara || currentFragment instanceof Page_akun) {
            loadFragment(new Page_beranda());
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else if (currentFragment instanceof Page_beranda) {
            super.onBackPressed();
        } else {
            loadFragment(new Page_beranda());
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
}
