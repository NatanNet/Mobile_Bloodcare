package com.example.bloodcare;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class GalleryHelper {

    public static final int PICK_IMAGE = 1; // Request code untuk memilih gambar

    // Method untuk membuka galeri dan memilih gambar
    public static void openGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PICK_IMAGE);
    }

    // Mendapatkan hasil gambar dari galeri, bisa dipanggil dalam onActivityResult
    public static Uri handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            return data.getData();
        }
        return null;
    }
}
