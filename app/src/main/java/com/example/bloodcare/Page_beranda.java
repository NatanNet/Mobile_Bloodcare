package com.example.bloodcare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Page_beranda extends Fragment {

    private RequestQueue requestQueue;
    private TextView halo;
    private Handler handler;
    private Runnable clockRunnable;
    private TextView clockTextView;

    public Page_beranda() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout untuk fragment
        View view = inflater.inflate(R.layout.activity_dashboard1, container, false);

        requestQueue = Volley.newRequestQueue(requireContext());
        halo = view.findViewById(R.id.idhalo);
        clockTextView = view.findViewById(R.id.textViewClock);

        // Menjalankan fungsi untuk menampilkan jam
        startClock();

        // Mengambil username atau email dari arguments
        Bundle bundle = getArguments();
        String usernameOrEmail = bundle != null ? bundle.getString("username_or_email") : null;
        Bitmap savedImage = ImageUtil.loadImageFromSharedPreferences(getContext());
        ImageView imgProfile = view.findViewById(R.id.profil);
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
        }        if (usernameOrEmail != null) {
            fetchUserFullName(usernameOrEmail);
        } else {
            halo.setText("Halo,\nGuest");
        }

        // Menampilkan tanggal saat ini
        TextView date = view.findViewById(R.id.dateTextView);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy");
        date.setText(sdf.format(calendar.getTime()));

        // Mengatur klik listener untuk navigasi
        setupListeners(view);

        return view;
    }

    private void startClock() {
        handler = new Handler();
        clockRunnable = new Runnable() {
            @Override
            public void run() {
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                clockTextView.setText(currentTime);
                handler.postDelayed(this, 1000); // Perbarui setiap 1 detik
            }
        };
        handler.post(clockRunnable); // Jalankan pertama kali
    }




    private void fetchUserFullName(String usernameOrEmail) {
        String url = Config.BASE_URL + "get_user_fullname.php?username_or_email=" + usernameOrEmail;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.has("username")) {
                            String username = response.getString("username");
                            // Set text with newline to separate "Halo," and the username
                            halo.setText("Halo,\n" + username + "!");
                        } else {
                            halo.setText("Halo,\nUser");
                            Toast.makeText(getContext(), response.getString("error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        halo.setText("Halo,\nUser");
                        Toast.makeText(getContext(), "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            halo.setText("Halo,\nUser");
            Log.e("Page_beranda", "Error: " + error.getMessage());
            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(request);
    }



    private void setupListeners(View view) {
        LinearLayout layoutKlik = view.findViewById(R.id.acaraklik);
        LinearLayout dataKlik = view.findViewById(R.id.datadonorklik);
        LinearLayout stokKlik = view.findViewById(R.id.stokklik);
        LinearLayout laporanKlik = view.findViewById(R.id.laporanklik);
        ImageView imageViewakun = view.findViewById(R.id.profil);

        layoutKlik.setOnClickListener(v -> startActivity(new Intent(getActivity(), Page_acaradonor.class)));
        dataKlik.setOnClickListener(v -> startActivity(new Intent(getActivity(), Page_datapendonor2.class)));
        stokKlik.setOnClickListener(v -> startActivity(new Intent(getActivity(), Page_stokdarah.class)));
        laporanKlik.setOnClickListener(v -> startActivity(new Intent(getActivity(), Page_laporan.class)));

        imageViewakun.setOnClickListener(v -> {
            Fragment secondFragment = new Page_akun();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
        // Hentikan handler untuk mencegah kebocoran memori
        if (handler != null && clockRunnable != null) {
            handler.removeCallbacks(clockRunnable);
        }
    }
}
