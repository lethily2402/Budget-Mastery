package com.finalproject.budgetmastery.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.finalproject.budgetmastery.Fragment.AddFragment;
import com.finalproject.budgetmastery.Fragment.HomeFragment;
import com.finalproject.budgetmastery.Fragment.ReportFragment;
import com.finalproject.budgetmastery.Fragment.SettingFragment;
import com.finalproject.budgetmastery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigator);
        selectedFragment = new HomeFragment();
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, selectedFragment).commit();
        }

        // Bắt sự kiện khi một mục trong BottomNavigationView được chọn
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_wallet) {
                    selectedFragment = new DsKhoanChi();
                } else if (itemId == R.id.nav_plus) {
                    selectedFragment = new AddFragment();
                } else if (itemId == R.id.nav_report) {
                    selectedFragment = new ReportFragment();
                }
                else if (itemId == R.id.nav_setting) {
                    selectedFragment = new SettingFragment();
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frLayout, selectedFragment).commit();
                }
                return true;
            }
        });
    }
}