package com.example.bloodcare;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Page_detail_akun extends Fragment {

    private String GET_URL = Config.BASE_URL + "akun_detail.php";
    private EditText editTextEmail, editTextUsername, editTextNamaLengkap, editTextTanggalLahir, editTextNoHp, editTextAlamat;

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

        // Ambil data dari intent yang dikirimkan oleh Page_akun
        if (getActivity() != null) {
            String usernameOrEmail = getActivity().getIntent().getStringExtra("username_or_email");

            if (usernameOrEmail != null && !usernameOrEmail.isEmpty()) {
                // Menampilkan data pada EditText jika username_or_email tersedia
                loadDataAkun(usernameOrEmail);
            } else {
                Toast.makeText(getContext(), "Tidak ada data username atau email yang diterima", Toast.LENGTH_SHORT).show();
            }
        }

        // Tambahkan listener untuk button editpp yang akan berpindah ke fragment Page_editakun
        Button editButton = view.findViewById(R.id.btneditpp);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil data dari EditText atau Intent (misalnya username_or_email)
                String usernameOrEmail = editTextUsername.getText().toString().trim();

                // Kirim data ke Page_edit_akun menggunakan Bundle
                Bundle bundle = new Bundle();
                bundle.putString("username_or_email", usernameOrEmail); // Kirimkan username_or_email

                // Ganti fragment ke Page_editakun
                Page_edit_akun pageEditAkun = new Page_edit_akun();
                pageEditAkun.setArguments(bundle);  // Set arguments ke Page_edit_akun

                // Lakukan transaction fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, pageEditAkun);
                transaction.addToBackStack(null); // Menambahkan ke back stack agar bisa kembali
                transaction.commit();
            }
        });

        return view;
    }

    private void loadDataAkun(String usernameOrEmail) {
        String urlWithParams = GET_URL + "?username_or_email=" + usernameOrEmail;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlWithParams, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");

                            editTextEmail.setText(data.optString("email", ""));
                            editTextUsername.setText(data.optString("username", ""));
                            editTextNamaLengkap.setText(data.optString("nama_lengkap", ""));

                            // Ambil dan format tanggal lahir
                            String tanggalLahir = data.optString("tanggal_lahir", "");
                            Log.d("TanggalLahirAPI", "Tanggal lahir dari API: " + tanggalLahir);

                            if (!tanggalLahir.isEmpty()) {
                                String formattedTanggalLahir = formatTanggal(tanggalLahir);
                                editTextTanggalLahir.setText(formattedTanggalLahir);
                            } else {
                                editTextTanggalLahir.setText("");
                            }

                            editTextNoHp.setText(data.optString("no_hp", ""));
                            editTextAlamat.setText(data.optString("alamat", ""));
                        } else {
                            Toast.makeText(getContext(), "Gagal memuat data: " + response.optString("message", ""), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Kesalahan parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Gagal menghubungi server: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    private String formatTanggal(String tanggal) {

        // Periksa jika tanggal adalah 0000-00-00
        if ("0000-00-00".equals(tanggal)) {
            return ""; // Tampilkan teks kosong atau alternatif seperti "Tanggal tidak tersedia"
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedTanggal = "";

        try {
            if (!tanggal.isEmpty()) {
                Date date = inputFormat.parse(tanggal);
                formattedTanggal = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Format tanggal tidak valid", Toast.LENGTH_SHORT).show();
        }

        return formattedTanggal;
    }
}
