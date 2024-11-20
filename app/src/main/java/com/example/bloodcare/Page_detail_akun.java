package com.example.bloodcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Page_detail_akun extends Fragment {

    private EditText editTextEmail, editTextUsername, editTextNamaLengkap, editTextTanggalLahir, editTextNoHp, editTextAlamat;
    private static final String DETAIL_AKUN_URL = "http://192.168.1.40/akun_detail.php"; // Ganti dengan URL API Anda

    public Page_detail_akun() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_akun2, container, false);

        // Inisialisasi EditText
        editTextEmail = view.findViewById(R.id.etEmail);
        editTextUsername = view.findViewById(R.id.etUsername);
        editTextNamaLengkap = view.findViewById(R.id.etNamaLengkap);
        editTextTanggalLahir = view.findViewById(R.id.etTglLahir);
        editTextNoHp = view.findViewById(R.id.etNomorHp);
        editTextAlamat = view.findViewById(R.id.EtAlamat);

        // Memuat data dari server
        loadDataAkun();

        return view;
    }

    private void loadDataAkun() {
        // Buat request JSON untuk mendapatkan detail akun
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, DETAIL_AKUN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Periksa apakah response berhasil
                            if (response.getBoolean("success")) {
                                JSONObject data = response.getJSONObject("data");

                                // Set data ke EditText
                                editTextEmail.setText(data.getString("email"));
                                editTextUsername.setText(data.getString("username"));
                                editTextNamaLengkap.setText(data.getString("nama_lengkap"));
                                editTextTanggalLahir.setText(data.getString("tanggal_lahir"));
                                editTextNoHp.setText(data.getString("no_handphone"));
                                editTextAlamat.setText(data.getString("alamat"));
                            } else {
                                Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Kesalahan parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Gagal menghubungi server: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }
}