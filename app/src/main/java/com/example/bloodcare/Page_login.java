package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main2);

        editUsername = findViewById(R.id.textusername);
        editPassword = findViewById(R.id.textpass);
        textInputLayoutPassword = findViewById(R.id.TextInputLayout);
        buttonLogin = findViewById(R.id.login_button);
        register = findViewById(R.id.register_text);
        forgotPass = findViewById(R.id.forgot_password_text);

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        register.setOnClickListener(v -> startActivity(new Intent(Page_login.this, Page_register.class)));

        forgotPass.setOnClickListener(v -> startActivity(new Intent(Page_login.this, Page_otpsend.class)));

        buttonLogin.setOnClickListener(v -> {
            String usernameOrEmail = editUsername.getText() != null ? editUsername.getText().toString().trim() : "";
            String password = editPassword.getText() != null ? editPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(usernameOrEmail) || TextUtils.isEmpty(password)) {
                Toast.makeText(Page_login.this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (!validatePassword(password)) {
                Toast.makeText(Page_login.this, "Password tidak memenuhi kriteria", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(usernameOrEmail, password);
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

    private void loginUser(String usernameOrEmail, String password) {
        String url = Config.BASE_URL + "login.php";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username_or_email", usernameOrEmail);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        String status = response.getString("status");

                        if ("success".equals(status)) {
                            int userId = response.getInt("user_id");
                            String username = response.optString("username", "");
                            String email = response.optString("email", "");
                            String redirect_to = response.optString("redirect_to", "");

                            Toast.makeText(Page_login.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                            // Membuat intent untuk halaman dashboard
                            Intent intent = new Intent(Page_login.this, Page_dashboard.class);
                            intent.putExtra("user_id", userId);
                            intent.putExtra("username_or_email", username.isEmpty() ? email : username);

                            // Periksa nilai dari redirect_to dan tentukan targetnya
                            if ("page_editprofil".equals(redirect_to)) {
                                intent.putExtra("redirect_to", "page_editprofil");
                            } else {
                                intent.putExtra("redirect_to", "page_dashboard"); // Default ke dashboard
                            }

                            startActivity(intent);
                        } else {
                            Toast.makeText(Page_login.this, "Login gagal", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(Page_login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }
}
