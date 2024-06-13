package com.finalproject.budgetmastery.Fragment;

import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.finalproject.budgetmastery.R;

public class ExpenseFragment extends Fragment {
    EditText edtSoTien, edtGhiChu, edtNgayThang;
    Spinner spChonNhom;
    Button btnLuu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);


        // Tìm View bằng ID main và thiết lập OnApplyWindowInsetsListener
        View mainView = view.findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        edtSoTien = view.findViewById(R.id.edtSoTien);
        spChonNhom = view.findViewById(R.id.spChonNhom);
        edtGhiChu = view.findViewById(R.id.edtGhiChu);
        edtNgayThang = view.findViewById(R.id.edtNgayThang);
        btnLuu = view.findViewById(R.id.btnLuu);

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soTien = edtSoTien.getText().toString();
                String ghiChu = edtGhiChu.getText().toString();
                String ngayThang = edtNgayThang.getText().toString();

                // Xử lý lưu thông tin tại đây
            }
        });

        return view;
    }
}