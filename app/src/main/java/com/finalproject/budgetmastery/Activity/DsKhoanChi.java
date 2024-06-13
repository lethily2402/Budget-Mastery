package com.finalproject.budgetmastery.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.finalproject.budgetmastery.Adapter.ViewPagerThuChiAdapter;
import com.finalproject.budgetmastery.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.FirebaseDatabase;

public class DsKhoanChi extends Fragment {
    private TabLayout tabLayout1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ds_khoan_chi, container, false);



        tabLayout1 = view.findViewById(R.id.tabLayout1);
        ViewPager2 viewPager = view.findViewById(R.id.viewPagerList);

        ViewPagerThuChiAdapter adapter = new ViewPagerThuChiAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout1, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Khoản Chi");
                    break;
                case 1:
                    tab.setText("Khoản Thu");
                    break;
            }
        }).attach();

        return view;
    }

}
