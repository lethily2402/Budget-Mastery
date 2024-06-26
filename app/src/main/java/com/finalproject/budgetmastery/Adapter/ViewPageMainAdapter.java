package com.finalproject.budgetmastery.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.finalproject.budgetmastery.Activity.DsKhoanChi;
import com.finalproject.budgetmastery.Fragment.AddFragment;
import com.finalproject.budgetmastery.Fragment.HomeFragment;
import com.finalproject.budgetmastery.Fragment.ReportFragment;
import com.finalproject.budgetmastery.Fragment.SettingFragment;

public class ViewPageMainAdapter extends FragmentStateAdapter {

    public ViewPageMainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                 return new DsKhoanChi();
            case 2:
                return new AddFragment();
            case 3:
                return new ReportFragment();
            case 4:
                return new SettingFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Số lượng Fragment trong ViewPager2
    }
}

