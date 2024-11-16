package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Page_register extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonDaftar;
    private static final String REGISTER_URL = Config.BASE_URL + "login.php"; // Ganti dengan URL API Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ImageButton buttonBack = findViewById(R.id.backbutton);
        buttonDaftar = findViewById(R.id.btn_daftar);
        TextView textLogin = findViewById(R.id.login_text);

        // Inisialisasi input
        editTextUsername = findViewById(R.id.textusername);
        editTextEmail = findViewById(R.id.textemail);
        editTextPassword = findViewById(R.id.textpass);
        editTextConfirmPassword = findViewById(R.id.textconfirmpass);

        // Set OnClickListener untuk tombol back
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set OnClickListener untuk tombol daftar
        buttonDaftar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Set OnClickListener untuk teks login
        textLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_register.this, Page_login.class);
                startActivity(intent);
            }
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

        // Validasi kesesuaian password
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Konfirmasi password tidak sesuai");
            editTextConfirmPassword.requestFocus();
            return;
        }

        // Membuat request ke server menggunakan Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Mengubah response menjadi JSON
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
                            Toast.makeText(Page_register.this, "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page_register.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
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
}
