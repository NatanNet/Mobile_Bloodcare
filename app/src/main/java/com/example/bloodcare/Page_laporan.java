package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Page_laporan extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
    private ListView listView1;
    private Page_laporan3 adapter;
    private final String LAPORAN_URL = Config.BASE_URL + "get_laporan.php";
    private String selectedMonth = "all"; // Default untuk semua bulan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_laporan);

        listView1 = findViewById(R.id.listView1);
        ImageView iconLaporan = findViewById(R.id.iconLaporan);
        ImageButton buttonBack = findViewById(R.id.icback3);

        // Inisialisasi Adapter
        adapter = new Page_laporan3(this, dataList);
        listView1.setAdapter(adapter);

        // Tombol kembali
        buttonBack.setOnClickListener(v -> finish());

        // Konfigurasi Popup Menu
        iconLaporan.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(Page_laporan.this, view);
            popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> handleMenuItemClick(item));
            popup.show();
        });

        // Ambil data awal
        fetchLaporanData();

        // Listener untuk item ListView
        listView1.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> selectedLaporan = dataList.get(position);
            Intent intent = new Intent(Page_laporan.this, Page_laporan2.class);
            intent.putExtra("id_laporan", selectedLaporan.get("id_laporan"));
            intent.putExtra("nama_pendonor", selectedLaporan.get("nama"));
            intent.putExtra("lokasi_donor", selectedLaporan.get("lokasi_donor"));
            intent.putExtra("berat_badan", selectedLaporan.get("beratbadan"));
            intent.putExtra("tekanan_darah", selectedLaporan.get("tekanan"));
            intent.putExtra("goldar", selectedLaporan.get("goldar"));
            intent.putExtra("nohp", selectedLaporan.get("nohp"));
            intent.putExtra("rhesus", selectedLaporan.get("rhesus"));
            startActivity(intent);
        });
    }

    private boolean handleMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.item_januari) {
            selectedMonth = "01";
        } else if (itemId == R.id.item_februari) {
            selectedMonth = "02";
        } else if (itemId == R.id.item_maret) {
            selectedMonth = "03";
        } else if (itemId == R.id.item_april) {
            selectedMonth = "04";
        } else if (itemId == R.id.item_mei) {
            selectedMonth = "05";
        } else if (itemId == R.id.item_juni) {
            selectedMonth = "06";
        } else if (itemId == R.id.item_juli) {
            selectedMonth = "07";
        } else if (itemId == R.id.item_agustus) {
            selectedMonth = "08";
        } else if (itemId == R.id.item_september) {
            selectedMonth = "09";
        } else if (itemId == R.id.item_oktober) {
            selectedMonth = "10";
        } else if (itemId == R.id.item_november) {
            selectedMonth = "11";
        } else if (itemId == R.id.item_desember) {
            selectedMonth = "12";
        } else if (itemId == R.id.item_semua) {
            selectedMonth = "all";
        } else {
            return false;
        }


        // Muat ulang data berdasarkan bulan yang dipilih
        fetchLaporanData();
        return true;
    }

    private void fetchLaporanData() {
        String requestUrl = LAPORAN_URL;
        if (!selectedMonth.equals("all")) {
            requestUrl += "?bulan=" + selectedMonth;
        }

        StringRequest request = new StringRequest(Request.Method.GET, requestUrl,
                response -> {
                    try {
                        Log.d("API Response", response);

                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            JSONArray data = jsonObject.getJSONArray("data");

                            // Bersihkan dataList sebelum menambahkan data baru
                            dataList.clear();

                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("id_laporan", obj.optString("id_laporan", ""));
                                    map.put("nama", obj.optString("nama_pendonor", ""));
                                    map.put("nohp", obj.optString("no_telp", ""));
                                    map.put("lokasi_donor", obj.optString("lokasi_donor", ""));
                                    map.put("goldar", obj.optString("goldar", ""));
                                    map.put("tekanan", obj.optString("tekanan_darah", ""));
                                    map.put("beratbadan", obj.optString("berat_badan", ""));
                                    map.put("rhesus", obj.optString("rhesus", ""));
                                    dataList.add(map);
                                }
                            } else {
                                // Jika tidak ada data, tampilkan pesan
                                showToast("Tidak ada data laporan untuk bulan ini.");
                            }

                        } else {
                            // Jika status bukan success
                            showToast(jsonObject.optString("message", "Gagal memuat data."));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("Error parsing data: " + e.getMessage());
                    } finally {
                        // Update ListView
                        updateListView();
                    }
                },
                error -> {
                    Log.e("API Error", "Error fetching data", error);
                    showToast("Tidak ada data laporan untuk bulan ini.");
                    dataList.clear();
                    updateListView();
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void updateListView() {
        adapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
