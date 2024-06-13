package com.finalproject.budgetmastery.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.finalproject.budgetmastery.R;

import java.util.Calendar;


public class IncomeFragment extends Fragment {
    Button btnLuu;
    EditText edtSoTien, edtGhiChu, edtNgayThang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_khoan_thu, container, false);
        btnLuu = view.findViewById(R.id.btnOk);
        edtSoTien = view.findViewById(R.id.edtSoTien);
        edtGhiChu = view.findViewById(R.id.edtGhiChu);
        edtNgayThang = view.findViewById(R.id.edtNgayThang);

        edtNgayThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edtNgayThang.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }
}