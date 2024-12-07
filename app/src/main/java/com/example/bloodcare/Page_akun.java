package com.example.bloodcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class Page_akun extends Fragment {

    private SharedPreferencess sharedPreferences;
    private String usernameOrEmail;
    private TextView textViewFullName, textViewEmail, textViewPhone;

    public Page_akun() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard3, container, false);

        sharedPreferences = new SharedPreferencess(getActivity());

        if (getArguments() != null) {
            usernameOrEmail = getArguments().getString("usernameOrEmail");
            Log.d("INPUT_VALUE", "UsernameOrEmail: " + usernameOrEmail);
        }

        // Inisialisasi TextView
        textViewFullName = view.findViewById(R.id.profilname); // Pastikan Anda punya TextView dengan ID ini
        textViewEmail = view.findViewById(R.id.profilemail);
        textViewPhone = view.findViewById(R.id.tvProfilePhone);

        // Ambil data user berdasarkan username atau email
        fetchUserData(usernameOrEmail);

        // Inisialisasi tombol
        Button buttonNavigate = view.findViewById(R.id.btneditpp);
        ImageView buttonBack = view.findViewById(R.id.keluar);
        ImageView imageViewGoToOTP = view.findViewById(R.id.gantipss);
        ImageView imageViewGoogle = view.findViewById(R.id.tentangkami1);
        ImageView imageViewPusatBantuan = view.findViewById(R.id.bantuan1);
        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        Bitmap savedImage = ImageUtil.loadImageFromSharedPreferences(getContext());

        if (savedImage != null) {
            Glide.with(this).load(savedImage).into(imgProfile);
        } else {
            Glide.with(this).load(R.drawable.ic_profile).into(imgProfile);
        }

        // Listener untuk buttonNavigate
        buttonNavigate.setOnClickListener(v -> {
            Fragment secondFragment = new Page_detail_akun();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Listener untuk buttonBack (Logout)
        buttonBack.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                .setTitle("Keluar")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    sharedPreferences.logout();
                    Intent intent = new Intent(getActivity(), Page_login.class);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("Tidak", null)
                .show());

        // Listener untuk imageViewGoToOTP
        imageViewGoToOTP.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Page_otpsend.class);
            startActivity(intent);
        });

        // Listener untuk imageViewGoogle
        imageViewGoogle.setOnClickListener(v -> {
            Uri googleUri = Uri.parse("https://bloodcare.my.id/website_bloodcare/website/public_html/");
            Intent intent = new Intent(Intent.ACTION_VIEW, googleUri);
            startActivity(intent);
        });

        // Listener untuk pusat bantuan
        imageViewPusatBantuan.setOnClickListener(v -> {
            String phoneNumber = "+6282142141480";
            String message = "Halo, saya membutuhkan bantuan.";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message)));
            startActivity(intent);
        });

        return view;
    }

    private void fetchUserData(String usernameOrEmail) {
        String url = Config.BASE_URL + "get_user_fullname2.php?usernameOrEmail=" + usernameOrEmail;

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Periksa apakah statusnya success
                        if (response.has("status")) {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONObject data = response.getJSONObject("data");
                                String fullName = data.getString("nama_lengkap");
                                String email = data.getString("email");
                                String phone = data.getString("no_hp");
                                textViewFullName.setText(fullName);
                                textViewEmail.setText(email);
                                textViewPhone.setText(phone);
                            } else {
                                String message = response.optString("message", "Unknown error");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Unexpected response format", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        queue.add(jsonObjectRequest);
    }

}
