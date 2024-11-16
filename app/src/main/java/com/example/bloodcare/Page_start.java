package com.example.bloodcare;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Page_start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        // Ambil referensi ke TextView
        TextView textView = findViewById(R.id.text);
        TextView poweredTextView = findViewById(R.id.powered);

        // Load custom font dari folder assets
        Typeface customFontBold = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Bold.ttf");
        Typeface customFontLight = Typeface.createFromAsset(getAssets(), "fonts/Poppins-Light.ttf");

        // Terapkan font ke TextView
        textView.setTypeface(customFontBold);
        poweredTextView.setTypeface(customFontLight);

        // Handler untuk menunda intent ke MainActivity2
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent untuk berpindah dari MainActivity ke MainActivity2
                Intent intent = new Intent(Page_start.this, Page_login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();  // Mengakhiri MainActivity sehingga tidak kembali ke splash screen
            }
        }, 2000);  // Mengubah waktu delay menjadi 2000ms (2 detik)
    }
}
