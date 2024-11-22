package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Page_stokdarah extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_stokdarah);

        // Inisialisasi ImageView edit
        ImageView edit = findViewById(R.id.iconEditAplus);

        // Tambahkan OnClickListener pada ImageView edit
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke Page_acaradonor2
                Intent intent = new Intent(Page_stokdarah.this, Page_stokdarah2.class);
                startActivity(intent);
            }
        });
    }
}
