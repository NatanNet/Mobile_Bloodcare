package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
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

public class Page_otpsend extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendOtpButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        emailEditText = findViewById(R.id.EtEmail);
        sendOtpButton = findViewById(R.id.btn_kirim);
        ImageButton buttonBack = findViewById(R.id.backbutton4);

        // Set listener untuk tombol back
        buttonBack.setOnClickListener(v -> finish());

        // Inisialisasi RequestQueue Volley
        requestQueue = Volley.newRequestQueue(this);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                // Menonaktifkan tombol setelah diklik
                sendOtpButton.setEnabled(false);

                String email = emailEditText.getText().toString().trim();
                if (!email.isEmpty()) {
                    sendOtp(email);
                } else {
                    Toast.makeText(Page_otpsend.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendOtp(String email) {
        String url = Config.BASE_URL + "send_otp.php";  // Ganti dengan URL endpoint API Anda

        // Membuat JSON object untuk data yang dikirim
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Membuat request POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(Page_otpsend.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();

                                // Transition to otp_input activity
                                Intent intent = new Intent(Page_otpsend.this, Page_otpverif.class);
                                intent.putExtra("email",email);

                                startActivity(intent);
                            } else {
                                Toast.makeText(Page_otpsend.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Page_otpsend.this, "Response parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Page_otpsend.this, "Failed to send OTP: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Menambahkan request ke RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
