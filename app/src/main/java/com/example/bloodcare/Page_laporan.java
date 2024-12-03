package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private final String LAPORAN_URL = Config.BASE_URL + "get_laporan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_laporan);

        listView1 = findViewById(R.id.listView1);
        ImageView iconLaporan = findViewById(R.id.iconLaporan);
        ImageButton buttonBack = findViewById(R.id.icback3);

        buttonBack.setOnClickListener(v -> finish());

        // Popup Menu Configuration
        iconLaporan.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(Page_laporan.this, view);
            popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.item_senin) {
                    showToast("Senin dipilih");
                } else if (itemId == R.id.item_selasa) {
                    showToast("Selasa dipilih");
                } else if (itemId == R.id.item_rabu) {
                    showToast("Rabu dipilih");
                } else if (itemId == R.id.item_kamis) {
                    showToast("Kamis dipilih");
                } else if (itemId == R.id.item_jumat) {
                    showToast("Jumat dipilih");
                } else if (itemId == R.id.item_minggu_ini) {
                    showToast("Minggu Ini dipilih");
                } else if (itemId == R.id.item_minggu_lalu) {
                    showToast("Minggu Lalu dipilih");
                } else if (itemId == R.id.item_bulan_ini) {
                    showToast("Bulan Ini dipilih");
                } else if (itemId == R.id.item_bulan_lalu) {
                    showToast("Bulan Lalu dipilih");
                } else if (itemId == R.id.item_semua) {
                    showToast("Semua dipilih");
                }
                return true;

            });
            popup.show();
        });

        fetchLaporanData();

        // Tambahkan Listener pada ListView
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

    private void fetchLaporanData() {
        StringRequest request = new StringRequest(Request.Method.GET, LAPORAN_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            dataList.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id_laporan", obj.optString("id_laporan"));
                                map.put("nama", obj.getString("nama_pendonor"));
                                map.put("nohp", obj.getString("no_telp"));
                                map.put("lokasi_donor", obj.getString("lokasi_donor"));
                                map.put("goldar", obj.getString("goldar"));
                                map.put("tekanan", obj.getString("tekanan_darah"));
                                map.put("beratbadan", obj.getString("berat_badan"));
                                map.put("rhesus", obj.getString("rhesus"));
                                dataList.add(map);
                            }

                            listView1.setAdapter(new Page_laporan3(Page_laporan.this, dataList));
                        } else {
                            showToast(jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        showToast("Error parsing data: " + e.getMessage());
                    }
                },
                error -> showToast("Gagal mengambil data: " + error.getMessage()));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
