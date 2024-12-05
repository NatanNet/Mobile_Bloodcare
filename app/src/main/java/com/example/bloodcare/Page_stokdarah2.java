package com.example.bloodcare;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Page_stokdarah2 extends AppCompatActivity {

    private EditText etDarahTerkumpul, etKebutuhanDarah;
    private Spinner etJenisDarah;
    private Button btnSubmit;
    private String golonganDarah;
    private static final String URL = Config.BASE_URL+ "stok_darah.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_stokdarah2);

        ImageButton buttonBack = findViewById(R.id.icback);

        buttonBack.setOnClickListener(v -> finish());


        // Ambil golongan darah dari Intent
        golonganDarah = getIntent().getStringExtra("golongan_darah");

        Spinner spJenisDarah = findViewById(R.id.spinnerJenisDarah);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_darah_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJenisDarah.setAdapter(adapter);

        // Inisialisasi view
        etJenisDarah = findViewById(R.id.spinnerJenisDarah);
        etDarahTerkumpul = findViewById(R.id.editDarahTerkumpul);
        etKebutuhanDarah = findViewById(R.id.editKebutuhanDarah);
        btnSubmit = findViewById(R.id.btnSimpan);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    private void submitData() {
        String jenisDarah = etJenisDarah.getSelectedItem().toString().trim();
        String darahTerkumpul = etDarahTerkumpul.getText().toString().trim();
        String kebutuhanDarah = etKebutuhanDarah.getText().toString().trim();

        if (jenisDarah.isEmpty() || darahTerkumpul.isEmpty() || kebutuhanDarah.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Membuat objek JSON untuk data yang akan dikirim
            JSONObject data = new JSONObject();
            data.put("golongan_darah", golonganDarah);
            data.put("jenis_darah", jenisDarah);
            data.put("darah_terkumpul", darahTerkumpul);
            data.put("kebutuhan_darah", kebutuhanDarah);

            // Membuat request JSON ke server
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, data,
                    response -> {
                        try {
                            // Debug respons server
                            System.out.println("Response: " + response.toString());

                            // Cek apakah respons server berhasil
                            if (response.has("success") && response.getBoolean("success")) {
                                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                finish(); // Menutup activity setelah berhasil
                            } else {
                                String errorMessage = response.has("message") ? response.getString("message") : "Gagal menyimpan data.";
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace(); // Debug error parsing
                            Toast.makeText(this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        error.printStackTrace(); // Debug error saat request
                        Toast.makeText(this, "Gagal menyimpan data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            // Tambahkan request ke RequestQueue
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } catch (JSONException e) {
            e.printStackTrace(); // Debug error saat membuat JSON
            Toast.makeText(this, "Error creating JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}