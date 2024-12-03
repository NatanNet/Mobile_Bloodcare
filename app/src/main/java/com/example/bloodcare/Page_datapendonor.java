package com.example.bloodcare;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Page_datapendonor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_datapendonor);

        ImageButton buttonBack = findViewById(R.id.btnkembali);
        buttonBack.setOnClickListener(v -> finish());

        // Ambil data dari Intent
        String nama = getIntent().getStringExtra("nama");
        String nohp = getIntent().getStringExtra("nohp");
        String lokasiDonor = getIntent().getStringExtra("lokasi_donor");
        String beratbadan = getIntent().getStringExtra("bb");
        String goldar = getIntent().getStringExtra("goldar");
        String alamat = getIntent().getStringExtra("alamat");
        String rhesus = getIntent().getStringExtra("rhesus");
        String tekanan = getIntent().getStringExtra("tekanan");
        String id_pendonor = getIntent().getStringExtra("id_pendonor");

        // Set data ke EditText
        EditText editNama = findViewById(R.id.nama1);
        EditText editNohp = findViewById(R.id.nomerhp);
        EditText editBerat = findViewById(R.id.berat);
        EditText editLokasi = findViewById(R.id.lokasi1);
        EditText editGoldar = findViewById(R.id.goldar);
        EditText editAlamat = findViewById(R.id.alamat1);
        EditText editRhesus = findViewById(R.id.resus);
        EditText editTekanan = findViewById(R.id.tekanan);

        editNama.setText(nama);
        editNohp.setText(nohp);
        editBerat.setText(beratbadan);
        editLokasi.setText(lokasiDonor);
        editGoldar.setText(goldar);
        editAlamat.setText(alamat);
        editRhesus.setText(rhesus);
        editTekanan.setText(tekanan);

        // Button Update/Insert
        Button btnUpdate = findViewById(R.id.btnsimpanpendonor);
        btnUpdate.setOnClickListener(v -> {
            // Ambil data yang diperlukan untuk update atau input
            String newGoldar = editGoldar.getText().toString().trim();
            String newBerat = editBerat.getText().toString().trim();
            String newRhesus = editRhesus.getText().toString().trim();
            String newTekanan = editTekanan.getText().toString().trim();

            // Validasi bahwa semua data sudah diisi
            if (newGoldar.isEmpty() || newBerat.isEmpty() || newRhesus.isEmpty() || newTekanan.isEmpty()) {
                Toast.makeText(Page_datapendonor.this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                // Tentukan apakah ini adalah update atau input
                if (id_pendonor != null && !id_pendonor.isEmpty()) {
                    // Update data pendonor
                    updateDonorData(id_pendonor, newGoldar, newBerat, newRhesus, newTekanan, () -> {
                        // Setelah update berhasil, lakukan insert laporan
//                        insertLaporanData(nama, nohp, lokasiDonor, newBerat, newGoldar, newTekanan, newRhesus);
                    });
                }
            }
        });
    }

    private void updateDonorData(String id_pendonor, String goldar, String beratBadan, String rhesus, String tekananDarah, Runnable onSuccess) {
        String url = Config.BASE_URL + "update_pendonor.php";

        HashMap<String, String> params = new HashMap<>();
        params.put("id_pendonor", id_pendonor);
        params.put("goldar", goldar);
        params.put("berat_badan", beratBadan);
        params.put("rhesus", rhesus);
        params.put("tekanan_darah", tekananDarah); // Pastikan parameter ini sesuai dengan API Anda

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            Toast.makeText(Page_datapendonor.this, "Data pendonor berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            onSuccess.run();
                        } else {
                            Toast.makeText(Page_datapendonor.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Page_datapendonor.this, "Response Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VolleyError", "Error: " + error.getMessage());
                    Toast.makeText(Page_datapendonor.this, "Server Error", Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}
//    private void insertLaporanData(String nama, String nohp, String lokasiDonor, String beratBadan, String goldar, String tekananDarah, String rhesus) {
//        String url = Config.BASE_URL + "api_input_laporan.php";
//
//        // Log untuk debug
//        Log.d("PARAMS", "nama_pendonor: " + getIntent().getStringExtra("nama"));
//        Log.d("PARAMS", "lokasi_donor: " + getIntent().getStringExtra("lokasi_donor"));
//        Log.d("PARAMS", "no_telp: " + getIntent().getStringExtra("nohp"));
//        Log.d("PARAMS", "berat_badan: " + beratBadan);
//        Log.d("PARAMS", "goldar: " + goldar);
//        Log.d("PARAMS", "tekanan_darah: " + tekananDarah);
//        Log.d("PARAMS", "rhesus: " + rhesus);
//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("nama_pendonor", nama);
//        params.put("lokasi_donor", lokasiDonor);
//        params.put("no_telp", nohp);
//        params.put("berat_badan", beratBadan);
//        params.put("goldar", goldar);
//        params.put("tekanan_darah", tekananDarah);
//        params.put("rhesus", rhesus);
//
//        JsonObjectRequest insertRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
//                response -> {
//                    try {
//                        if (response.getString("status").equals("success")) {
//                            Toast.makeText(Page_datapendonor.this, "Data laporan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//                            finish();
//                        } else {
//                            Toast.makeText(Page_datapendonor.this, response.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(Page_datapendonor.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> {
//                    String response = error.networkResponse != null ? new String(error.networkResponse.data) : error.getMessage();
//                    Log.e("VolleyError", "Error Response: " + response);
//                    Toast.makeText(Page_datapendonor.this, "Server Error: " + response, Toast.LENGTH_LONG).show();
//                });
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(insertRequest);
//    }


