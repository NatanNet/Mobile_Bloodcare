package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.net.URLEncoder;

public class Page_datapendonor2 extends AppCompatActivity {

    private ListView listView;
    private DonorAdapter adapter;
    private ArrayList<HashMap<String, String>> donorList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_datapendonor2);

        listView = findViewById(R.id.listView);
        donorList = new ArrayList<>();
        adapter = new DonorAdapter(this, donorList);
        listView.setAdapter(adapter);

        // Button to go back to the previous page
        ImageButton buttonBack = findViewById(R.id.imageButton4);
        buttonBack.setOnClickListener(v -> finish());

        // Initialize SearchView
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search donor name");

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the data of the selected donor
            HashMap<String, String> selectedDonor = donorList.get(position);

            // Intent to open Page_datapendonor
            Intent intent = new Intent(Page_datapendonor2.this, Page_datapendonor.class);
            intent.putExtra("nama", selectedDonor.get("nama"));
            intent.putExtra("nohp", selectedDonor.get("nohp"));
            intent.putExtra("lokasi_donor", selectedDonor.get("lokasi_donor"));
            intent.putExtra("goldar", selectedDonor.get("goldar"));
            intent.putExtra("tb", selectedDonor.get("tb"));
            intent.putExtra("tekanan", selectedDonor.get("tekanan_darah"));
            intent.putExtra("bb", selectedDonor.get("bb"));
            intent.putExtra("alamat", selectedDonor.get("alamat"));
            intent.putExtra("rhesus", selectedDonor.get("rhesus"));
            intent.putExtra("id_pendonor", selectedDonor.get("id_pendonor"));
            intent.putExtra("id_akun", selectedDonor.get("id_akun"));

            startActivity(intent);
        });

        // Search event listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDonorData(query); // Call the search function
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // If the input is empty, show all data
                if (newText.isEmpty()) {
                    fetchAllDonorData(); // Show all data if the query is empty
                }
                return true;
            }
        });

        // Fetch all donor data when the page is first loaded
        fetchAllDonorData();
    }

    private void fetchAllDonorData() {
        String url = Config.BASE_URL + "get_pendonor.php";  // Endpoint untuk mengambil semua data pendonor

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            donorList.clear();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObj = dataArray.getJSONObject(i);

                                HashMap<String, String> donorData = new HashMap<>();
                                donorData.put("nama", dataObj.getString("nama_pendonor"));
                                donorData.put("nohp", dataObj.optString("no_telp"));
                                donorData.put("lokasi_donor", dataObj.getString("lokasi_donor"));
                                donorData.put("status", dataObj.getString("status"));
                                donorData.put("tekanan_darah", dataObj.optString("tekanan_darah"));
                                donorData.put("goldar", dataObj.optString("goldar"));
                                donorData.put("bb", dataObj.optString("berat_badan"));
                                donorData.put("alamat", dataObj.optString("alamat"));
                                donorData.put("rhesus", dataObj.optString("rhesus"));
                                donorData.put("id_pendonor", dataObj.optString("id_pendonor"));
                                donorData.put("id_akun", dataObj.optString("id_akun"));

                                // Tambahkan data di awal list
                                donorList.add(0, donorData);
                            }

                            adapter.notifyDataSetChanged(); // Perbarui ListView
                        } else {
                            Toast.makeText(Page_datapendonor2.this, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Page_datapendonor2.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(Page_datapendonor2.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        queue.add(jsonObjectRequest);
    }


    private void searchDonorData(String query) {
        String queryFormatted = query.replace(" ", "+");  // Format query
        String url = Config.BASE_URL + "get_pendonor.php?nama_pendonor=" + queryFormatted;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("Response", response.toString()); // Debug response

                        String status = response.optString("status", "unknown");
                        if (status.equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            donorList.clear();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObj = dataArray.getJSONObject(i);

                                HashMap<String, String> donorData = new HashMap<>();
                                donorData.put("nama", dataObj.getString("nama_pendonor"));
                                donorData.put("nohp", dataObj.optString("no_telp"));
                                donorData.put("lokasi_donor", dataObj.getString("lokasi_donor"));
                                donorData.put("status", dataObj.getString("status"));
                                donorData.put("tekanan_darah", dataObj.optString("tekanan_darah"));
                                donorData.put("goldar", dataObj.optString("goldar"));
                                donorData.put("bb", dataObj.optString("berat_badan"));
                                donorData.put("alamat", dataObj.optString("alamat"));
                                donorData.put("rhesus", dataObj.optString("rhesus"));
                                donorData.put("id_pendonor", dataObj.optString("id_pendonor"));
                                donorData.put("id_akun", dataObj.optString("id_akun"));

                                // Tambahkan data di awal list
                                donorList.add(0, donorData);
                            }

                            adapter.notifyDataSetChanged(); // Perbarui ListView
                        } else {
                            Toast.makeText(Page_datapendonor2.this, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Page_datapendonor2.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(Page_datapendonor2.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        queue.add(jsonObjectRequest);
    }


    // Override onResume to reload the data when the user returns
    @Override
    protected void onResume() {
        super.onResume();

        // Call the function to reload donor data when the activity resumes
        fetchAllDonorData();
    }
}