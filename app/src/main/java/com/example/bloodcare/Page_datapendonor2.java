package com.example.bloodcare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Page_datapendonor2 extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> donorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_datapendonor2);

        listView = findViewById(R.id.listView);
        donorList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, donorList);
        listView.setAdapter(adapter);

        fetchDonorData();
    }

    private void fetchDonorData() {
        String url = Config.BASE_URL + "get_pendonor.php"; // Ganti dengan URL server PHP Anda

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                JSONArray dataArray = response.getJSONArray("data");
                                donorList.clear();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);
                                    String namaPendonor = dataObj.getString("nama_pendonor");
                                    String goldar = dataObj.getString("goldar");
                                    String rhesus = dataObj.getString("rhesus");

                                    // Format data untuk ditampilkan
                                    String formattedData = "Nama: " + namaPendonor + "\nGolongan Darah: " + goldar + " " + rhesus;
                                    donorList.add(formattedData);
                                }

                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(Page_datapendonor2.this, "Data not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String errorMessage = error.getMessage();
                Toast.makeText(Page_datapendonor2.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jsonObjectRequest);
    }
}
