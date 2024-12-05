package com.example.bloodcare.api;

import com.example.bloodcare.ApiResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart

    @POST("update_profile_mobile.php")

    Call<ApiResponse> updateProfile(

            @Part("id_akun") RequestBody idAkun,

            @Part("username") RequestBody username, // Sesuai dengan kolom 'username'
            @Part("no_hp") RequestBody noHp, // Sesuai dengan kolom 'no_hp'
            @Part("alamat") RequestBody alamat, // Sesuai dengan kolom 'alamat'

            @Part("nama_lengkap") RequestBody namaLengkap, // Sesuai dengan kolom 'nama_lengkap'



            @Part("tanggal_lahir") RequestBody tanggalLahir, // Sesuai dengan kolom 'tanggal_lahir'

            @Part MultipartBody.Part profilePicture // Gambar, sesuai dengan kolom 'profile_picture'

    );

}