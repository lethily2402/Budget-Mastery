package com.finalproject.budgetmastery.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.budgetmastery.Adapter.AdapterListKhoanChi;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.Model.ModelListKhoanChi;
import com.finalproject.budgetmastery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DsKhoanChiFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private ListView listView;
    private AdapterListKhoanChi adapter;
    private Button btnThemphanloai;
    private List<ModelListKhoanChi> listItems;
    private ImageView imageIcon;

    private String selectedImageUri;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Phanloai");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ds_khoan_chi, container, false);



        listItems = new ArrayList<>();

        listView = view.findViewById(R.id.listview_ds);
        btnThemphanloai = view.findViewById(R.id.btnThemphanloai);

        adapter = new AdapterListKhoanChi(requireContext(), R.layout.khoanchi_list, listItems);
        listView.setAdapter(adapter);


//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                listItems.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    try {
//                        // Log the raw data
//                        Log.d("FirebaseData", snapshot.toString());
//
//                        // Retrieve data as a HashMap
//                        Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
//                        if (data != null) {
//                            String selectedImageUri = (String) data.get("selectedImageUri");
//                            String title = (String) data.get("title");
//
//                            // Create a ModelListHome object
//                            ModelListKhoanChi expense = new ModelListKhoanChi(Uri.parse(selectedImageUri), title);
//                            listItems.add(expense);
//                        }
//                    } catch (ClassCastException e) {
//                        Log.e("FirebaseData", "Failed to convert data to ModelListHome", e);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle possible errors
//                Log.e("FirebaseData", "Database error: " + databaseError.getMessage());
//            }
//        });


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

        imageIcon = dialog.findViewById(R.id.imageIcon);
        EditText edtThemnhom = dialog.findViewById(R.id.edtThemnhom);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnLuu = dialog.findViewById(R.id.btnLuu);

        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    String title = edtThemnhom.getText().toString();
                    if (!title.isEmpty()) {
                        ModelListKhoanChi khoanChi = new ModelListKhoanChi(selectedImageUri, title);
                        myRef.push().setValue(khoanChi).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("FirebaseSuccess", "Data saved successfully");
                                    Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Log.e("FirebaseError", "Failed to save data", task.getException());
                                    Toast.makeText(requireContext(), "Lưu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(requireContext(), "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();
            selectedImageUri = uri.toString();
            imageIcon.setImageURI(uri);
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

        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnXoa = dialog.findViewById(R.id.btnLuu);

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