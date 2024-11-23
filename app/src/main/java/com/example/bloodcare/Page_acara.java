package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Page_acara extends Fragment {

    private ListView listView;
    private ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
    private String GET_URL = "http://192.168.1.94/website_bloodcare/api/mobile/get_dataAcara.php";
    private String DELETE_URL = "http://192.168.1.94/website_bloodcare/api/mobile/delete_donor.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard2, container, false);

        listView = view.findViewById(R.id.listView);
        fetchData();

        return view;
    }

    private void fetchData() {
        StringRequest request = new StringRequest(Request.Method.GET, GET_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            dataList.clear();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id_acara", obj.getString("id_acara"));
                                map.put("lokasi", obj.getString("lokasi"));
                                map.put("fasilitas", obj.getString("fasilitas"));
                                map.put("time_waktu", obj.getString("time_waktu"));
                                map.put("tgl_acara", obj.getString("tgl_acara"));
                                dataList.add(map);
                            }

                            CustomAdapter adapter = new CustomAdapter();
                            listView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_dashboard2_, parent, false);
            }

            TextView textNama = convertView.findViewById(R.id.textNama);
            ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
            ImageButton btnDelete = convertView.findViewById(R.id.btnDelete); // Tombol hapus ditambahkan

            HashMap<String, String> item = dataList.get(position);
            textNama.setText(item.get("lokasi"));

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), Page_acaradonor.class);
                intent.putExtra("id_acara", item.get("id_acara"));
                intent.putExtra("lokasi", item.get("lokasi"));
                intent.putExtra("fasilitas", item.get("fasilitas"));
                intent.putExtra("time_waktu", item.get("time_waktu"));
                intent.putExtra("tgl_acara", item.get("tgl_acara"));
                startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> {
                // Memanggil fungsi deleteData() dengan id acara
                deleteData(item.get("id_acara"));
            });

            return convertView;
        }
    }

    private void deleteData(String id) {
        StringRequest request = new StringRequest(Request.Method.POST, DELETE_URL,
                response -> {
                    Toast.makeText(getContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    fetchData(); // Refresh data setelah penghapusan
                },
                error -> Toast.makeText(getContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id); // Mengirimkan parameter "id" ke API
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
