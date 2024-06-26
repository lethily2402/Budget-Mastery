package com.finalproject.budgetmastery.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.budgetmastery.Adapter.AdapterCustomSpinnerKhoanThu;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.Model.ModelListKhoanThu;
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


public class IncomeFragment extends Fragment {
    Button btnLuu;
    EditText edtSoTien, edtGhiChu, edtNgayThang;
    Spinner spChonNhom;
    private List<ModelListHome> incomeList;
    private DatabaseReference khoanThuRef;
    private List<ModelListKhoanThu> khoanThuList;
    private DatabaseReference userKhoanThuRef;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khoan_thu, container, false);

        btnLuu = view.findViewById(R.id.btnOk);
        spChonNhom = view.findViewById(R.id.spChonNhom);
        edtSoTien = view.findViewById(R.id.edtSoTien);
        edtGhiChu = view.findViewById(R.id.edtGhiChu);
        edtNgayThang = view.findViewById(R.id.edtNgayThang);

        khoanThuList = new ArrayList<>();
        incomeList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            khoanThuRef = FirebaseDatabase.getInstance().getReference().child("khoanThu");
            userKhoanThuRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("addkhoanthu");
            loadFirebaseData();
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
        loadFirebaseData();

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

    private void loadFirebaseData() {
        khoanThuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                khoanThuList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        ModelListKhoanThu item = snapshot.getValue(ModelListKhoanThu.class);
                        if (item != null && item.getTxt_title() != null) {
                            khoanThuList.add(item);
                        }
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
                updateSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSpinner() {
        AdapterCustomSpinnerKhoanThu spinnerAdapter = new AdapterCustomSpinnerKhoanThu(requireContext(), khoanThuList);
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

    private void saveDataToFirebase() {
        String soTien = edtSoTien.getText().toString().trim();
        String ghiChu = edtGhiChu.getText().toString().trim();
        String ngayThang = edtNgayThang.getText().toString().trim();
        ModelListKhoanThu selectedItem = (ModelListKhoanThu) spChonNhom.getSelectedItem();

        if (soTien.isEmpty() || ghiChu.isEmpty() || ngayThang.isEmpty() || selectedItem == null) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return;
        }

        String nhom = selectedItem.getTxt_title();
        String imageUri = selectedItem.getImageUri();

        String userId = currentUser.getUid();
        DatabaseReference detailRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("chitietkhoanthu").child(nhom).child("income");
        String tvDay = parseDayFromDate(ngayThang);
        ModelListHome newKhoanThu = new ModelListHome(ngayThang, tvDay, nhom, soTien, imageUri);
        String id = userKhoanThuRef.push().getKey();
        if (id != null) {
            userKhoanThuRef.child(id).setValue(newKhoanThu).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        detailRef.child(id).setValue(newKhoanThu).addOnCompleteListener(new OnCompleteListener<Void>() {
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