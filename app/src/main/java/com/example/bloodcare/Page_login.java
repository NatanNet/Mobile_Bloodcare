package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class Page_login extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button buttonLogin;
    private TextView register, forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nonaktifkan mode malam secara default
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Set layout activity
        setContentView(R.layout.activity_main2);

        // Inisialisasi UI
        editUsername = findViewById(R.id.textusername);
        editPassword = findViewById(R.id.textpass);
        buttonLogin = findViewById(R.id.login_button);
        register = findViewById(R.id.register_text);
        forgotPass = findViewById(R.id.forgot_password_text);

        // OnClickListener untuk register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Page_login.this, Page_register.class));
            }
        });

        // OnClickListener untuk forgot password
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Page_login.this, Page_otpsend.class));
            }
        });

        // OnClickListener untuk login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_or_email = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                // Validasi input
                if (TextUtils.isEmpty(username_or_email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Page_login.this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(username_or_email, password);
                }
            }
        });
    }

    // Fungsi untuk login user
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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                int userId = response.getInt("user_id");
                                String redirectTo = response.getString("redirect_to");

                                Toast.makeText(Page_login.this, "Login sukses", Toast.LENGTH_SHORT).show();

                                // Redirect ke halaman sesuai respon
                                if (redirectTo.equals("main_activity")) {
                                    // Jika ingin membuka Main Activity
                                    Intent intent = new Intent(Page_login.this, Page_dashboard.class);
//                                    intent.putExtra("user_id", userId);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    // Jika ingin membuka ActivityDashboard dan langsung menampilkan Fragment Page_edit_akun
                                    Intent intent = new Intent(Page_login.this, Page_dashboard.class);
                                    intent.putExtra("user_id", userId);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                String message = response.getString("message");
                                Toast.makeText(Page_login.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Page_login.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page_login.this, "Gagal terhubung ke server: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }


}
