package com.example.bloodcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Page_forgotpass extends AppCompatActivity {

    private EditText editTextNewPassword, editTextConfirmPassword;
    private Button buttonSubmit;
    private TextInputLayout textInputLayoutPassword, textInputLayoutConfirmPassword;
    private String email;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_pass);

        // Ambil email dari Intent
        if (getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email");
        }

        Log.d("Page_forgotpass", "Email diterima dari Intent: " + email);
        Toast.makeText(this, "Email diterima: " + email, Toast.LENGTH_LONG).show();

        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Email tidak ditemukan, silakan coba lagi.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inisialisasi komponen UI
        editTextNewPassword = findViewById(R.id.textpass);
        editTextConfirmPassword = findViewById(R.id.textconfirmpass);
        buttonSubmit = findViewById(R.id.btn_confrim);
        textInputLayoutPassword = findViewById(R.id.TextInputLayout3);
        textInputLayoutConfirmPassword = findViewById(R.id.konfirmpss);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memperbarui kata sandi...");

        // Fungsi tombol Submit
        buttonSubmit.setOnClickListener(v -> {
            String newPassword = editTextNewPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Page_forgotpass.this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                textInputLayoutConfirmPassword.setError("Kata sandi tidak cocok");
            } else if (validatePassword(newPassword)) {
                gantiPassword(email, newPassword);
            }
        });
    }

    private boolean validatePassword(String password) {
        if (password.length() >= 8) {
            Pattern uppercase = Pattern.compile("[A-Z]");
            Pattern lowercase = Pattern.compile("[a-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern symbol = Pattern.compile("[^a-zA-Z0-9]");

            boolean hasUppercase = uppercase.matcher(password).find();
            boolean hasLowercase = lowercase.matcher(password).find();
            boolean hasDigit = digit.matcher(password).find();
            boolean hasSymbol = symbol.matcher(password).find();

            if (hasUppercase && hasLowercase && hasDigit && hasSymbol) {
                textInputLayoutPassword.setHelperText("Password Anda kuat");
                textInputLayoutPassword.setError(null);
                return true;
            } else {
                textInputLayoutPassword.setError("Password harus mengandung huruf besar, kecil, angka, dan simbol");
            }
        } else {
            textInputLayoutPassword.setError("Password minimal 8 karakter");
        }
        return false;
    }

    private void gantiPassword(String email, String newPassword) {
        // Log untuk memastikan nilai newPassword sebelum request
        Log.d("Page_forgotpass", "Mencoba mengganti password untuk email: " + email);
        Log.d("Page_forgotpass", "Password baru: " + newPassword);

        // Cek jika newPassword kosong atau null
        if (newPassword == null || newPassword.isEmpty()) {
            Toast.makeText(Page_forgotpass.this, "Password baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        String url = Config.BASE_URL + "ganti_password.php";

        // Membuat JSONObject untuk mengirim data dalam format JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            Toast.makeText(Page_forgotpass.this, "Kata sandi berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Page_forgotpass.this, Page_forgotpass_sucess.class));
                            finish();
                        } else {
                            Toast.makeText(Page_forgotpass.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Page_forgotpass.this, "Kesalahan JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    String errorMessage = error.getMessage() != null ? error.getMessage() : "Terjadi kesalahan jaringan";
                    Toast.makeText(Page_forgotpass.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                // Mengirimkan body request dalam format JSON
                return jsonObject.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Pastikan header menggunakan application/json
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}
