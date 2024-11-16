package com.example.bloodcare;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Page_acaradonor extends AppCompatActivity {

    private EditText etDate;
    private EditText editTextData;
    private Button buttonSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_acaradonor);

        etDate = findViewById(R.id.etDate);
        editTextData = findViewById(R.id.editTextLokasiDonor);  // Hanya set salah satu

        buttonSimpan = findViewById(R.id.btnSave);

        etDate.setOnClickListener(v -> showDatePicker());

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editTextData.getText().toString().trim();

                if (data.isEmpty()) {
                    // Jika data belum diisi
                    Toast.makeText(Page_acaradonor.this, "Lengkapi data", Toast.LENGTH_SHORT).show();
                } else {
                    // Jika data sudah diisi
                    Toast.makeText(Page_acaradonor.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Page_acaradonor.this,
                (view, selectedYear, selectedMonth, selectedDay) ->
                        etDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear),
                year, month, day
        );
        datePickerDialog.show();
    }
}
