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
        editTextData = findViewById(R.id.editTextLokasiDonor); // Lokasi Donor
        editTextFasiltas = findViewById(R.id.editTextFasilitas); // Fasilitas Donor
        editTextwaktu = findViewById(R.id.editTextwaktuDonor);

        buttonSimpan = findViewById(R.id.btnSave);
        ImageButton buttonback = findViewById(R.id.imageButton3);

        // Menampilkan DatePicker saat klik EditText
        etDate.setOnClickListener(v -> showDatePicker());

        // Ketika tombol simpan diklik
        buttonSimpan.setOnClickListener(v -> {
            String lokasiDonor = editTextData.getText().toString().trim();
            String tanggalAcara = etDate.getText().toString().trim();
            String fasilitas = editTextFasiltas.getText().toString().trim();  // ngambil data dari edittext fasilitas
            String waktu = editTextwaktu.getText().toString().trim();   // ngambil data dari edittext waktu

            if (lokasiDonor.isEmpty() || tanggalAcara.isEmpty() || waktu.isEmpty()) {
                // Jika data belum diisi
                Toast.makeText(Page_acaradonor.this, "Lengkapi data", Toast.LENGTH_SHORT).show();
            } else {
                // Jika data valid, kirim data ke server
                simpanDataAcaraDonor(lokasiDonor, tanggalAcara, fasilitas, waktu);
            }
        });

        // Tombol kembali
        buttonback.setOnClickListener(v -> finish());

        // Menambahkan klik listener untuk input waktu
        editTextwaktu.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    Page_acaradonor.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // Format waktu menjadi HH:mm:ss
                            String time = String.format("%02d:%02d:00", hourOfDay, minute);  // Memastikan format adalah HH:mm:ss
                            editTextwaktu.setText(time);  // Menampilkan waktu di EditText
                        }
                    }, hour, minute, true);

            timePickerDialog.show();
        });
    }

    // Fungsi untuk memilih tanggal
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Page_acaradonor.this,
                (view, selectedYear, selectedMonth, selectedDay) ->
                        etDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear),
                year, month, day
        );
        datePickerDialog.show();
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.94/website_bloodcare/api/mobile/tambah_acara.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    // Menampilkan Toast jika data berhasil disimpan
                                    Toast.makeText(Page_acaradonor.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();

                                    // Setelah data disimpan, pindah ke Page_acaradonor2
                                    Intent intent = new Intent(Page_acaradonor.this, Page_acara.class);
                                    startActivity(intent);
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
                    params.put("tgl_acara", formattedDate);  // Mengirimkan tanggal yang diformat
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
}
