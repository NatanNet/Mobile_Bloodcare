package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Page_login extends AppCompatActivity {

    private TextInputEditText editUsername, editPassword;
    private TextInputLayout textInputLayoutPassword;
    private Button buttonLogin;
    private TextView register, forgotPass;

    private SharedPreferencess sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nonaktifkan mode malam secara default
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Set layout activity
        setContentView(R.layout.activity_main2);

        // Inisialisasi SharedPreferences
        sharedPreferences = new SharedPreferencess(this);

        // Cek jika sudah login sebelumnya
        if (sharedPreferences.isLoggedIn()) {
            // Jika sudah login, langsung menuju halaman dashboard
            int userId = sharedPreferences.getUserId();
            String usernameOrEmail = sharedPreferences.getUsernameOrEmail();

            Intent intent = new Intent(Page_login.this, Page_dashboard.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("username_or_email", usernameOrEmail);
            startActivity(intent);
            finish();
        }

        // Inisialisasi UI
        editUsername = findViewById(R.id.textusername);
        editPassword = findViewById(R.id.textpass);
        textInputLayoutPassword = findViewById(R.id.TextInputLayout); // Layout untuk validasi password
        buttonLogin = findViewById(R.id.login_button);
        register = findViewById(R.id.register_text);
        forgotPass = findViewById(R.id.forgot_password_text);

        // Tambahkan TextWatcher untuk validasi password
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak perlu implementasi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak perlu implementasi
            }
        });

        // OnClickListener untuk register
        register.setOnClickListener(v -> startActivity(new Intent(Page_login.this, Page_register.class)));

        // OnClickListener untuk forgot password
        forgotPass.setOnClickListener(v -> startActivity(new Intent(Page_login.this, Page_otpsend.class)));

        // OnClickListener untuk login
        buttonLogin.setOnClickListener(v -> {
            String usernameOrEmail = editUsername.getText() != null ? editUsername.getText().toString().trim() : "";
            String password = editPassword.getText() != null ? editPassword.getText().toString().trim() : "";

            // Validasi input username dan password
            if (TextUtils.isEmpty(usernameOrEmail) || TextUtils.isEmpty(password)) {
                Toast.makeText(Page_login.this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (!validatePassword(password)) {
                // Jika password tidak valid, tampilkan pesan
                Toast.makeText(Page_login.this, "Password tidak memenuhi kriteria", Toast.LENGTH_SHORT).show();
            } else {
                // Jika validasi lolos, lakukan login
                loginUser(usernameOrEmail, password);
            }
        });
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
            textInputLayoutPassword.setHelperText("Password harus maksimal 8 karakter");
            textInputLayoutPassword.setError(null);
            return false;
        }
    }

    // Fungsi untuk login user
    private void loginUser(String usernameOrEmail, String password) {
        String url = Config.BASE_URL + "login.php"; // Ganti dengan URL server PHP Anda

        // Membuat JSONObject untuk mengirim input sebagai JSON
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username_or_email", usernameOrEmail);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Membuat request POST dengan JsonObjectRequest
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        String status = response.getString("status");

                        if (status.equals("success")) {
                            int userId = response.getInt("user_id");
                            String username = response.getString("username"); // Pastikan server mengembalikan username
                            String email = response.getString("email"); // Pastikan server mengembalikan email
                            String redirectTo = response.getString("redirect_to");

                            // Simpan status login di SharedPreferences
                            sharedPreferences.saveLoginStatus(true);
                            sharedPreferences.saveUserId(userId);
                            sharedPreferences.saveUsernameOrEmail(username != null ? username : email);

                            Toast.makeText(Page_login.this, "Login sukses", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Page_login.this, Page_dashboard.class);
                            intent.putExtra("user_id", userId);
                            intent.putExtra("username_or_email", username != null ? username : email);
                            if (redirectTo.equals("main_activity")) {
                                // Jika data lengkap, tidak menambahkan target_fragment
                            } else if (redirectTo.equals("page_editprofil")) {
                                // Jika data tidak lengkap, arahkan ke fragment Page_detail_akun
                                intent.putExtra("user_id", 1); // Target fragment untuk Page_detail_akun
                            }

                            startActivity(intent);
                            finish();

                        } else {
                            String message = response.getString("message");
                            Toast.makeText(Page_login.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Page_login.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Page_login.this, "Gagal terhubung ke server: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }
}
