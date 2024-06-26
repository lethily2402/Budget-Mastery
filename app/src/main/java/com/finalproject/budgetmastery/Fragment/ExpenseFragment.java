package com.finalproject.budgetmastery.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.budgetmastery.Adapter.AdapterCustomSpinnerKhoanChi;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseFragment extends Fragment {

    Button btnLuu;
    EditText edtSoTien, edtGhiChu, edtNgayThang;
    Spinner spChonNhom;

    private List<ModelListKhoanChi> khoanChiList;
    private List<ModelListHome> expenseList;

    private DatabaseReference khoanChiRef;
    private DatabaseReference userKhoanChiRef;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khoan_chi, container, false);

        btnLuu = view.findViewById(R.id.btnOk);
        spChonNhom = view.findViewById(R.id.spChonNhom);
        edtSoTien = view.findViewById(R.id.edtSoTien);
        edtGhiChu = view.findViewById(R.id.edtGhiChu);
        edtNgayThang = view.findViewById(R.id.edtNgayThang);

        khoanChiList = new ArrayList<>();
        expenseList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            khoanChiRef = FirebaseDatabase.getInstance().getReference().child("khoanChi");
            userKhoanChiRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanchi");
            loadFirebaseData();
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        loadFirebaseData();


        spChonNhom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ModelListKhoanChi selectedItem = (ModelListKhoanChi) parent.getItemAtPosition(position);
                if (selectedItem != null) {
                    loadExpenses(selectedItem.getTxt_title());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        edtNgayThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFirebase();
            }
        });

        return view;
    }

//    private void loadFirebaseData() {
//        khoanChiRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                khoanChiList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    try {
//                        ModelListKhoanChi item = snapshot.getValue(ModelListKhoanChi.class);
//                        if (item != null && item.getTxt_title() != null) {
//                            khoanChiList.add(item);
//                        }
//                    } catch (ClassCastException e) {
//                        e.printStackTrace();
//                    }
//                }
//                updateSpinner();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
private void loadFirebaseData() {
    khoanChiRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            khoanChiList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                try {
                    // Capture the key of the snapshot
                    String key = snapshot.getKey();
                    ModelListKhoanChi item = snapshot.getValue(ModelListKhoanChi.class);
                    if (item != null) {
                        item.setKey(key); // Set the key to the ModelListKhoanChi object
                        khoanChiList.add(item);
                    }
                } catch (Exception e) {
                    Log.e("FirebaseData", "Failed to convert data to ModelListKhoanChi", e);
                }
            }
            updateSpinner();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(requireContext(), "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

    private void updateSpinner() {
        AdapterCustomSpinnerKhoanChi spinnerAdapter = new AdapterCustomSpinnerKhoanChi(requireContext(), khoanChiList);
        spChonNhom.setAdapter(spinnerAdapter);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edtNgayThang.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void loadExpenses(String category) {
        // Clear existing list
        expenseList.clear();
        String userId = currentUser.getUid();
        DatabaseReference userKhoanChiRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("addkhoanchi").child(category).child("income");

        userKhoanChiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelListHome expense = snapshot.getValue(ModelListHome.class);
                    if (expense != null) {
                        expenseList.add(expense);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load expenses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveDataToFirebase() {
        String soTien = edtSoTien.getText().toString().trim();
        String ghiChu = edtGhiChu.getText().toString().trim();
        String ngayThang = edtNgayThang.getText().toString().trim();
        ModelListKhoanChi selectedItem = (ModelListKhoanChi) spChonNhom.getSelectedItem();

        if (soTien.isEmpty() || ghiChu.isEmpty() || ngayThang.isEmpty() || selectedItem == null) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return;
        }

        String nhom = selectedItem.getTxt_title();
        String imageUri = selectedItem.getImageUri();
        String userId = currentUser.getUid();
        DatabaseReference detailRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("chitiet").child(nhom).child("expense");

        String tvDay = parseDayFromDate(ngayThang);
        ModelListHome newKhoanChi = new ModelListHome(ngayThang, tvDay, nhom, soTien, imageUri);
        String id = userKhoanChiRef.push().getKey();
        if (id != null) {
            userKhoanChiRef.child(id).setValue(newKhoanChi).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        detailRef.child(id).setValue(newKhoanChi).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                    resetFields();
                                } else {
                                    Toast.makeText(requireContext(), "Lưu không thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(requireContext(), "Lưu không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(requireContext(), "Lưu không thành công", Toast.LENGTH_SHORT).show();
        }
    }



    private void resetFields() {
        edtSoTien.setText("");
        edtGhiChu.setText("");
        edtNgayThang.setText("");
        spChonNhom.setSelection(0);
    }

    private String parseDayFromDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(dateString));
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    return "Chủ Nhật";
                case Calendar.MONDAY:
                    return "Thứ Hai";
                case Calendar.TUESDAY:
                    return "Thứ Ba";
                case Calendar.WEDNESDAY:
                    return "Thứ Tư";
                case Calendar.THURSDAY:
                    return "Thứ Năm";
                case Calendar.FRIDAY:
                    return "Thứ Sáu";
                case Calendar.SATURDAY:
                    return "Thứ Bảy";
                default:
                    return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
