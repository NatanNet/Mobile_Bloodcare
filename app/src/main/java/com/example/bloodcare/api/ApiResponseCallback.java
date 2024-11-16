package com.example.bloodcare.api;

public interface ApiResponseCallback {
    /**
     * Dipanggil saat respons API berhasil diterima.
     *
     * @param response String respons dari server.
     */
    void onSuccess(String response);

    /**
     * Dipanggil saat terjadi kesalahan dalam permintaan API.
     *
     * @param error String pesan kesalahan.
     */
    void onError(String error);
}
