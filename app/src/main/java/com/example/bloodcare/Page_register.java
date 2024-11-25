package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

public class Page_register extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private TextInputLayout textInputLayoutPassword, textInputLayoutConfirmPassword;
    private Button buttonDaftar;
    private static final String REGISTER_URL = "http://192.168.1.94/website_bloodcare/api/mobile/register.php";// Ganti dengan URL API Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Inisialisasi komponen UI
        ImageButton buttonBack = findViewById(R.id.backbutton);
        buttonDaftar = findViewById(R.id.btn_daftar);
        TextView textLogin = findViewById(R.id.login_text);

        editTextUsername = findViewById(R.id.textusername);
        editTextEmail = findViewById(R.id.textemail);
        editTextPassword = findViewById(R.id.textpass);
        editTextConfirmPassword = findViewById(R.id.textconfirmpass);

        textInputLayoutPassword = findViewById(R.id.TextInputLayout2);
        textInputLayoutConfirmPassword = findViewById(R.id.confrimpass2);

        // Set listener untuk tombol back
        buttonBack.setOnClickListener(v -> finish());

        // Set listener untuk tombol daftar
        buttonDaftar.setOnClickListener(v -> registerUser());

        // Set listener untuk teks login
        textLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Page_register.this, Page_login.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        // Ambil data dari EditText
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validasi data input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi password
        if (!validatePassword(password)) {
            return; // Jika password tidak valid, hentikan proses
        }

        // Validasi kesesuaian password dan konfirmasi password
        if (!password.equals(confirmPassword)) {
            textInputLayoutConfirmPassword.setError("Konfirmasi password tidak sesuai");
            textInputLayoutConfirmPassword.requestFocus();
            return;
        } else {
            textInputLayoutConfirmPassword.setError(null);
        }

        // Membuat request ke server menggunakan Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ServerResponse", response);  // Menambahkan log untuk melihat response
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                Toast.makeText(Page_register.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Page_register.this, Page_register_succes.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Page_register.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Page_register.this, "Kesalahan parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani error di sini
                        Log.e("VolleyError", error.toString());
                        Toast.makeText(Page_register.this, "Gagal menghubungi server: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Membuat data yang akan dikirim ke server
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        // Menambahkan request ke dalam RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Fungsi untuk validasi password
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
                textInputLayoutPassword.setError("Password harus mengandung huruf besar, huruf kecil, angka, dan simbol");
                textInputLayoutPassword.setHelperText(null);
                return false;
            }
        } else {
            textInputLayoutPassword.setHelperText("Password minimal 8 karakter");
            textInputLayoutPassword.setError(null);
            return false;
        }
    }
}
