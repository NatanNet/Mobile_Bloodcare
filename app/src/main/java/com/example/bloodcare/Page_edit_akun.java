package com.example.bloodcare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Page_edit_akun extends Fragment {

    private static final String ARG_USER_ID = "user_id";  // Ganti nama argument jika perlu
    private int userId;  // Menyimpan user_id yang dikirimkan

    public Page_edit_akun() {
        // Required empty public constructor
    }

    public static Page_edit_akun newInstance(int userId) {
        Page_edit_akun fragment = new Page_edit_akun();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);  // Menyimpan user_id ke Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID, -1);  // Ambil user_id, default -1 jika tidak ada
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page_edit_akun, container, false);

        // Anda dapat menggunakan userId di sini, misalnya menampilkan data terkait user

        return view;
    }
}
