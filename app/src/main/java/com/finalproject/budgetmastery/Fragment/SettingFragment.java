//package com.finalproject.budgetmastery.Fragment;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//
//import com.finalproject.budgetmastery.R;
//
//public class SettingFragment extends Fragment {
//    private View view;
//    LinearLayout layoutLogout, layoutProfile;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_setting, container, false);
//        layoutLogout = view.findViewById(R.id.btn_logout);
//        layoutProfile = view.findViewById(R.id.btn_profile);
//
//
//
//
//
//        layoutLogout.setOnClickListener(v -> {
//            Dialog dialogLogOut = new Dialog(view.getContext());
//            dialogLogOut.setContentView(R.layout.fragment_log_out);
//            dialogLogOut.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            Button btnLogOut = dialogLogOut.findViewById(R.id.btn_logout);
//            Button btnCancel = dialogLogOut.findViewById(R.id.btn_cancel);
//            btnLogOut.setOnClickListener(view -> {
//                Intent intent = new Intent(view.getContext(), com.finalproject.budgetmastery.Activity.LoginActivity.class);
//                startActivity(intent);
//            });
//            btnCancel.setOnClickListener(view -> dialogLogOut.dismiss());
//            dialogLogOut.show();
//        });
//
//
//        //sự kiện chuyển sang trang profile
//        layoutProfile.setOnClickListener(v -> {
//            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//            SettingProfileFragment settingProfileFragment = new SettingProfileFragment();
//            fragmentManager.beginTransaction().replace(R.id.frLayout, settingProfileFragment).commit();
//        });
//
//        return view;
//    }
//}

package com.finalproject.budgetmastery.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.finalproject.budgetmastery.R;

public class SettingFragment extends Fragment {
    private View view;
    private LinearLayout layoutLogout, layoutProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        layoutLogout = view.findViewById(R.id.btn_logout);
        layoutProfile = view.findViewById(R.id.btn_profile);

        layoutLogout.setOnClickListener(v -> {
            Dialog dialogLogOut = new Dialog(view.getContext());
            dialogLogOut.setContentView(R.layout.fragment_log_out);
            dialogLogOut.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button btnLogOut = dialogLogOut.findViewById(R.id.btn_logout);
            Button btnCancel = dialogLogOut.findViewById(R.id.btn_cancel);
            btnLogOut.setOnClickListener(view1 -> {
                Intent intent = new Intent(view.getContext(), com.finalproject.budgetmastery.Activity.LoginActivity.class);
                startActivity(intent);
            });
            btnCancel.setOnClickListener(view12 -> dialogLogOut.dismiss());
            dialogLogOut.show();
        });

        // Sự kiện chuyển sang trang profile
        layoutProfile.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            SettingProfileFragment settingProfileFragment = new SettingProfileFragment();
            fragmentManager.beginTransaction().replace(R.id.frLayout, settingProfileFragment).commit();
        });

        return view;
    }
}
