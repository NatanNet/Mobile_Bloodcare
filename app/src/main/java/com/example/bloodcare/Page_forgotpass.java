package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Page_forgotpass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_pass);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ImageButton buttonBack3 = findViewById(R.id.backbutton3);
        Button confirm = findViewById(R.id.btn_confim);
        buttonBack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengembalikan ke aktivitas sebelumnya
                finish(); // Menutup aktivitas saat ini
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_forgotpass.this, Page_forgotpass_sucess.class);
                startActivity(intent);
            }
        });


    }
}