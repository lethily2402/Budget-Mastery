package com.finalproject.budgetmastery.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.finalproject.budgetmastery.Adapter.AdapterListKhoanThu;
import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.Model.ModelListKhoanThu;
import com.finalproject.budgetmastery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DsKhoanThuFragment extends Fragment {
    private ListView listView;
    private AdapterListKhoanThu adapter;
    private Button btnThemphanloai;
    private List<ModelListKhoanThu> listItems;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    ImageView imageIcon;
    private DatabaseReference khoanThuRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ds_khoan_thu, container, false);
        listView = view.findViewById(R.id.listview_ds);
        btnThemphanloai = view.findViewById(R.id.btnThemphanloai);
        listItems = new ArrayList<>();

        adapter = new AdapterListKhoanThu(requireContext(), R.layout.khoanthu_list, listItems);
        listView.setAdapter(adapter);

        khoanThuRef = FirebaseDatabase.getInstance().getReference().child("khoanThu");
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
        return view;
    }
    private void loadFirebaseData(){
        khoanThuRef.addValueEventListener(new ValueEventListener() {
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
                                ModelListKhoanThu income = new ModelListKhoanThu(imageUri, tenNhom, key);
                                adapter.add(income);
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
        khoanThuRef.child(key).removeValue()
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

                // Tạo đối tượng ModelListKhoanThu với đường dẫn ảnh và tên nhóm
                ModelListKhoanThu newItem = new ModelListKhoanThu(selectedImageUri.toString(), tenNhom, null);

                // Đường dẫn tới node "khoanThu" trong Realtime Database
                DatabaseReference khoanThuRef = FirebaseDatabase.getInstance().getReference().child("khoanThu");

                // Push dữ liệu mới lên Realtime Database
                String newItemKey = khoanThuRef.push().getKey();
                newItem.setKey(newItemKey);
                khoanThuRef.child(newItemKey).setValue(newItem)
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
                ModelListKhoanThu item = listItems.get(position);

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
