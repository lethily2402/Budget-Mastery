package com.finalproject.budgetmastery.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finalproject.budgetmastery.R;
import com.google.android.material.tabs.TabLayout;

public class ReportFragment extends Fragment {
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);


        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WeekFragment()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selectedFragment = null;
                if (tab.getText().equals("Tháng")) {
                    selectedFragment = new MonthFragment();
                } else if (tab.getText().equals("Tuần")) {
                    selectedFragment = new WeekFragment();
                }

                if (selectedFragment != null) {
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }
}