package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Page_stokdarah extends AppCompatActivity {

    // Declare TextViews for displaying the stock data
    private TextView stokAPlus, stokAMinus, stokBPlus, stokBMinus, stokABPlus, stokABMinus, stokOPlus, stokOMinus;

    // URL for fetching stock data from the server
    private static final String URL = Config.BASE_URL + "get_stokdarah.php";
    private static final int REQUEST_CODE_UPDATE = 1; // Request code for activity result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_stokdarah);

        // Initialize the back button and its click listener
        ImageButton buttonBack = findViewById(R.id.icback2);
        buttonBack.setOnClickListener(v -> finish());

        // Initialize the TextViews
        stokAPlus = findViewById(R.id.DarahTerkumpulAplus);
        stokAMinus = findViewById(R.id.DarahTerkumpulAmin);
        stokBPlus = findViewById(R.id.DarahTerkumpulBplus);
        stokBMinus = findViewById(R.id.DarahTerkumpulBmin);
        stokABPlus = findViewById(R.id.DarahTerkumpulABplus);
        stokABMinus = findViewById(R.id.DarahTerkumpulABmin);
        stokOPlus = findViewById(R.id.DarahTerkumpulOplus);
        stokOMinus = findViewById(R.id.DarahTerkumpulOmin);

        // Initialize ImageViews for the edit icons
        ImageView imgAPlus = findViewById(R.id.iconEditAplus);
        ImageView imgAMinus = findViewById(R.id.iconEditAmin);
        ImageView imgBPlus = findViewById(R.id.iconEditBplus);
        ImageView imgBMinus = findViewById(R.id.iconEditBmin);
        ImageView imgABPlus = findViewById(R.id.iconEditABplus);
        ImageView imgABMinus = findViewById(R.id.iconEditABmin);
        ImageView imgOPlus = findViewById(R.id.iconEditOplus);
        ImageView imgOMinus = findViewById(R.id.iconEditOmin);

        // Set the click listeners for each ImageView to navigate to the update page
        imgAPlus.setOnClickListener(view -> navigateToDetail("A", "positive", true, getStokFromTextView(stokAPlus)));
        imgAMinus.setOnClickListener(view -> navigateToDetail("A", "negative", true, getStokFromTextView(stokAMinus)));
        imgBPlus.setOnClickListener(view -> navigateToDetail("B", "positive", true, getStokFromTextView(stokBPlus)));
        imgBMinus.setOnClickListener(view -> navigateToDetail("B", "negative", true, getStokFromTextView(stokBMinus)));
        imgABPlus.setOnClickListener(view -> navigateToDetail("AB", "positive", true, getStokFromTextView(stokABPlus)));
        imgABMinus.setOnClickListener(view -> navigateToDetail("AB", "negative", true, getStokFromTextView(stokABMinus)));
        imgOPlus.setOnClickListener(view -> navigateToDetail("O", "positive", true, getStokFromTextView(stokOPlus)));
        imgOMinus.setOnClickListener(view -> navigateToDetail("O", "negative", true, getStokFromTextView(stokOMinus)));

        // Fetch the blood stock data from the server
        getStokDarah();
    }

    // Method to extract stock number from TextView (removes non-numeric characters)
    private int getStokFromTextView(TextView textView) {
        String text = textView.getText().toString().replaceAll("[^0-9]", "");
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    // Method to navigate to the detail page for updating or viewing stock
    private void navigateToDetail(String golonganDarah, String rhesus, boolean isUpdate, int jumlahStok) {
        Intent intent = new Intent(Page_stokdarah.this, Page_stokdarah2.class);
        intent.putExtra("golongan_darah", golonganDarah);
        intent.putExtra("rhesus", rhesus);
        intent.putExtra("is_update", isUpdate); // Flag to indicate update mode
        intent.putExtra("jumlah_stok", jumlahStok); // Send current stock for update
        startActivityForResult(intent, REQUEST_CODE_UPDATE); // Start activity for result

    }

    // Method to fetch blood stock data from the server
    private void getStokDarah() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        // Get the data object from the response
                        JSONObject data = response.getJSONObject("data");

                        // Set the fetched stock data into the TextViews
                        stokAPlus.setText("Darah terkumpul : " + data.getInt("A_plus"));
                        stokAMinus.setText("Darah terkumpul : " + data.getInt("A_minus"));
                        stokBPlus.setText("Darah terkumpul : " + data.getInt("B_plus"));
                        stokBMinus.setText("Darah terkumpul : " + data.getInt("B_minus"));
                        stokABPlus.setText("Darah terkumpul : " + data.getInt("AB_plus"));
                        stokABMinus.setText("Darah terkumpul : " + data.getInt("AB_minus"));
                        stokOPlus.setText("Darah terkumpul : " + data.getInt("O_plus"));
                        stokOMinus.setText("Darah terkumpul : " + data.getInt("O_minus"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Gagal mengambil data stok: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }





    // Handling the result when returning from the update activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode == RESULT_OK && data != null) {
                // Check if the data update requires a refresh
                boolean refreshNeeded = data.getBooleanExtra("refresh_needed", false);
                if (refreshNeeded) {
                    // Refresh the data from the server
                    getStokDarah(); // Re-fetch the data from the server
                }
            }
        }
    }
}
