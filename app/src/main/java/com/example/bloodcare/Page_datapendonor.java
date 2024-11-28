package com.example.bloodcare;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Page_datapendonor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_datapendonor);

        // Ambil data dari Intent
        String nama = getIntent().getStringExtra("nama");
        String nohp = getIntent().getStringExtra("nohp");
        String lokasiDonor = getIntent().getStringExtra("lokasi_donor");
        String beratbadan = getIntent().getStringExtra("bb");
        String goldar = getIntent().getStringExtra("goldar");
        String alamat = getIntent().getStringExtra("alamat");

        // Set data ke TextView
        ((EditText) findViewById(R.id.nama1)).setText(nama);
        ((EditText) findViewById(R.id.nomerhp)).setText(nohp);
        ((EditText) findViewById(R.id.berat)).setText(beratbadan);
        ((EditText) findViewById(R.id.lokasi1)).setText(lokasiDonor);
        ((EditText) findViewById(R.id.goldar)).setText(goldar);
        ((EditText) findViewById(R.id.alamat1)).setText(alamat);

    }
}
