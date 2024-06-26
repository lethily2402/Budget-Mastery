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
//public class HomeFragment extends Fragment {
//
//    private ListView listView;
//    private AdapterListHome adapter;
//    private List<ModelListHome> transactionList;
//
//    private TextView sotienConLaiTextView;
//    private TextView chiTieuTextView;
//    private TextView thuNhapTextView;
//    private TextView tennguoidung;
//
//    private DatabaseReference expenseReference;
//    private DatabaseReference incomeReference;
//    private DatabaseReference userReference;
//
//    private FirebaseAuth auth;
//    private FirebaseUser currentUser;
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
//        tennguoidung = view.findViewById(R.id.tennguoidung);
//
//        transactionList = new ArrayList<>();
//        adapter = new AdapterListHome(requireContext(), R.layout.home_list_item_by_date, transactionList);
//        listView.setAdapter(adapter);
//
//        auth = FirebaseAuth.getInstance();
//        currentUser = auth.getCurrentUser();
//
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            expenseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanchi");
//            incomeReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanthu");
//            userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
//            loadFirebaseData();
//            loadUserData();
//        } else {
//            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
//        }
//
//        ImageView imgIcon = view.findViewById(R.id.imgSearch);
//        imgIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(requireContext(), SearchActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        return view;
//    }
//
//    private void loadUserData() {
//        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String username = dataSnapshot.child("name").getValue(String.class);
//                    Log.d("HomeFragment", "User name loaded from Firebase: " + username);
//                    tennguoidung.setText("Xin chào, " + username);
//
//
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
//
//    private void loadFirebaseData() {
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
//                Collections.sort(transactionList, new Comparator<ModelListHome>() {
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//                    @Override
//                    public int compare(ModelListHome o1, ModelListHome o2) {
//                        try {
//                            Date date1 = dateFormat.parse(o1.getTvDate());
//                            Date date2 = dateFormat.parse(o2.getTvDate());
//                            int dateComparison = date2.compareTo(date1);
//                            if (dateComparison != 0) {
//                                return dateComparison;
//                            } else {
//                                return Long.compare(o2.getTimestamp(), o1.getTimestamp());
//                            }
//                        } catch (ParseException e) {
//                            throw new IllegalArgumentException(e);
//                        }
//                    }
//                });
//
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

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private TextView tennguoidung;

    private DatabaseReference expenseReference;
    private DatabaseReference incomeReference;
    private DatabaseReference userReference;

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
        tennguoidung = view.findViewById(R.id.tennguoidung);

        transactionList = new ArrayList<>();
        adapter = new AdapterListHome(requireContext(), R.layout.home_list_item_by_date, transactionList);
        listView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            expenseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanchi");
            incomeReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanthu");
            userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            loadFirebaseData();
            loadUserData();
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        ImageView imgIcon = view.findViewById(R.id.imgSearch);
        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
            }
        });

        return view;
    }

    private void showDeleteDialog(final int position) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_xoa);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        TextView textView = dialog.findViewById(R.id.textView8);
        Button btnHuy = dialog.findViewById(R.id.btnBoqua);
        Button btnXoa = dialog.findViewById(R.id.btnOk);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelListHome item = transactionList.get(position);
                deleteItemFromFirebase(item.getKey());
                transactionList.remove(position);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

//    private void deleteItemFromFirebase(String key) {
//        if (key == null) {
//            Log.e("DeleteItem", "Key is null, cannot delete from Firebase");
//            Toast.makeText(requireContext(), "Không thể xóa vì key null", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Remove from 'addkhoanchi' node
//        expenseReference.child(key).removeValue()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(requireContext(), "Xóa khoản chi thành công", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(requireContext(), "Xóa khoản chi thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        // Remove from 'addkhoanthu' node
//        incomeReference.child(key).removeValue()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(requireContext(), "Xóa khoản thu thành công", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(requireContext(), "Xóa khoản thu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
private void deleteItemFromFirebase(String key) {
    if (key == null) {
        Log.e("DeleteItem", "Key is null, cannot delete from Firebase");
        Toast.makeText(requireContext(), "Không thể xóa vì key null", Toast.LENGTH_SHORT).show();
        return;
    }

    // Tiếp tục quá trình xóa
    expenseReference.child(key).removeValue()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(requireContext(), "Xóa khoản chi thành công", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireContext(), "Xóa khoản chi thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    incomeReference.child(key).removeValue()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(requireContext(), "Xóa khoản thu thành công", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireContext(), "Xóa khoản thu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
}


    private void loadUserData() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("name").getValue(String.class);
                    Log.d("HomeFragment", "User name loaded from Firebase: " + username);
                    tennguoidung.setText("Xin chào, " + username);
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
                        if (expense != null && expense.getTvAmount() != null) {
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

                Collections.sort(transactionList, new Comparator<ModelListHome>() {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    @Override
                    public int compare(ModelListHome o1, ModelListHome o2) {
                        try {
                            if (o1.getTvDate() == null || o2.getTvDate() == null) {
                                return 0; // or handle appropriately
                            }

                            Date date1 = dateFormat.parse(o1.getTvDate());
                            Date date2 = dateFormat.parse(o2.getTvDate());
                            int dateComparison = date2.compareTo(date1);
                            if (dateComparison != 0) {
                                return dateComparison;
                            } else {
                                return Long.compare(o2.getTimestamp(), o1.getTimestamp());
                            }
                        } catch (ParseException | NullPointerException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });


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
