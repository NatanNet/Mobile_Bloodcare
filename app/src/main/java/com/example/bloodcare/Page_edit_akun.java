package com.example.bloodcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Page_edit_akun extends Fragment {

    private EditText editTextEmail, editTextUsername, editTextNamaLengkap, editTextTanggalLahir, editTextNoHp, editTextAlamat;
    private final String GET_URL = Config.BASE_URL + "akun_detail.php";
    private final String UPDATE_URL = Config.BASE_URL + "edit_akun.php"; // URL untuk update data
    private String oldUsername; // Variabel untuk menyimpan username lama

    public Page_edit_akun() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_edit_akun, container, false);

        // Inisialisasi EditText
        editTextEmail = view.findViewById(R.id.etEmail);
        editTextUsername = view.findViewById(R.id.etUsername);
        editTextNamaLengkap = view.findViewById(R.id.etNamaLengkap);
        editTextTanggalLahir = view.findViewById(R.id.etTglLahir);
        editTextNoHp = view.findViewById(R.id.etNomorHp);
        editTextAlamat = view.findViewById(R.id.EtAlamat);

        // Ambil data dari arguments
        if (getArguments() != null) {
            oldUsername = getArguments().getString("username_or_email", "");
            if (!oldUsername.isEmpty()) {
                loadDataAkun(oldUsername);
            }
        }

        // Listener untuk EditText tanggal lahir
        editTextTanggalLahir.setOnClickListener(v -> {
            if (editTextTanggalLahir.getText().toString().equals("0000-00-00")) {
                showDatePicker();
            } else {
                Toast.makeText(getContext(), "Tanggal lahir sudah diatur dan tidak bisa diubah", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener untuk tombol simpan akun
        Button simpanButton = view.findViewById(R.id.btnSimpanakun);
        simpanButton.setOnClickListener(v -> {
            if (oldUsername == null || oldUsername.isEmpty()) {
                Toast.makeText(getContext(), "Username lama tidak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }
            saveDataAkun(oldUsername);
        });

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    editTextTanggalLahir.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void loadDataAkun(String usernameOrEmail) {
        String urlWithParams = GET_URL + "?username_or_email=" + usernameOrEmail;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlWithParams, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");

                            // Ambil data dari response JSON dan set ke EditText
                            editTextEmail.setText(data.optString("email", ""));
                            editTextUsername.setText(data.optString("username", ""));
                            editTextNamaLengkap.setText(data.optString("nama_lengkap", ""));
                            editTextTanggalLahir.setText(data.optString("tanggal_lahir", "0000-00-00"));
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

    private void saveDataAkun(String oldUsername) {
        String usernameBaru = editTextUsername.getText().toString().trim();
        String noTelepon = editTextNoHp.getText().toString().trim();
        String alamat = editTextAlamat.getText().toString().trim();
        String namaLengkap = editTextNamaLengkap.getText().toString().trim();
        String tanggalLahir = editTextTanggalLahir.getText().toString().trim();

        if (usernameBaru.isEmpty() || noTelepon.isEmpty() || alamat.isEmpty() || namaLengkap.isEmpty() || tanggalLahir.isEmpty()) {
            Toast.makeText(getContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getBoolean("success")) {
                            Toast.makeText(getContext(), "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();

                            // Kirim intent ke Page_dashboard activity
                            Intent intent = new Intent(getContext(), Page_dashboard.class);
                            intent.putExtra("username_or_email", usernameBaru); // Mengirimkan username baru

                            // Start the activity
                            startActivity(intent);

                            // Optionally, you can finish the current fragment/fragment activity if you want to close it
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Gagal memperbarui data: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Kesalahan parsing respons: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Gagal menghubungi server: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", oldUsername);
                params.put("username_baru", usernameBaru);
                params.put("no_telepon", noTelepon);
                params.put("alamat", alamat);
                params.put("nama_lengkap", namaLengkap);
                params.put("tanggal_lahir", tanggalLahir);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
