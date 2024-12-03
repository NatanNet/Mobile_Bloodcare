package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Page_stokdarah extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_stokdarah);

        ImageButton buttonBack = findViewById(R.id.icback2);

        buttonBack.setOnClickListener(v -> finish());

        // Inisialisasi ImageView
        ImageView imgAPlus = findViewById(R.id.iconEditAplus);
        ImageView imgAMinus = findViewById(R.id.iconEditAmin);
        ImageView imgBPlus = findViewById(R.id.iconEditBplus);
        ImageView imgBMinus = findViewById(R.id.iconEditBmin);
        ImageView imgABPlus = findViewById(R.id.iconEditABplus);
        ImageView imgABMinus = findViewById(R.id.iconEditABmin);
        ImageView imgOPlus = findViewById(R.id.iconEditOplus);
        ImageView imgOMinus = findViewById(R.id.iconEditOmin);
        // Lanjutkan dengan ID lainnya...

        // Set onClickListener untuk masing-masing ImageView
        imgAPlus.setOnClickListener(view -> navigateToDetail("A+"));
        imgAMinus.setOnClickListener(view -> navigateToDetail("A-"));
        imgBPlus.setOnClickListener(view -> navigateToDetail("B+"));
        imgBMinus.setOnClickListener(view -> navigateToDetail("B-"));
        imgABPlus.setOnClickListener(view -> navigateToDetail("AB+"));
        imgABMinus.setOnClickListener(view -> navigateToDetail("AB-"));
        imgOPlus.setOnClickListener(view -> navigateToDetail("O+"));
        imgOMinus.setOnClickListener(view -> navigateToDetail("O-"));
        // Lanjutkan dengan golongan darah lainnya...
    }

    private void navigateToDetail(String golonganDarah) {
        Intent intent = new Intent(Page_stokdarah.this, Page_stokdarah2.class);
        intent.putExtra("golongan_darah", golonganDarah);
        startActivity(intent);
    }
}