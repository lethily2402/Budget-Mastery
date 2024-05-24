package com.finalproject.budgetmastery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KhoanChi extends AppCompatActivity {
    EditText edtSoTien, edtGhiChu, edtNgayThang;
    Spinner spChonNhom;
    Button btnLuu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khoan_chi);

        // Tìm View bằng ID main và thiết lập OnApplyWindowInsetsListener
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        edtSoTien = findViewById(R.id.edtSoTien);
        spChonNhom = findViewById(R.id.spChonNhom);
        edtGhiChu = findViewById(R.id.edtGhiChu);
        edtNgayThang = findViewById(R.id.edtNgayThang);
        btnLuu = findViewById(R.id.btnLuu);

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soTien = edtSoTien.getText().toString();
                String ghiChu = edtGhiChu.getText().toString();
                String ngayThang = edtNgayThang.getText().toString();

                // Xử lý lưu thông tin tại đây
            }
        });
    }
}
