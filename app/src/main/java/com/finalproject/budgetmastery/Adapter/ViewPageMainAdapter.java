package com.finalproject.budgetmastery.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.finalproject.budgetmastery.Activity.DsKhoanChi;
import com.finalproject.budgetmastery.Fragment.HomeFragment;
import com.finalproject.budgetmastery.Activity.KhoanChi;

public class ViewPageMainAdapter extends FragmentStateAdapter {

    public ViewPageMainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new KhoanChi();
            case 2:
//               return new ThongKeFragment();
            case 3:
                return new DsKhoanChi();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Số lượng Fragment trong ViewPager2
    }
}

