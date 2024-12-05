package com.example.bloodcare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Page_acaradonor extends AppCompatActivity {

    private EditText etDate;
    private EditText editTextData, editTextFasiltas, editTextwaktu;
    private Button buttonSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_acaradonor);

        etDate = findViewById(R.id.etDate);
        editTextData = findViewById(R.id.editTextLokasiDonor);
        editTextFasiltas = findViewById(R.id.editTextFasilitas);
        editTextwaktu = findViewById(R.id.editTextwaktuDonor);
        buttonSimpan = findViewById(R.id.btnSave);
        ImageButton buttonback = findViewById(R.id.imageButton3);

        // Cek apakah Intent membawa data untuk mode edit
        Intent intent = getIntent();
        boolean isEditMode = intent.getBooleanExtra("is_edit_mode", false);
        String idAcara = intent.getStringExtra("id_acara");

        if (isEditMode) {
            // Mode edit, isi data ke EditText
            editTextData.setText(intent.getStringExtra("lokasi"));
            editTextFasiltas.setText(intent.getStringExtra("fasilitas"));
            editTextwaktu.setText(intent.getStringExtra("time_waktu"));
            etDate.setText(intent.getStringExtra("tgl_acara"));
            buttonSimpan.setText("Perbarui"); // Ubah teks tombol menjadi "Perbarui"
        } else {
            buttonSimpan.setText("Simpan"); // Default teks tombol
        }

        // Klik Listener Tombol
        buttonSimpan.setOnClickListener(v -> {
            String lokasiDonor = editTextData.getText().toString().trim();
            String tanggalAcara = etDate.getText().toString().trim();
            String fasilitas = editTextFasiltas.getText().toString().trim();
            String waktu = editTextwaktu.getText().toString().trim();

            if (lokasiDonor.isEmpty() || tanggalAcara.isEmpty() || waktu.isEmpty()) {
                Toast.makeText(Page_acaradonor.this, "Lengkapi data", Toast.LENGTH_SHORT).show();
            } else {
                if (isEditMode) {
                    // Mode Edit
                    updateData(idAcara, lokasiDonor, fasilitas, waktu, tanggalAcara);
                } else {
                    // Mode Insert
                    simpanDataAcaraDonor(lokasiDonor, tanggalAcara, fasilitas, waktu);
                }
            }
        });

        // Tombol kembali
        buttonback.setOnClickListener(v -> finish());

        // Menampilkan DatePicker saat klik EditText
        etDate.setOnClickListener(v -> showDatePicker());

        // Menampilkan TimePicker saat klik EditText waktu
        editTextwaktu.setOnClickListener(v -> showTimePicker());
    }

    // Fungsi untuk memilih tanggal
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Page_acaradonor.this,
                R.style.DatePickerDialogTheme,
                (view, selectedYear, selectedMonth, selectedDay) ->
                        etDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear),
                year, month, day
        );
        datePickerDialog.show();
    }

    // Fungsi untuk memilih waktu
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Page_acaradonor.this,
                R.style.DatePickerDialogTheme,
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format("%02d:%02d:00", hourOfDay, minuteOfHour);
                    editTextwaktu.setText(time);
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }


    // Fungsi untuk mengirimkan data acara donor ke server PHP
    private void simpanDataAcaraDonor(String lokasiDonor, String tanggal, String fasilitas, String waktu) {
        try {
            // Mengonversi tanggal yang ada di etDate ke format yang sesuai (YYYY-MM-DD)
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(tanggal);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = outputFormat.format(date);

            // Kirim tanggal yang telah diformat ke server
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL + "tambah_acara.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    // Menampilkan Toast jika data berhasil disimpan
                                    Toast.makeText(Page_acaradonor.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();

                                    // Setelah data disimpan, pindah ke Fragment 2 di DashboardActivity
                                    Intent intent = new Intent(Page_acaradonor.this, Page_dashboard.class);
                                    intent.putExtra("target_fragment", 2); // Fragment 2
                                    startActivity(intent);
                                    finish(); // Menutup Activity saat ini
                                } else {
                                    Toast.makeText(Page_acaradonor.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(Page_acaradonor.this, "Kesalahan parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Tangani error jika tidak dapat menghubungi server
                            Toast.makeText(Page_acaradonor.this, "Gagal menghubungi server: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("lokasi", lokasiDonor);
                    params.put("fasilitas", fasilitas);
                    params.put("time_waktu", waktu);
                    params.put("tgl_acara", formattedDate); // Mengirimkan tanggal yang diformat
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Page_acaradonor.this);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Page_acaradonor.this, "Format tanggal salah", Toast.LENGTH_SHORT).show();
        }
    }
    // Fungsi untuk mengupdate data
    private void updateData(String idAcara, String lokasi, String fasilitas, String timeWaktu, String tglAcara) {
        String UPDATE_URL = Config.BASE_URL + "edit_acara.php";

        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_URL,
                response -> {
                    Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    finish(); // Kembali ke list
                },
                error -> Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_acara", idAcara);
                params.put("lokasi", lokasi);
                params.put("fasilitas", fasilitas);
                params.put("time_waktu", timeWaktu);
                params.put("tgl_acara", tglAcara);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
}
    }