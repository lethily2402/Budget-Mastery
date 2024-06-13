package com.finalproject.budgetmastery.Activity;

//
import android.os.Bundle;

        import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.finalproject.budgetmastery.Adapter.ViewPageMainAdapter;
import com.finalproject.budgetmastery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_home);

        // Handle window insets for immersive mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        // Setup BottomNavigationView
//        BottomNavigationView navigationBarView = findViewById(R.id.navigation);
//        navigationBarView.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.home) {
////                startActivity(new Intent(this, Home.class));
//
//                return true;
//            } else if (id == R.id.Vitien) {
//                startActivity(new Intent(this, KhoanChi.class));
//                return true;
//            } else if (id == R.id.Thongke) {
//                startActivity(new Intent(this, ThongKe.class));
//                return true;
//            } else if (id == R.id.Caidat) {
//                startActivity(new Intent(this, CaiDat.class));
//                return true;
//            }
//            return false;
//        });
        ViewPager2 viewPager = findViewById(R.id.viewPagerMain);
        ViewPageMainAdapter viewPageMainAdapter = new ViewPageMainAdapter(this);
        viewPager.setAdapter(viewPageMainAdapter);


        BottomNavigationView navigationBarView = findViewById(R.id.navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                viewPager.setCurrentItem(0);

                return true;
            } else if (id == R.id.Vitien) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (id == R.id.Thongke) {
                viewPager.setCurrentItem(2);

                return true;
            } else if (id == R.id.Caidat) {
                viewPager.setCurrentItem(3);
            }
            return false;
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Chuyển đổi mục được chọn của BottomNavigationView tương ứng với trang được chọn trong ViewPager2
                if (position == 0) {
                    navigationBarView.setSelectedItemId(R.id.home);
                } else if (position == 1) {
                    navigationBarView.setSelectedItemId(R.id.Vitien);
                } else if (position == 2) {
                    navigationBarView.setSelectedItemId(R.id.Thongke);
                } else if (position == 3) {
                    navigationBarView.setSelectedItemId(R.id.Caidat);
                }
            }
        });


    }


}
