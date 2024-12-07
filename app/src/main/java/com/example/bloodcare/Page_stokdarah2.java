package com.example.bloodcare;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Page_stokdarah2 extends AppCompatActivity {

    private EditText etDarahTerkumpul, etKebutuhanDarah, etrhesus, etgoldar;
    private Spinner spJenisDarah;
    private Button btnSubmit;
    private String golonganDarah;
    private String rhesus;
    private boolean isUpdate; // Variabel untuk menyimpan mode (insert/update)
    private static final String URL = Config.BASE_URL + "stok_darah.php";
    private static final String requestUrl = Config.BASE_URL + "get_stokdarah2.php";

    // Map untuk konversi jenis darah ke singkatan
    private static final Map<String, String> jenisDarahMap = new HashMap<>();

    static {
        jenisDarahMap.put("Whole Blood", "WB");
        jenisDarahMap.put("Packed Red Cells", "PRC");
        jenisDarahMap.put("Thrombocyte Concentrate", "TC");
        jenisDarahMap.put("Fresh Frozen Plasma", "FFP");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_stokdarah2);

        // Inisialisasi elemen UI
        etgoldar = findViewById(R.id.editgoldar);
        etrhesus = findViewById(R.id.editrhesus);
        etDarahTerkumpul = findViewById(R.id.editDarahTerkumpul);
        btnSubmit = findViewById(R.id.btnSimpan);

        // Ambil data dari Intent
        golonganDarah = getIntent().getStringExtra("golongan_darah");
        rhesus = getIntent().getStringExtra("rhesus");
        isUpdate = getIntent().getBooleanExtra("is_update", false); // Flag untuk update
        int jumlahStok = getIntent().getIntExtra("jumlah_stok", 0); // Ambil stok yang ada

        // Set nilai golongan darah dan rhesus ke EditText
        etgoldar.setText(golonganDarah);  // Isi EditText golongan darah
        etrhesus.setText(rhesus);         // Isi EditText rhesus

//        // Menampilkan golongan darah di TextView
//        etKebutuhanDarah.setText("Golongan Darah: " + golonganDarah + " (" + rhesus + ")");

        // Jika mode update, set jumlah darah ke EditText
        if (isUpdate) {
            etDarahTerkumpul.setText(String.valueOf(jumlahStok));  // Set jumlah stok yang ingin diupdate
            btnSubmit.setText("Update Stok"); // Ganti tombol menjadi Update
        } else {
            btnSubmit.setText("Tambah Stok"); // Tombol untuk menambah stok
        }

        btnSubmit.setOnClickListener(v -> {
            String jumlahDarah = etDarahTerkumpul.getText().toString().trim();




            try {


                if (isUpdate) {
                    // Panggil fungsi untuk memperbarui stok darah
                    updateStokDarah(golonganDarah, rhesus, Integer.parseInt(jumlahDarah));
                } else {
                    // Panggil fungsi untuk menambah stok darah
                    submitData(golonganDarah, rhesus, jumlahDarah);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Input jumlah/kebutuhan harus berupa angka yang valid!", Toast.LENGTH_SHORT).show();
            }
        });


        // Menyiapkan tombol kembali
        ImageButton buttonBack = findViewById(R.id.icback);
        buttonBack.setOnClickListener(v -> finish());

        // Inisialisasi Spinner untuk jenis darah
        spJenisDarah = findViewById(R.id.spinnerJenisDarah);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_darah_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJenisDarah.setAdapter(adapter);

        // Menambahkan listener untuk Spinner
        spJenisDarah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Ambil jenis darah yang dipilih
                String jenisDarahLong = parentView.getItemAtPosition(position).toString();
                String jenisDarah = jenisDarahMap.getOrDefault(jenisDarahLong, "");

                // Panggil getData dengan singkatan jika dalam mode update
                if (isUpdate) {
                    getData(jenisDarah);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Tidak ada tindakan ketika tidak ada item yang dipilih
            }
        });
    }

    private void submitData(String golonganDarah, String rhesus, String jumlahDarahString) {
        int jumlahDarah;

        try {
            jumlahDarah = Integer.parseInt(jumlahDarahString); // Parse jumlah darah
            if (jumlahDarah <= 0) {
                Toast.makeText(this, "Jumlah darah harus lebih besar dari 0!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Jumlah darah harus berupa angka yang valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        String jenisDarahLong = spJenisDarah.getSelectedItem().toString().trim();
        String jenisDarah = jenisDarahMap.getOrDefault(jenisDarahLong, "");

        if (jenisDarah.isEmpty()) {
            Toast.makeText(this, "Pilih jenis darah yang valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject data = new JSONObject();
            data.put("goldar", golonganDarah);
            data.put("jenis_darah", jenisDarah);
            data.put("stok", jumlahDarah);
            data.put("rhesus", rhesus);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, data,
                    response -> {
                        try {
                            if (response.has("status") && response.getString("status").equals("success")) {
                                Toast.makeText(this, "Stok darah berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                String errorMessage = response.has("message") ? response.getString("message") : "Gagal menambahkan stok.";
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Gagal menambahkan stok: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    private void getData(String jenisDarah) {
        if (golonganDarah == null || rhesus == null || jenisDarah == null) {
            Toast.makeText(this, "Parameter tidak lengkap. Silakan periksa kembali.", Toast.LENGTH_SHORT).show();
            return;
        }

        String completeUrl = requestUrl + "?golongan_darah=" + golonganDarah + "&rhesus=" + rhesus + "&jenis_darah=" + jenisDarah;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, completeUrl, null,
                response -> {
                    try {
                        if (response.optBoolean("success", false)) {
                            JSONObject data = response.getJSONObject("data");

                            // Mengambil dan mengisi nilai stok darah
                            etDarahTerkumpul.setText(data.optString("stok", ""));


                            // Mengambil dan mengisi nilai golongan darah dan rhesus
                            golonganDarah = data.optString("goldar", "");
                            rhesus = data.optString("rhesus", "");

                            // Menampilkan nilai golongan darah dan rhesus pada EditText
                            etgoldar.setText(golonganDarah);  // Isi EditText golongan darah
                            etrhesus.setText(rhesus);         // Isi EditText rhesus

                            Toast.makeText(this, "Data berhasil dimuat", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = response.optString("message", "Data tidak ditemukan.");
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void updateStokDarah(String golonganDarah, String rhesus, int jumlahDarah) {
        String updateUrl = Config.BASE_URL + "update_stokdarah.php";

        // Ambil jenis darah dari Spinner
        String jenisDarahLong = spJenisDarah.getSelectedItem().toString().trim();
        String jenisDarah = jenisDarahMap.getOrDefault(jenisDarahLong, "");

        // Validasi input
        if (jenisDarah.isEmpty() || jumlahDarah <= 0) {
            Toast.makeText(this, "Jenis darah harus dipilih dan jumlah stok harus valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Membuat objek JSON hanya untuk data yang perlu di-update
            JSONObject data = new JSONObject();
            data.put("goldar", golonganDarah);     // Golongan darah (A, B, O, AB)
            data.put("jenis_darah", jenisDarah);  // Jenis darah dalam bentuk singkatan (WB, PRC, TC, FFP)
            data.put("stok", jumlahDarah);        // Jumlah darah yang diupdate
            data.put("rhesus", rhesus);           // Rhesus (+ atau -)

            // Membuat request JSON ke server untuk update stok
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, updateUrl, data,
                    response -> {
                        try {
                            // Cek apakah respons dari server sukses
                            if (response.has("success") && response.getBoolean("success")) {
                                Toast.makeText(this, "Stok darah berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                finish(); // Kembali ke halaman sebelumnya setelah update
                            } else {
                                String errorMessage = response.optString("message", "Gagal memperbarui stok.");
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Gagal memperbarui stok: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            // Tambahkan request ke RequestQueue
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
