package com.example.bloodcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Page_laporan extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_laporan);

        ImageView iconLaporan = findViewById(R.id.iconLaporan);

        iconLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(Page_laporan.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.item_senin) {
                            Toast.makeText(Page_laporan.this, "Senin dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_selasa) {
                            Toast.makeText(Page_laporan.this, "Selasa dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_rabu) {
                            Toast.makeText(Page_laporan.this, "Rabu dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_kamis) {
                            Toast.makeText(Page_laporan.this, "Kamis dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_jumat) {
                            Toast.makeText(Page_laporan.this, "Jumat dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_minggu_ini) {
                            Toast.makeText(Page_laporan.this, "Minggu Ini dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_minggu_lalu) {
                            Toast.makeText(Page_laporan.this, "Minggu Lalu dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_bulan_ini) {
                            Toast.makeText(Page_laporan.this, "Bulan Ini dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_bulan_lalu) {
                            Toast.makeText(Page_laporan.this, "Bulan Lalu dipilih", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.item_semua) {
                            Toast.makeText(Page_laporan.this, "Semua dipilih", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });


        //Iki digae memasukkan id setiap text view nama
        // Tambahkan setelah iconLaporan.setOnClickListener
        //iki id setiap lihat
        TextView tvLihatNatan = findViewById(R.id.tvLihatNatan);
        TextView LihatAmar = findViewById(R.id.LihatAmar);
        TextView LihatGita = findViewById(R.id.LihatGita);
        TextView LihatApril = findViewById(R.id.LihatApril);
        TextView LihatSasa = findViewById(R.id.LihatSasa);
        TextView LihatAzkai = findViewById(R.id.LihatAzkai);


        //kode iki gae ben nama dan lihat iso diklik boy
        tvLihatNatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_laporan.this, Page_laporan2.class);
                startActivity(intent);
            }
        });

        // Atur OnClickListener untuk register TextView
        LihatAmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_laporan.this, Page_laporan2.class);
                startActivity(intent);
            }
        });

        LihatGita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_laporan.this, Page_laporan2.class);
                startActivity(intent);
            }
        });

        LihatApril.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_laporan.this, Page_laporan2.class);
                startActivity(intent);
            }
        });

        LihatSasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_laporan.this, Page_laporan2.class);
                startActivity(intent);
            }
        });

        LihatAzkai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_laporan.this, Page_laporan2.class);
                startActivity(intent);
            }
        });
    }
}
/*

        LihatAmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Page_laporan.this, "Lihat Amar dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        LihatGita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Page_laporan.this, "Lihat Gita dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        LihatApril.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Page_laporan.this, "Lihat April dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        LihatSasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Page_laporan.this, "lihat Sasa dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        LihatAzkai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Page_laporan.this, "Lihat Azkai dipilih", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/
