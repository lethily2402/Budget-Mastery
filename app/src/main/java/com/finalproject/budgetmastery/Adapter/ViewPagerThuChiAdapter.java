package com.finalproject.budgetmastery.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.finalproject.budgetmastery.Fragment.DsKhoanChiFragment;
import com.finalproject.budgetmastery.Fragment.DsKhoanThuFragment;

public class ViewPagerThuChiAdapter extends FragmentStateAdapter {

    public ViewPagerThuChiAdapter(@NonNull FragmentActivity fragmentActivity)    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DsKhoanChiFragment();
            case 1:
                return new DsKhoanThuFragment();
            default:
                return new DsKhoanChiFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
