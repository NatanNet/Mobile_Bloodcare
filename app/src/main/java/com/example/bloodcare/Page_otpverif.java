package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class Page_otpverif extends AppCompatActivity {

    private EditText text1, text2, text3, text4, text5;
    private Button btnVerifikasi;
    private String verificationId;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5); // Sesuaikan dengan layout yang benar

        // Mendapatkan verificationId dan email dari intent
        verificationId = getIntent().getStringExtra("verificationId");
        email = getIntent().getStringExtra("email");

        // Log untuk memastikan data intent diterima dengan benar
        Log.d("Page_otpverif", "Email diterima: " + email);
        if (email == null) {
            Toast.makeText(this, "Email tidak diterima dari Intent", Toast.LENGTH_SHORT).show();
        }

        // Menginisialisasi komponen UI
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        btnVerifikasi = findViewById(R.id.btn_verifikasi);
        ImageButton backButton = findViewById(R.id.backbutton2);

        // Menetapkan fungsi tombol kembali
        backButton.setOnClickListener(v -> finish());

        // Menambahkan TextWatchers untuk pergerakan kursor otomatis
        addTextWatcher(text1, text2);
        addTextWatcher(text2, text3);
        addTextWatcher(text3, text4);
        addTextWatcher(text4, text5);
        addTextWatcher(text5, null);  // Tidak ada EditText selanjutnya untuk yang terakhir

        // Menangani klik tombol verifikasi
        btnVerifikasi.setOnClickListener(v -> {
            String otp = text1.getText().toString() + text2.getText().toString() +
                    text3.getText().toString() + text4.getText().toString() +
                    text5.getText().toString();

            if (otp.length() == 5) {
                verifyOtp(otp);  // Panggil fungsi verifikasi OTP
            } else {
                Toast.makeText(Page_otpverif.this, "Masukkan OTP yang valid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTextWatcher(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1 && nextEditText != null) {
                    nextEditText.requestFocus();
                } else if (s.length() == 0 && currentEditText != text1) {
                    currentEditText.focusSearch(View.FOCUS_LEFT).requestFocus();
                }
            }
        });
    }

    // Fungsi untuk memverifikasi OTP ke server
    private void verifyOtp(String otp) {
        String url = Config.BASE_URL + "verifikasi_kode_otp.php";   // URL server PHP Anda

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("otp", otp);
            jsonBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            Toast.makeText(Page_otpverif.this, "Verifikasi sukses", Toast.LENGTH_SHORT).show();
                            // Kirim email ke Page_forgotpass
                            Intent intent = new Intent(Page_otpverif.this, Page_forgotpass.class);
                            Log.d("Page_otpverif", "Email dikirim: " + email);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Page_otpverif.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Page_otpverif.this, "Kesalahan JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Page_otpverif.this, "Gagal koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }
}
