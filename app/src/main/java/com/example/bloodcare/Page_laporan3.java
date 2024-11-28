package com.example.bloodcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Page_laporan3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_laporan3);

        // TextView untuk membuka Page_laporan2
        TextView textnama = findViewById(R.id.textlihat);


        textnama.setOnClickListener(v -> {
            Intent intent = new Intent(Page_laporan3.this, Page_laporan2.class);
            startActivity(intent);
        });
    }
}