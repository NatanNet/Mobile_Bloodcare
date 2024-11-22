package com.example.bloodcare;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Page_stokdarah2 extends AppCompatActivity {

    private EditText edtDarahTerkumpul, edtKebutuhanDarah;
    private Spinner spinnerJenisDarah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_stokdarah2);

        // Inisialisasi View
        edtDarahTerkumpul = findViewById(R.id.editDarahTerkumpul);
        edtKebutuhanDarah = findViewById(R.id.editKebutuhanDarah);
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

        // Tombol kembali
        findViewById(R.id.icback).setOnClickListener(v -> onBackPressed());

        // Tombol Simpan
        findViewById(R.id.btnSimpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        // Ambil input dari user
        String darahTerkumpul = edtDarahTerkumpul.getText().toString().trim();
        String kebutuhanDarah = edtKebutuhanDarah.getText().toString().trim();
        String jenisDarah = spinnerJenisDarah.getSelectedItem().toString();

        // Validasi input
        if (darahTerkumpul.isEmpty() || kebutuhanDarah.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proses penyimpanan data (contoh: API request atau database)
        // Misalnya, simpan ke server menggunakan Volley atau Retrofit
        Toast.makeText(this, "Data berhasil disimpan:\n" +
                "Jenis Darah: " + jenisDarah +
                "\nDarah Terkumpul: " + darahTerkumpul +
                "\nKebutuhan Darah: " + kebutuhanDarah, Toast.LENGTH_LONG).show();

        // Kembali ke halaman sebelumnya
        finish();
    }
}
