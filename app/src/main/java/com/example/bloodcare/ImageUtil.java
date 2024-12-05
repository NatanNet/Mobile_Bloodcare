package com.example.bloodcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    private static final String PREFS_NAME = "image_prefs";
    private static final String IMAGE_KEY = "user_profile_image";

    // Menyimpan gambar ke SharedPreferences
    public static void saveImageToSharedPreferences(Context context, Bitmap bitmap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Mengonversi gambar Bitmap ke Base64
        String base64Image = encodeToBase64(bitmap);

        // Menyimpan gambar dalam format Base64 ke SharedPreferences
        editor.putString(IMAGE_KEY, base64Image);
        editor.apply();
    }

    // Mengonversi Bitmap ke Base64
    private static String encodeToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Memuat gambar dari SharedPreferences
    public static Bitmap loadImageFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Mengambil string Base64 dari SharedPreferences
        String base64Image = sharedPreferences.getString(IMAGE_KEY, null);

        if (base64Image != null) {
            // Mengonversi string Base64 menjadi Bitmap
            return decodeFromBase64(base64Image);
        }

        return null; // Jika gambar tidak ada
    }

    // Mengonversi Base64 menjadi Bitmap
    private static Bitmap decodeFromBase64(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
