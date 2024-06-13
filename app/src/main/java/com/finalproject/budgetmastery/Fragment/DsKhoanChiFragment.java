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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.budgetmastery.Adapter.AdapterListKhoanChi;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
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


public class DsKhoanChiFragment extends Fragment {
    private ListView listView;
    private AdapterListKhoanChi adapter;
    private Button btnThemphanloai;
    private List<ModelListKhoanChi> listItems;
    String imageUrl;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    ImageView imageIcon;
    private DatabaseReference khoanChiRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ds_khoan_chi, container, false);
        listView = view.findViewById(R.id.listview_ds);
        btnThemphanloai = view.findViewById(R.id.btnThemphanloai);
        listItems = new ArrayList<>();

        adapter = new AdapterListKhoanChi(requireContext(), R.layout.khoanchi_list, listItems);
        listView.setAdapter(adapter);

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
        return view;
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

                            // Create a ModelListKhoanChi object
                            ModelListKhoanChi expense = new ModelListKhoanChi(imageUri, tenNhom);
                            adapter.add(expense);
                        }
                    } catch (ClassCastException e) {
                        Log.e("FirebaseData", "Failed to convert data to ModelListKhoanChi", e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseData", "Database error: " + databaseError.getMessage());
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

//        btnLuu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
        // Sự kiện khi nhấn nút "Lưu" trong dialog
//        btnLuu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Lấy đường dẫn ảnh từ ImageView (imageUrl)
//                // Tạo đối tượng ModelListKhoanChi với đường dẫn ảnh và tên nhóm
//                String tenNhom = edtThemnhom.getText().toString();
//
//                ModelListKhoanChi newItem = new ModelListKhoanChi(imageUrl, tenNhom);
//
//                // Đường dẫn tới node "khoanChi" trong Realtime Database
//                DatabaseReference khoanChiRef = FirebaseDatabase.getInstance().getReference().child("khoanChi");
//
//                // Push dữ liệu mới lên Realtime Database
//                String newItemKey = khoanChiRef.push().getKey();
//                khoanChiRef.child(newItemKey).setValue(newItem);
//
//                // Hiển thị thông báo và đóng dialog
//                Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });

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
                ModelListKhoanChi newItem = new ModelListKhoanChi(selectedImageUri.toString(), tenNhom);

                // Đường dẫn tới node "khoanChi" trong Realtime Database
                DatabaseReference khoanChiRef = FirebaseDatabase.getInstance().getReference().child("khoanChi");

                // Push dữ liệu mới lên Realtime Database
                String newItemKey = khoanChiRef.push().getKey();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Cập nhật ảnh được chọn vào ImageView
            imageIcon.setImageURI(selectedImageUri);

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
                listItems.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
