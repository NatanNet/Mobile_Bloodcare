package com.example.bloodcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Page_forgotpass extends AppCompatActivity {

    private EditText editTextNewPassword, editTextConfirmPassword;
    private Button buttonSubmit;
    private String email;  // Variabel untuk menampung email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_pass);

        // Ambil email dari intent
        email = getIntent().getStringExtra("email");

        editTextNewPassword = findViewById(R.id.textpass);
        editTextConfirmPassword = findViewById(R.id.textconfirmpass);
        buttonSubmit = findViewById(R.id.btn_confrim);

        buttonSubmit.setOnClickListener(v -> {
            String newPassword = editTextNewPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Page_forgotpass.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(Page_forgotpass.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Jika password valid, kirimkan ke server untuk diperbarui
                gantiPassword(email, newPassword);
            }
        });
    }

    // Fungsi untuk mengganti password
    private void gantiPassword(String email, String newPassword) {
        // URL server untuk mengganti password (sesuaikan dengan URL server Anda)
        String url = Config.BASE_URL + "ganti_password.php"; // Ganti dengan URL server PHP Anda
        // Lakukan request ke server untuk mengganti password, gunakan email yang diteruskan
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("success")) {
                                Toast.makeText(Page_forgotpass.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                // Lakukan navigasi ke halaman login atau halaman lain setelah password diubah
                            } else {
                                Toast.makeText(Page_forgotpass.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Page_forgotpass.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page_forgotpass.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Kirimkan email dan password baru ke server
                Map<String, String> params = new HashMap<>();
                params.put("email", email);  // Mengirim email yang diteruskan
                params.put("new_password", newPassword);  // Mengirim password baru
                return params;
            }
        };

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(Page_forgotpass.this);
        requestQueue.add(stringRequest);
    }
}
