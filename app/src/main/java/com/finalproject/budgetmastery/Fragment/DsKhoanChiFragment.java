package com.finalproject.budgetmastery.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.finalproject.budgetmastery.Adapter.AdapterExpense;
import com.finalproject.budgetmastery.Adapter.AdapterListKhoanChi;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DsKhoanChiFragment extends Fragment {
    private ListView listView;
    private AdapterListKhoanChi adapter;
    private Button btnThemphanloai;
    private List<ModelListKhoanChi> listItems;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    ImageView imageIcon;
    private DatabaseReference khoanChiRef;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ds_khoan_chi, container, false);
        listView = view.findViewById(R.id.listview_ds);
        btnThemphanloai = view.findViewById(R.id.btnThemphanloai);
        listItems = new ArrayList<>();

        adapter = new AdapterListKhoanChi(requireContext(), R.layout.khoanchi_list, listItems);
        listView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Khởi tạo DatabaseReference
        khoanChiRef = FirebaseDatabase.getInstance().getReference().child("khoanChi");

        // Load dữ liệu từ Firebase và hiển thị trong ListView
        loadFirebaseData();
        btnThemphanloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThemDialog(Gravity.CENTER);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelListKhoanChi selectedItem = listItems.get(position);
                showExpensesDialog(selectedItem.getTxt_title());
            }
        });

        return view;
    }

    private void showExpensesDialog(String category) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chitiet);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        // Thiết lập các thuộc tính cho window để dialog có thể tràn màn hình
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT; // Sửa đổi chiều cao để tràn màn hình
        window.setAttributes(layoutParams);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ListView listViewExpenses = dialog.findViewById(R.id.listView1);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(category);

        List<ModelListHome> expenseList = new ArrayList<>();
        AdapterExpense adapter = new AdapterExpense(requireContext(), R.layout.home_list_item_by_date, expenseList);
        listViewExpenses.setAdapter(adapter);

        // Get UID of current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle if the user is not logged in
            return;
        }
        String userId = currentUser.getUid();

        // Reference to "users/{userId}/addkhoanchi/{category}/income"
        DatabaseReference userKhoanChiRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("chitiet").child(category).child("expense");

        userKhoanChiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelListHome expense = snapshot.getValue(ModelListHome.class);
                    if (expense != null) {
                        expenseList.add(expense);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load expenses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Close dialog on button click
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void loadFirebaseData() {
        khoanChiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                        if (data != null) {
                            String imageUri = (String) data.get("imageUri");
                            String tenNhom = (String) data.get("txt_title");
                            String key = snapshot.getKey(); // Lấy key từ snapshot

                            if (imageUri != null && tenNhom != null) {
                                // Tạo đối tượng ModelListKhoanChi với key
                                ModelListKhoanChi expense = new ModelListKhoanChi(imageUri, tenNhom, key);
                                adapter.add(expense);
                            } else {
                                Log.w("FirebaseData", "Item có dữ liệu null bị bỏ qua: " + snapshot.getKey());
                            }
                        }
                    } catch (ClassCastException e) {
                        Log.e("FirebaseData", "Chuyển đổi dữ liệu sang ModelListKhoanChi thất bại", e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseData", "Lỗi cơ sở dữ liệu: " + databaseError.getMessage());
            }
        });
    }


    private void deleteItemFromFirebase(String key) {
        khoanChiRef.child(key).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openThemDialog(int gravity) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        ImageView imageView = dialog.findViewById(R.id.imageIcon);
        EditText edtThemnhom = dialog.findViewById(R.id.edtThemnhom);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnLuu = dialog.findViewById(R.id.btnLuu);
        imageIcon = imageView;
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy tên nhóm từ EditText
                String tenNhom = edtThemnhom.getText().toString().trim();
                if (tenNhom.isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng nhập tên nhóm", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra xem đã chọn ảnh chưa
                if (selectedImageUri == null) {
                    Toast.makeText(requireContext(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo đối tượng ModelListKhoanChi với đường dẫn ảnh và tên nhóm
                ModelListKhoanChi newItem = new ModelListKhoanChi(selectedImageUri.toString(), tenNhom, null);

                // Đường dẫn tới node "khoanChi" trong Realtime Database
                DatabaseReference khoanChiRef = FirebaseDatabase.getInstance().getReference().child("khoanChi");

                // Push dữ liệu mới lên Realtime Database
                String newItemKey = khoanChiRef.push().getKey();
                newItem.setKey(newItemKey);
                khoanChiRef.child(newItemKey).setValue(newItem)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Hiển thị thông báo khi lưu thành công và đóng dialog
                                Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Hiển thị thông báo khi lưu thất bại
                                Toast.makeText(requireContext(), "Lưu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở trình chọn ảnh từ thư viện của thiết bị
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Cập nhật ảnh được chọn vào ImageView
            if (imageIcon != null) {
                imageIcon.setImageURI(selectedImageUri);
            }
        }
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
                // Lấy item hiện tại
                ModelListKhoanChi item = listItems.get(position);

                // Xóa item từ Firebase
                deleteItemFromFirebase(item.getKey());

                // Xóa item từ list và cập nhật adapter
                listItems.remove(position);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

}