package com.finalproject.budgetmastery.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.finalproject.budgetmastery.Model.ModelListKhoanThu;
import com.finalproject.budgetmastery.R;

import java.util.ArrayList;
import java.util.List;


public class DsKhoanThuFragment extends Fragment {
    private ListView listView;
    private AdapterListKhoanThu adapter;
    private Button btnThemphanloai;
    private List<ModelListKhoanThu> listItems;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ds_khoan_thu, container, false);
        listView = view.findViewById(R.id.listview_ds);
        btnThemphanloai = view.findViewById(R.id.btnThemphanloai);
        listItems = new ArrayList<>();
        listItems.add(new ModelListKhoanThu(R.drawable.tienluong, "Tiền lương"));
        listItems.add(new ModelListKhoanThu(R.drawable.tienluong, "Tiền lương"));
        listItems.add(new ModelListKhoanThu(R.drawable.tienluong, "Tiền lương"));
        listItems.add(new ModelListKhoanThu(R.drawable.tienluong, "Tiền lương"));
        listItems.add(new ModelListKhoanThu(R.drawable.tienluong, "Tiền lương"));

        adapter = new AdapterListKhoanThu(requireContext(), R.layout.khoanthu_list , listItems);
        listView.setAdapter(adapter);
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
        ImageView imageView = dialog.findViewById(R.id.imageView);
        EditText edtThemnhom = dialog.findViewById(R.id.edtThemnhom);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnLuu = dialog.findViewById(R.id.btnLuu);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        dialog.show();
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