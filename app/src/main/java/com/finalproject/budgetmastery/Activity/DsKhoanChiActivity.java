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

import com.finalproject.budgetmastery.Adapter.AdapterListKhoanChi;
import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.R;

public class DsKhoanChiActivity extends AppCompatActivity {
    ListView listView;
    AdapterListKhoanChi adapterFrame;

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

//         Tìm cách sửa lỗi phần dưới này đi nhé
//         Phân rõ ra ai code file nào chứ đừng động vào file của nhau là nó lỗi hết đấy

//        adapterFrame = new AdapterListKhoanChi(this, R.layout.khoanchi_list);
//
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
//        adapterFrame.add(new ModelListKhoanChi(R.drawable.thucpham_icon, "Thực phẩm"));
        listView.setAdapter(adapterFrame);
    }
}
