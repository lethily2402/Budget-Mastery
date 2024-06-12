package com.finalproject.budgetmastery.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.finalproject.budgetmastery.R;

import java.util.Calendar;
import java.util.List;

public class KhoanChiFragment extends Fragment {
    Button btnLuu;
    EditText edtSoTien, edtGhiChu, edtNgayThang;
    Spinner spChonNhom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khoan_chi, container, false);

        btnLuu = view.findViewById(R.id.btnLuu);
        spChonNhom = view.findViewById(R.id.spChonNhom);
        edtSoTien = view.findViewById(R.id.edtSoTien);
        edtGhiChu = view.findViewById(R.id.edtGhiChu);
        edtNgayThang = view.findViewById(R.id.edtNgayThang);

//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        DsKhoanChiFragment dsKhoanChiFragment = (DsKhoanChiFragment) fragmentManager.findFragmentById(R.id.listview_ds);
//
//        if (dsKhoanChiFragment != null) {
//            List<String> items = dsKhoanChiFragment.getItems();
//            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
//            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spChonNhom.setAdapter(spinnerAdapter);
//        }

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
