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

    private TextView stokAPlus, stokAMinus, stokBPlus, stokBMinus, stokABPlus, stokABMinus, stokOPlus, stokOMinus;

    private static final String URL = Config.BASE_URL + "get_stokdarah.php"; // URL API Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_stokdarah);
        ImageButton buttonBack = findViewById(R.id.icback2);

        buttonBack.setOnClickListener(v -> finish());

        // Inisialisasi TextView
        stokAPlus = findViewById(R.id.DarahTerkumpulAplus);
        stokAMinus = findViewById(R.id.DarahTerkumpulAmin);
        stokBPlus = findViewById(R.id.DarahTerkumpulBplus);
        stokBMinus = findViewById(R.id.DarahTerkumpulBmin);
        stokABPlus = findViewById(R.id.DarahTerkumpulABplus);
        stokABMinus = findViewById(R.id.DarahTerkumpulABmin);
        stokOPlus = findViewById(R.id.DarahTerkumpulOplus);
        stokOMinus = findViewById(R.id.DarahTerkumpulOmin);

        //Inisialisasi ImageView
        ImageView imgAPluss = findViewById(R.id.iconEditAplus);
        ImageView imgAMinuss = findViewById(R.id.iconEditAmin);
        ImageView imgABPluss = findViewById(R.id.iconEditABplus);
        ImageView imgABMinuss = findViewById(R.id.iconEditABmin);
        ImageView imgBMinuss = findViewById(R.id.iconEditBmin);
        ImageView imgBPluss = findViewById(R.id.iconEditBplus);
        ImageView imgOMinuss = findViewById(R.id.iconEditOmin);
        ImageView imgOPluss = findViewById(R.id.iconEditOplus);

        ImageView imgAPlus = findViewById(R.id.iconGolonganAplus);
        ImageView imgAMinus = findViewById(R.id.iconGolonganAmin);
        ImageView imgBPlus = findViewById(R.id.iconGolonganBplus);
        ImageView imgBMinus = findViewById(R.id.iconGolonganBmin);
        ImageView imgABPlus = findViewById(R.id.iconGolonganABplus);
        ImageView imgABMinus = findViewById(R.id.iconGolonganABmin);
        ImageView imgOPlus = findViewById(R.id.iconGolonganOplus);
        ImageView imgOMinus = findViewById(R.id.iconGolonganOmin);

        // Set onClickListener untuk setiap ImageView untuk input baru
        imgAPlus.setOnClickListener(view -> navigateToDetail("A", "positive", false, 0));
        imgAMinus.setOnClickListener(view -> navigateToDetail("A", "negative", false, 0));
        imgBPlus.setOnClickListener(view -> navigateToDetail("B", "positive", false, 0));
        imgBMinus.setOnClickListener(view -> navigateToDetail("B", "negative", false, 0));
        imgABPlus.setOnClickListener(view -> navigateToDetail("AB", "positive", false, 0));
        imgABMinus.setOnClickListener(view -> navigateToDetail("AB", "negative", false, 0));
        imgOPlus.setOnClickListener(view -> navigateToDetail("O", "positive", false, 0));
        imgOMinus.setOnClickListener(view -> navigateToDetail("O", "negative", false, 0));

        // Set onClickListener untuk setiap ImageView jika ingin memperbarui stok
        imgAPluss.setOnClickListener(view -> navigateToDetail("A", "positive", true, getStokFromTextView(stokAPlus)));
        imgAMinuss.setOnClickListener(view -> navigateToDetail("A", "negative", true, getStokFromTextView(stokAMinus)));
        imgBPluss.setOnClickListener(view -> navigateToDetail("B", "positive", true, getStokFromTextView(stokBPlus)));
        imgBMinuss.setOnClickListener(view -> navigateToDetail("B", "negative", true, getStokFromTextView(stokBMinus)));
        imgABPluss.setOnClickListener(view -> navigateToDetail("AB", "positive", true, getStokFromTextView(stokABPlus)));
        imgABMinuss.setOnClickListener(view -> navigateToDetail("AB", "negative", true, getStokFromTextView(stokABMinus)));
        imgOPluss.setOnClickListener(view -> navigateToDetail("O", "positive", true, getStokFromTextView(stokOPlus)));
        imgOMinuss.setOnClickListener(view -> navigateToDetail("O", "negative", true, getStokFromTextView(stokOMinus)));

        // Ambil data stok dari server
        getStokDarah();
    }

    // Fungsi untuk mengambil nilai stok darah dari TextView
    private int getStokFromTextView(TextView textView) {
        String text = textView.getText().toString().replaceAll("[^0-9]", "");
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    // Fungsi untuk menavigasi ke halaman detail
    private void navigateToDetail(String golonganDarah, String rhesus, boolean isUpdate, int jumlahStok) {
        // Navigasi ke halaman detail
        Intent intent = new Intent(Page_stokdarah.this, Page_stokdarah2.class);
        intent.putExtra("golongan_darah", golonganDarah);
        intent.putExtra("rhesus", rhesus);
        intent.putExtra("is_update", isUpdate); // Flag untuk menentukan mode
        intent.putExtra("jumlah_stok", jumlahStok); // Kirimkan jumlah stok untuk update
        startActivity(intent);
    }

    // Mengambil data stok dari server
    private void getStokDarah() {
        // Buat Request ke server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        // Ambil objek "data" dari respons JSON
                        JSONObject data = response.getJSONObject("data");

                        // Ambil nilai dari masing-masing kunci dan set ke TextView
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

        // Tambahkan request ke RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
