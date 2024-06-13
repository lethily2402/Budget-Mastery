package com.finalproject.budgetmastery.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.finalproject.budgetmastery.Fragment.ExpenseFragment;
import com.finalproject.budgetmastery.Fragment.IncomeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new IncomeFragment();
        }
        return new ExpenseFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}