package com.example.bloodcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bloodcare.api.ApiClient;
import com.example.bloodcare.api.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Page_edit_akun extends Fragment {

    private EditText editTextEmail, editTextUsername, editTextNamaLengkap, editTextTanggalLahir, editTextNoHp, editTextAlamat;
    private ImageView imgProfile; // Tambahkan ImageView untuk foto profil
    private final String GET_URL = Config.BASE_URL + "akun_detail.php";
    private final String UPDATE_URL = Config.BASE_URL + "edit_akun.php"; // URL untuk update data
    private String oldUsername; // Variabel untuk menyimpan username lama
    private static final int PICK_IMAGE = 1; // Request code untuk memilih gambar
    private String idAkun; // Variabel untuk menyimpan ID akun


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

        // Panggil method untuk mengatur EditText
        configureEditText(editTextNamaLengkap);

        // Inisialisasi ImageView untuk foto profil
        imgProfile = view.findViewById(R.id.imageProfil);
        Bitmap savedImage = ImageUtil.loadImageFromSharedPreferences(getContext());

        if (savedImage != null) {
            // Gunakan Glide untuk menampilkan gambar dalam ImageView
            Glide.with(this)
                    .load(savedImage) // Memuat Bitmap ke Glide
                    .into(imgProfile); // Menampilkan di ImageView
        } else {
            // Gambar default jika tidak ada gambar yang disimpan
            Glide.with(this)
                    .load(R.drawable.ic_profile) // Gambar default
                    .into(imgProfile); // Menampilkan gambar default
        }

        // Tombol kembali
        ImageButton backButton = view.findViewById(R.id.backbutton6);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack()); // Kembali ke fragment sebelumnya

        // Validasi panjang minimal dan maksimal untuk nomor telepon
        editTextNoHp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)}); // Maksimal 13 karakter
        editTextNoHp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 11) {
                    editTextNoHp.setError("Nomor telepon minimal 11 karakter");
                } else if (s.length() > 13) {
                    editTextNoHp.setError("Nomor telepon maksimal 13 karakter");
                }
            }
        });

        // Ambil data dari arguments
        if (getArguments() != null) {
            oldUsername = getArguments().getString("username_or_email", "");
            if (!oldUsername.isEmpty()) {
                loadDataAkun(oldUsername);
            }
        }

        // Listener untuk EditText tanggal lahir
        configureTanggalLahirListener();

        // Listener untuk tombol simpan akun
        Button simpanButton = view.findViewById(R.id.btnSimpanakun);
        simpanButton.setOnClickListener(v -> {
            if (oldUsername == null || oldUsername.isEmpty()) {
                Toast.makeText(getContext(), "Username lama tidak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }
            saveDataAkun(oldUsername);
        });

        // Listener untuk foto profil
        imgProfile.setOnClickListener(v -> openGallery());

        return view;
    }

    // Method untuk mengatur klik dan fokus
    private void configureEditText(EditText editText) {
        // Cek apakah teks kosong
        if (editText.getText().toString().isEmpty()) {
            // Jika kosong, EditText dapat diklik dan difokuskan
            editText.setClickable(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true); // Untuk memastikan dapat disentuh
        } else {
            // Jika tidak kosong, EditText tidak dapat diklik dan difokuskan
            editText.setClickable(false);
            editText.setFocusable(false);
        }

        // Tambahkan listener untuk perubahan teks
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Perbarui properti klik dan fokus saat teks berubah
                configureEditText(editText);
            }
        });
    }

    // Method untuk listener tanggal lahir
    private void configureTanggalLahirListener() {
        editTextTanggalLahir.setOnClickListener(v -> {
            if (editTextTanggalLahir.getText().toString().equals("0000-00-00")) {
                showDatePicker();
            } else {
                Toast.makeText(getContext(), "Tanggal lahir sudah diatur dan tidak bisa diubah", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Fungsi untuk membuka galeri
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    // Mendapatkan hasil gambar yang dipilih dari galeri
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE) {
             imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imgProfile.setImageBitmap(bitmap); // Menampilkan gambar yang dipilih ke ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Fungsi untuk memilih tanggal
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                R.style.DatePickerDialogTheme, // Menggunakan tema kustom
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


                            String idAkun = data.optString("id_akun", ""); // Ambil id_akun
                            this.idAkun = idAkun; // Simpan ke variabel global


                            // Ambil data dari response JSON

                            String email = data.optString("email", "");
                            String username = data.optString("username", "");
                            String namaLengkap = data.optString("nama_lengkap", "");
                            String tanggalLahir = data.optString("tanggal_lahir", "0000-00-00");
                            String noHp = data.optString("no_hp", "");
                            String alamat = data.optString("alamat", "");

                            // Set data ke EditText
                            editTextEmail.setText(email);
                            editTextUsername.setText(username);
                            editTextNamaLengkap.setText(namaLengkap);
                            editTextTanggalLahir.setText(tanggalLahir);
                            editTextNoHp.setText(noHp);
                            editTextAlamat.setText(alamat);

                            // Log id_akun untuk memastikan nilainya
                            Log.d("ID_AKUN", "ID Akun: " + idAkun);


                        } else {
                            Toast.makeText(getContext(), "Gagal memuat data: " + response.optString("message", ""), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", "Kesalahan parsing data: " + e.getMessage());
                        Toast.makeText(getContext(), "Kesalahan parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Gagal menghubungi server: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    Uri imageUri;
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveDataAkun(String oldUsername) {
        String usernameBaru = editTextUsername.getText().toString().trim();
        String noTelepon = editTextNoHp.getText().toString().trim();
        String alamat = editTextAlamat.getText().toString().trim();
        String namaLengkap = editTextNamaLengkap.getText().toString().trim();
        String tanggalLahir = editTextTanggalLahir.getText().toString().trim();

        if (usernameBaru.isEmpty() || noTelepon.isEmpty() || alamat.isEmpty() || namaLengkap.isEmpty() || tanggalLahir.isEmpty()) {
            Toast.makeText(getContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Menyiapkan gambar hanya jika ada
        MultipartBody.Part body = null;
        if (imageUri != null) {
            Bitmap bitmap = getBitmapFromUri(imageUri);
            ImageUtil.saveImageToSharedPreferences(getContext(), bitmap);
            File imageFile = new File(getRealPathFromURI(imageUri)); // Mengonversi Uri ke file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            body = MultipartBody.Part.createFormData("profile_picture", imageFile.getName(), requestFile);

        }

        if (idAkun == null || idAkun.isEmpty()) {
            Toast.makeText(getContext(), "ID akun tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        // Membuat RequestBody untuk data teks lainnya
        RequestBody idAkunReq = RequestBody.create(MediaType.parse("text/plain"), idAkun);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), usernameBaru);
        RequestBody noHp = RequestBody.create(MediaType.parse("text/plain"), noTelepon);
        RequestBody alamatReq = RequestBody.create(MediaType.parse("text/plain"), alamat);
        RequestBody namaLengkapReq = RequestBody.create(MediaType.parse("text/plain"), namaLengkap);
        RequestBody tanggalLahirReq = RequestBody.create(MediaType.parse("text/plain"), tanggalLahir);

        // Menggunakan Retrofit untuk mengirimkan data
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse> call = apiService.updateProfile(
                idAkunReq, username, noHp, alamatReq, namaLengkapReq, tanggalLahirReq, body
        );

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("hasil = " , apiResponse.getMessage());

                    if ("success".equals(apiResponse.getStatus())) {
                        // Menampilkan pesan sukses
                        Toast.makeText(getContext(), "Profil Anda berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    } else {
                        // Menampilkan pesan gagal dengan alasan dari API
                        Toast.makeText(getContext(), "Gagal memperbarui data: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Menampilkan error jika response tidak sukses
                    Toast.makeText(getContext(), "Terjadi kesalahan: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Menampilkan error jika gagal menghubungi server
                Toast.makeText(getContext(), "Gagal menghubungi server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private byte[] getFileBytes(Uri imageUri) {
        File file = new File(getRealPathFromURI(imageUri));

        // Cek apakah file ada
        if (!file.exists()) {
            Log.e("FileError", "File not found: " + file.getAbsolutePath());
            return null;  // Return null jika file tidak ditemukan
        }

        byte[] fileBytes = null;

        try {
            // Membaca file ke dalam byte array
            FileInputStream fileInputStream = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            fileInputStream.read(fileBytes);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileError", "Failed to convert file to bytes: " + e.getMessage());
        }

        return fileBytes;
    }
    // Fungsi untuk mendapatkan path dari Uri gambar
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }
    // Fungsi untuk mengkonversi Uri menjadi byte array
    private byte[] getFileDataFromUri(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor == null) {
            // URI bukan file lokal, coba ambil path lain (untuk URI non-dalam penyimpanan lokal)
            return contentUri.getPath();  // untuk konten yang bukan dari galeri
        }

        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }
}
