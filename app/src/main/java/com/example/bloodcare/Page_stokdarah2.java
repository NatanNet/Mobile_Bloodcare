package com.example.bloodcare;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Page_stokdarah2 extends AppCompatActivity {

    private EditText edtDarahTerkumpul, edtKebutuhanDarah;
    private Spinner spinnerJenisDarah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_stokdarah2);

        // Inisialisasi EditText
        edtDarahTerkumpul = findViewById(R.id.editDarahTerkumpul);
        edtKebutuhanDarah = findViewById(R.id.editKebutuhanDarah);

        // Inisialisasi Spinner
        spinnerJenisDarah = findViewById(R.id.spinnerJenisDarah);

        // Array untuk jenis darah
        String[] jenisDarahOptions = {"WB (Whole Blood)", "PRC (Packed Red Cells)",
                "TC (Thrombocyte Concentrate)", "FFP (Fresh Frozen Plasma)"};

        // Adapter untuk Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                jenisDarahOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisDarah.setAdapter(adapter);

        // Menganbil tombol dari xml dan memberikan fungsi tombol
        findViewById(R.id.icback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aksi untuk kembali ke Activity sebelumnya
                onBackPressed();
            }
        });
    }
}