package com.finalproject.budgetmastery.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.finalproject.budgetmastery.Activity.AdapterFrameHome;
import com.finalproject.budgetmastery.Activity.MainActivity;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;

public class HomeFragment extends Fragment {
    ListView listView;
    AdapterFrameHome adapterFrame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.listview_ds);
        adapterFrame = new AdapterFrameHome(getContext(), R.layout.home_list_item_by_date);


        adapterFrame.add(new ModelListHome("12/03/2003", "2345341", "abcac", "123454"));
        adapterFrame.add(new ModelListHome("12/03/2003", "2345341", "abcac", "123454"));
        adapterFrame.add(new ModelListHome("12/03/2003", "2345341", "abcac", "123454"));
        adapterFrame.add(new ModelListHome("12/03/2003", "2345341", "abcac", "123454"));
        adapterFrame.add(new ModelListHome("12/03/2003", "2345341", "abcac", "123454"));

        listView.setAdapter(adapterFrame);

        return view;
    }
}