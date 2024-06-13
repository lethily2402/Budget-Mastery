package com.finalproject.budgetmastery.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finalproject.budgetmastery.Model.ModelListLKhoanChi;
import com.finalproject.budgetmastery.R;

public class DsKhoanChiActivity extends AppCompatActivity {
    ListView listView;
    AdapterFrameKhoanChi adapterFrame;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ds_khoan_chi);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            Log.e("DsKhoanChi", "View with ID 'main' not found");
        }

        listView = findViewById(R.id.listview_ds);
        adapterFrame = new AdapterFrameKhoanChi(this, R.layout.khoanchi_list);

        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        adapterFrame.add(new ModelListLKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        listView.setAdapter(adapterFrame);
    }
}
