package com.example.bloodcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Page_acara extends Fragment {

    private ListView listView;
    private TextView detailTextView;

    private ArrayList<String> lokasiList = new ArrayList<>();
    private ArrayList<String> fasilitasList = new ArrayList<>();
    private ArrayList<String> waktuList = new ArrayList<>();
    private ArrayList<String> tglList = new ArrayList<>();

    public Page_acara() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Membuat container utama untuk layout
        LinearLayout parentLayout = new LinearLayout(requireContext());
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        // Tambahkan layout pertama
        View layoutPertama = inflater.inflate(R.layout.activity_dashboard2, parentLayout, false);
        listView = layoutPertama.findViewById(R.id.listView); // Pastikan ada ListView dengan ID listView
        parentLayout.addView(layoutPertama);

        // Tambahkan layout kedua
        View layoutKedua = inflater.inflate(R.layout.activity_dashboard_2, parentLayout, false);
        detailTextView = layoutKedua.findViewById(R.id.textNama); // Pastikan ada TextView dengan ID detailTextView
        parentLayout.addView(layoutKedua);

        // Ambil data dari server
        fetchDataFromServer();

        return parentLayout;
    }

    private void fetchDataFromServer() {
        // URL file PHP (ganti dengan URL server Anda)
        String url = "https://192.168.1.125/get_dataAcara.php";

        // Buat permintaan JSON Array
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Parsing data dari response JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Ambil data
                                String lokasi = jsonObject.getString("lokasi");
                                String fasilitas = jsonObject.getString("fasilitas");
                                String time_waktu = jsonObject.getString("time_waktu");
                                String tgl_acara = jsonObject.getString("tgl_acara");

                                // Tambahkan ke ArrayList
                                lokasiList.add(lokasi);
                                fasilitasList.add(fasilitas);
                                waktuList.add(time_waktu);
                                tglList.add(tgl_acara);
                            }

                            // Update ListView dengan data
                            updateListView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error parsing data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tampilkan pesan error
                        Toast.makeText(requireContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Tambahkan request ke antrian Volley
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void updateListView() {
        // Buat daftar item untuk ditampilkan
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < lokasiList.size(); i++) {
            String item = "Lokasi: " + lokasiList.get(i) + "\n" +
                    "Fasilitas: " + fasilitasList.get(i) + "\n" +
                    "Waktu: " + waktuList.get(i) + "\n" +
                    "Tanggal: " + tglList.get(i);
            items.add(item);
        }

        // Set data ke ListView menggunakan ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // Atur listener untuk klik item
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = items.get(position);
            detailTextView.setText(selectedItem);
        });
    }
}
