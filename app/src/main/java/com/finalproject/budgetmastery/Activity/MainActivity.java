package com.finalproject.budgetmastery.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.finalproject.budgetmastery.R;
import com.finalproject.budgetmastery.Fragment.ReportFragment;
import com.finalproject.budgetmastery.Adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.navigator);

        // Tạo danh sách các Fragment
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ReportFragment()); // Fragment cho mục Report
        fragmentList.add(new SettingFragment()); // Fragment cho mục Settings

        // Thiết lập Adapter cho ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        // Bắt sự kiện khi một mục trong BottomNavigationView được chọn
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Thongke) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (item.getItemId() == R.id.Caidat) {
                    viewPager.setCurrentItem(1);
                    return true;
                }

                return false;
            }
        });

        // Bắt sự kiện khi trượt qua các trang trên ViewPager2 và cập nhật mục được chọn trong BottomNavigationView
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.Vitien);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.Caidat);
                        break;
                }
            }
        });
    }
}
