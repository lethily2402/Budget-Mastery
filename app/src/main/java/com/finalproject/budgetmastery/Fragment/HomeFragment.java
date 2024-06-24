//
//package com.finalproject.budgetmastery.Fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.finalproject.budgetmastery.Activity.SearchActivity;
//import com.finalproject.budgetmastery.Adapter.AdapterListHome;
//import com.finalproject.budgetmastery.Model.ModelListHome;
//import com.finalproject.budgetmastery.R;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HomeFragment extends Fragment {
//
//    private ListView listView;
//    private AdapterListHome adapter;
//    private List<ModelListHome> transactionList;
//
//    private TextView sotienConLaiTextView;
//    private TextView chiTieuTextView;
//    private TextView thuNhapTextView;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        listView = view.findViewById(R.id.listview_ds);
//        sotienConLaiTextView = view.findViewById(R.id.Sotienconlai);
//        chiTieuTextView = view.findViewById(R.id.chitieu);
//        thuNhapTextView = view.findViewById(R.id.thunhap);
//
//
//        transactionList = new ArrayList<>();
//        adapter = new AdapterListHome(requireContext(), R.layout.home_list_item_by_date, transactionList);
//        listView.setAdapter(adapter);
//
//        // Firebase setup
//        DatabaseReference expenseReference = FirebaseDatabase.getInstance().getReference().child("addkhoanchi");
//        DatabaseReference incomeReference = FirebaseDatabase.getInstance().getReference().child("addkhoanthu");
//
//        loadFirebaseData(expenseReference, incomeReference);
//
//        // Search icon click listener
//        ImageView imgIcon = view.findViewById(R.id.imgSearch);
//        imgIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(requireContext(), SearchActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        return view;
//    }
//
//    private void loadFirebaseData(DatabaseReference expenseReference, DatabaseReference incomeReference) {
//        expenseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int totalChitieu = 0;
//                transactionList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    try {
//                        ModelListHome expense = snapshot.getValue(ModelListHome.class);
//                        if (expense != null) {
//                            transactionList.add(expense);
//                            totalChitieu += Integer.parseInt(expense.getTvAmount());
//                        }
//                    } catch (Exception e) {
//                        Log.e("FirebaseData", "Failed to convert data to ModelListHome", e);
//                    }
//                }
//                // Load income data and calculate balance after loading expense data
//                loadIncomeData(incomeReference, totalChitieu);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void loadIncomeData(DatabaseReference incomeReference, int totalChitieu) {
//        incomeReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int totalThuNhap = 0;
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    try {
//                        ModelListHome income = snapshot.getValue(ModelListHome.class);
//                        if (income != null) {
//                            transactionList.add(income);
//                            totalThuNhap += Integer.parseInt(income.getTvAmount());
//                        }
//                    } catch (Exception e) {
//                        Log.e("FirebaseData", "Failed to convert data to ModelListHome", e);
//                    }
//                }
//                // Update UI with total income, expense, and remaining balance
//                updateUI(totalThuNhap, totalChitieu);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateUI(int totalThuNhap, int totalChitieu) {
//        int sotienConLai = totalThuNhap - totalChitieu;
//        sotienConLaiTextView.setText(String.valueOf(sotienConLai));
//        chiTieuTextView.setText("-" + totalChitieu);
//        thuNhapTextView.setText("+" + totalThuNhap);
//    }
//}
package com.finalproject.budgetmastery.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.budgetmastery.Activity.SearchActivity;
import com.finalproject.budgetmastery.Adapter.AdapterListHome;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ListView listView;
    private AdapterListHome adapter;
    private List<ModelListHome> transactionList;

    private TextView sotienConLaiTextView;
    private TextView chiTieuTextView;
    private TextView thuNhapTextView;

    private DatabaseReference expenseReference;
    private DatabaseReference incomeReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.listview_ds);
        sotienConLaiTextView = view.findViewById(R.id.Sotienconlai);
        chiTieuTextView = view.findViewById(R.id.chitieu);
        thuNhapTextView = view.findViewById(R.id.thunhap);

        transactionList = new ArrayList<>();
        adapter = new AdapterListHome(requireContext(), R.layout.home_list_item_by_date, transactionList);
        listView.setAdapter(adapter);

        // Firebase setup
        expenseReference = FirebaseDatabase.getInstance().getReference().child("addkhoanchi");
        incomeReference = FirebaseDatabase.getInstance().getReference().child("addkhoanthu");

        loadFirebaseData();

        // Search icon click listener
        ImageView imgIcon = view.findViewById(R.id.imgSearch);
        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadFirebaseData() {
        expenseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalChitieu = 0;
                transactionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        ModelListHome expense = snapshot.getValue(ModelListHome.class);
                        if (expense != null) {
                            transactionList.add(expense);
                            totalChitieu += Integer.parseInt(expense.getTvAmount());
                        }
                    } catch (Exception e) {
                        Log.e("FirebaseData", "Failed to convert data to ModelListHome", e);
                    }
                }
                // Load income data and calculate balance after loading expense data
                loadIncomeData(totalChitieu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadIncomeData(final int totalChitieu) {
        incomeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalThuNhap = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        ModelListHome income = snapshot.getValue(ModelListHome.class);
                        if (income != null) {
                            transactionList.add(income);
                            totalThuNhap += Integer.parseInt(income.getTvAmount());
                        }
                    } catch (Exception e) {
                        Log.e("FirebaseData", "Failed to convert data to ModelListHome", e);
                    }
                }
                // Update UI with total income, expense, and remaining balance
                updateUI(totalThuNhap, totalChitieu);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(int totalThuNhap, int totalChitieu) {
        int sotienConLai = totalThuNhap - totalChitieu;
        sotienConLaiTextView.setText(String.valueOf(sotienConLai));
        chiTieuTextView.setText("-" + totalChitieu);
        thuNhapTextView.setText("+" + totalThuNhap);
    }
}

