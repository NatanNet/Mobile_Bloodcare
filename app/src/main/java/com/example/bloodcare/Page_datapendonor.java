package com.example.bloodcare;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        String id_akun = getIntent().getStringExtra("id_akun");

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

                    sendSertifikatRequest(getApplicationContext(),id_akun,lokasiDonor);
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
    public static void sendSertifikatRequest(final Context context, String id_akun, String lokasi) {
        // URL API yang akan dikonsumsi
        String url = "http://" + Config.URL + "/website_bloodcare/pdf.php";  // Ganti dengan URL yang benar jika perlu
        System.out.println("url api sertifikat: " + url);

        // Membuat parameter untuk x-www-form-urlencoded
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);

                        try {
                            // Parsing response jika perlu
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success")) {
                                String successMessage = jsonResponse.getString("success");
                                String link = jsonResponse.getString("link");

                                // Menampilkan dialog jika sertifikat berhasil dibuat
                                showCertificateDialog(context, successMessage, link);
                            } else {
                                Log.d(TAG, "Error: Tidak ada field success pada response.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menangani error jaringan
                        if (error.networkResponse != null) {
                            Log.e(TAG, "Volley error code: " + error.networkResponse.statusCode);
                        } else {
                            Log.e(TAG, "Volley error: " + error.getMessage());
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Menyusun parameter yang akan dikirim menggunakan x-www-form-urlencoded
                Map<String, String> params = new HashMap<>();
                params.put("id_akun", id_akun);
                params.put("lokasi", lokasi);
                return params;
            }
        };

        // Membuat RequestQueue untuk Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Menambahkan request ke queue Volley
        requestQueue.add(stringRequest);
    }

    private static void showCertificateDialog(final Context context, String message, final String link) {
        // Membuat intent untuk membuka sertifikat
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + Config.URL + "/website_bloodcare/" + link));

        // Cek jika context bukan Activity (misalnya context aplikasi)
        if (!(context instanceof Activity)) {
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Menambahkan flag untuk konteks non-Activity
        }

        // Memulai aktivitas untuk membuka URL
        context.startActivity(browserIntent);
    }



}
