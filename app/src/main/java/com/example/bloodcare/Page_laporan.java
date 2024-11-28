package com.example.bloodcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Page_laporan extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_laporan);

        ImageView iconLaporan = findViewById(R.id.iconLaporan);
        ImageButton buttonBack = findViewById(R.id.icback3);


        // Set listener untuk tombol back
        buttonBack.setOnClickListener(v -> finish());

        iconLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(Page_laporan.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.item_senin) {
                            Toast.makeText(Page_laporan.this, "Senin dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_selasa) {
                            Toast.makeText(Page_laporan.this, "Selasa dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_rabu) {
                            Toast.makeText(Page_laporan.this, "Rabu dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_kamis) {
                            Toast.makeText(Page_laporan.this, "Kamis dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_jumat) {
                            Toast.makeText(Page_laporan.this, "Jumat dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_minggu_ini) {
                            Toast.makeText(Page_laporan.this, "Minggu Ini dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_minggu_lalu) {
                            Toast.makeText(Page_laporan.this, "Minggu Lalu dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_bulan_ini) {
                            Toast.makeText(Page_laporan.this, "Bulan Ini dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_bulan_lalu) {
                            Toast.makeText(Page_laporan.this, "Bulan Lalu dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_semua) {
                            Toast.makeText(Page_laporan.this, "Semua dipilih", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        // Panggil metode fetchLaporanData() untuk mengambil data laporan
        fetchLaporanData();
    }

    private void fetchLaporanData() {
        String url = Config.BASE_URL + "get_laporan.php"; // Ganti dengan URL API Anda
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if ("success".equals(response.getString("status"))) {
                            JSONArray dataArray = response.getJSONArray("data");
                            StringBuilder laporanBuilder = new StringBuilder();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                laporanBuilder.append("Nama: ").append(obj.getString("nama_pendonor"))
                                        .append("\nLokasi: ").append(obj.getString("lokasi_donor"))
                                        .append("\nHP/Email: ").append(obj.getString("hp_email"))
                                        .append("\nBerat Badan: ").append(obj.getDouble("berat_badan"))
                                        .append("\nGolongan Darah: ").append(obj.getString("goldar"))
                                        .append("\nTekanan Darah: ").append(obj.getString("tekanan_darah"))
                                        .append("\nRhesus: ").append(obj.getString("rhesus"))
                                        .append("\n\n");

                            }

                            Toast.makeText(this, laporanBuilder.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show());

        queue.add(jsonObjectRequest);
    }
}



