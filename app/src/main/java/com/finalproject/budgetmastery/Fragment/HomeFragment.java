
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
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
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
//    private DatabaseReference expenseReference;
//    private DatabaseReference incomeReference;
//
//    private FirebaseAuth auth;
//    private FirebaseUser currentUser;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        listView = view.findViewById(R.id.listview_ds);
//        sotienConLaiTextView = view.findViewById(R.id.hoSo);
//        chiTieuTextView = view.findViewById(R.id.chitieu);
//        thuNhapTextView = view.findViewById(R.id.thunhap);
//
//        transactionList = new ArrayList<>();
//        adapter = new AdapterListHome(requireContext(), R.layout.home_list_item_by_date, transactionList);
//        listView.setAdapter(adapter);
//
//        // Firebase setup
//        auth = FirebaseAuth.getInstance();
//        currentUser = auth.getCurrentUser();
//
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            expenseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanchi");
//            incomeReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanthu");
//
//            loadFirebaseData();
//        } else {
//            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
//        }
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
//    private void loadUserData() {
//        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String username = dataSnapshot.child("username").getValue(String.class);
//                    tenNguoiDungTextView.setText("Xin chào " + username);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu người dùng: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void loadFirebaseData() {
//
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
//                loadIncomeData(totalChitieu);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void loadIncomeData(final int totalChitieu) {
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
//
//                // Sort transactionList by date (newest to oldest)
//                Collections.sort(transactionList, new Comparator<ModelListHome>() {
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//                    @Override
//                    public int compare(ModelListHome o1, ModelListHome o2) {
//                        try {
//                            Date date1 = dateFormat.parse(o1.getTvDate());
//                            Date date2 = dateFormat.parse(o2.getTvDate());
//                            // Sort by date first
//                            int dateComparison = date2.compareTo(date1);
//                            if (dateComparison != 0) {
//                                return dateComparison;
//                            } else {
//                                // If dates are the same, sort by timestamp
//                                return Long.compare(o2.getTimestamp(), o1.getTimestamp());
//                            }
//                        } catch (ParseException e) {
//                            throw new IllegalArgumentException(e);
//                        }
//                    }
//                });
//
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private ListView listView;
    private AdapterListHome adapter;
    private List<ModelListHome> transactionList;

    private TextView sotienConLaiTextView;
    private TextView chiTieuTextView;
    private TextView thuNhapTextView;
    private TextView tennguoidung; // Add this

    private DatabaseReference expenseReference;
    private DatabaseReference incomeReference;
    private DatabaseReference userReference; // Add this

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.listview_ds);
        sotienConLaiTextView = view.findViewById(R.id.hoSo);
        chiTieuTextView = view.findViewById(R.id.chitieu);
        thuNhapTextView = view.findViewById(R.id.thunhap);
        tennguoidung = view.findViewById(R.id.tennguoidung); // Initialize tennguoidung

        transactionList = new ArrayList<>();
        adapter = new AdapterListHome(requireContext(), R.layout.home_list_item_by_date, transactionList);
        listView.setAdapter(adapter);

        // Firebase setup
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            expenseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanchi");
            incomeReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanthu");
            userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId); // Initialize userReference

            loadFirebaseData();
            loadUserData(); // Load user data for greeting
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

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

    private void loadUserData() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("name").getValue(String.class);
                    Log.d("HomeFragment", "User name loaded from Firebase: " + username); // Log to check username
                    tennguoidung.setText("Xin chào " + username);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu người dùng: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

                // Sort transactionList by date (newest to oldest)
                Collections.sort(transactionList, new Comparator<ModelListHome>() {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    @Override
                    public int compare(ModelListHome o1, ModelListHome o2) {
                        try {
                            Date date1 = dateFormat.parse(o1.getTvDate());
                            Date date2 = dateFormat.parse(o2.getTvDate());
                            // Sort by date first
                            int dateComparison = date2.compareTo(date1);
                            if (dateComparison != 0) {
                                return dateComparison;
                            } else {
                                // If dates are the same, sort by timestamp
                                return Long.compare(o2.getTimestamp(), o1.getTimestamp());
                            }
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });

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
