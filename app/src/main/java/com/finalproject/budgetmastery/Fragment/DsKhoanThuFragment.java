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
import com.finalproject.budgetmastery.Adapter.AdapterListKhoanThu;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.Model.ModelListKhoanThu;
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

public class DsKhoanThuFragment extends Fragment {
    private ListView listView;
    private AdapterListKhoanThu adapter;
    private Button btnThemphanloai;
    private List<ModelListKhoanThu> listItems;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    ImageView imageIcon;
    private DatabaseReference khoanThuRef;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ds_khoan_thu, container, false);
        listView = view.findViewById(R.id.listview_ds);
        btnThemphanloai = view.findViewById(R.id.btnThemphanloai);
        listItems = new ArrayList<>();

        adapter = new AdapterListKhoanThu(requireContext(), R.layout.khoanthu_list, listItems);
        listView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelListKhoanThu selectedItem = listItems.get(position);
                showIncomeDialog(selectedItem.getTxt_title());
            }
        });
        return view;
    }
    private void showIncomeDialog(String category) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chitiet);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
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

        List<ModelListHome> incomeList = new ArrayList<>();
        AdapterExpense adapter = new AdapterExpense(requireContext(), R.layout.home_list_item_by_date, incomeList);
        listViewExpenses.setAdapter(adapter);

        // Get UID of current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }
        String userId = currentUser.getUid();
        DatabaseReference userKhoanChiRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("chitietkhoanthu").child(category).child("income");

        userKhoanChiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                incomeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelListHome expense = snapshot.getValue(ModelListHome.class);
                    if (expense != null) {
                        incomeList.add(expense);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load expenses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
                            String key = snapshot.getKey();

                            if (imageUri != null && tenNhom != null) {
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
                if (selectedImageUri == null) {
                    Toast.makeText(requireContext(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }


                ModelListKhoanThu newItem = new ModelListKhoanThu(selectedImageUri.toString(), tenNhom, null);

                DatabaseReference khoanThuRef = FirebaseDatabase.getInstance().getReference().child("khoanThu");


                String newItemKey = khoanThuRef.push().getKey();
                newItem.setKey(newItemKey);
                khoanThuRef.child(newItemKey).setValue(newItem)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(requireContext(), "Lưu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                ModelListKhoanThu item = listItems.get(position);
                deleteItemFromFirebase(item.getKey());
                listItems.remove(position);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
