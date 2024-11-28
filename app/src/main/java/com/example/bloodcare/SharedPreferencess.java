package com.example.bloodcare;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencess {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "BloodCarePreferences";
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String USER_ID = "user_id";
    private static final String USERNAME_OR_EMAIL = "username_or_email";

    public SharedPreferencess(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Simpan status login
    public void saveLoginStatus(boolean status) {
        editor.putBoolean(IS_LOGGED_IN, status);
        editor.apply();
    }

    // Ambil status login
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    // Simpan user ID
    public void saveUserId(int userId) {
        editor.putInt(USER_ID, userId);
        editor.apply();
    }

    // Ambil user ID
    public int getUserId() {
        return sharedPreferences.getInt(USER_ID, -1); // -1 adalah nilai default jika tidak ditemukan
    }

    // Simpan username atau email
    public void saveUsernameOrEmail(String usernameOrEmail) {
        editor.putString(USERNAME_OR_EMAIL, usernameOrEmail);
        editor.apply();
    }

    // Ambil username atau email
    public String getUsernameOrEmail() {
        return sharedPreferences.getString(USERNAME_OR_EMAIL, null); // null jika tidak ditemukan
    }

    // Hapus data login (logout)
    public void logout() {
        editor.clear(); // Menghapus semua data yang ada di SharedPreferences
        editor.apply();
    }

    // Cek apakah sudah login atau belum
    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}
