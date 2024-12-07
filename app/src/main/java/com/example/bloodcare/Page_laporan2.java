package com.example.bloodcare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Page_laporan2 extends AppCompatActivity {
    private Button buttonlihat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_laporan2);

        ImageButton buttonBack = findViewById(R.id.back1);

        buttonBack.setOnClickListener(v -> finish());

        // Ambil data dari Intent
        String nama = getIntent().getStringExtra("nama_pendonor");
        String lokasiDonor = getIntent().getStringExtra("lokasi_donor");
        String beratbadan = getIntent().getStringExtra("berat_badan");
        String tekanan = getIntent().getStringExtra("tekanan_darah");
        String goldar = getIntent().getStringExtra("goldar");
        String nohp = getIntent().getStringExtra("nohp");
        String rhesus = getIntent().getStringExtra("rhesus");

        EditText editNama = findViewById(R.id.et_nama_pendonor);
        EditText editlokasi = findViewById(R.id.et_lokasi_donor);
        EditText editkontak = findViewById(R.id.et_kontak);
        EditText editbb = findViewById(R.id.et_bb);
        EditText editgol = findViewById(R.id.et_goldar);
        EditText edittekanan = findViewById(R.id.et_tekanan);
        EditText editrhesus = findViewById(R.id.et_rhesus);

        editNama.setText(nama);
        editlokasi.setText(lokasiDonor);
        editkontak.setText(nohp);
        editbb.setText(beratbadan);
        edittekanan.setText(tekanan);
        editgol.setText(goldar);
        editrhesus.setText(rhesus);

    }
}