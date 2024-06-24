
package com.finalproject.budgetmastery.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.budgetmastery.Adapter.AdapterSearch;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText etSearch;
    private ImageButton imageButton;
    private ListView lvThuchi;
    private AdapterSearch adapter;
    private List<ModelListHome> originalList;
    private List<ModelListHome> currentFilteredList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);

        // Khởi tạo biến và adapter
        etSearch = findViewById(R.id.etSearch);
        imageButton = findViewById(R.id.imageButton);
        lvThuchi = findViewById(R.id.lvThuchi);
        originalList = new ArrayList<>();
        currentFilteredList = new ArrayList<>();
        adapter = new AdapterSearch(this, R.layout.home_list_item_by_date, currentFilteredList);
        lvThuchi.setAdapter(adapter);

        // Thiết lập Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Tải dữ liệu từ Firebase
        loadDataFromFirebase();

        // Xử lý tìm kiếm khi thay đổi nội dung trong EditText
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase().trim();
                performSearch(searchText); // Thực hiện tìm kiếm với từ khóa mới
            }
        });

        // Xử lý sự kiện khi ImageButton được click
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = etSearch.getText().toString().toLowerCase().trim();
                performSearch(searchText); // Thực hiện tìm kiếm với từ khóa từ EditText

                // Hiển thị thông báo khi nhấn nút
                Toast.makeText(SearchActivity.this, "Searching for: " + searchText, Toast.LENGTH_SHORT).show();
            }
        });
    }

        private void loadDataFromFirebase() {
        databaseReference.child("addkhoanchi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelListHome item = snapshot.getValue(ModelListHome.class);
                    if (item != null) {
                        item.setTvDate(item.getTvDate()); // Đảm bảo tvDay được tính toán và lưu trữ
                        originalList.add(item);
                    }
                }
                adapter.notifyDataSetChanged(); // Cập nhật adapter sau khi tải dữ liệu
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Error loading data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("addkhoanthu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelListHome item = snapshot.getValue(ModelListHome.class);
                    if (item != null) {
                        item.setTvDate(item.getTvDate()); // Đảm bảo tvDay được tính toán và lưu trữ
                        originalList.add(item);
                    }
                }
                adapter.notifyDataSetChanged(); // Cập nhật adapter sau khi tải dữ liệu
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Error loading data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch(String searchText) {
        currentFilteredList.clear(); // Xóa dữ liệu trong danh sách lọc hiện tại

        if (searchText.isEmpty()) {
            currentFilteredList.addAll(originalList); // Nếu không có từ khóa tìm kiếm, hiển thị tất cả các mục
        } else {
            String[] keywords = searchText.split("\\s+"); // Chia chuỗi nhập thành các từ

            for (ModelListHome item : originalList) {
                boolean match = false;

                for (String keyword : keywords) {
                    // Sử dụng regex cho tìm kiếm linh hoạt hơn
                    String regex = ".*" + keyword.toLowerCase() + ".*";
                    if (item.getTvAmount().toLowerCase().matches(regex)
                            || item.getTvTitle().toLowerCase().matches(regex)
                            || item.getTvDate().toLowerCase().matches(regex)) {
                        match = true;
                        break; // Nếu có ít nhất một từ trùng khớp thì thoát
                    }
                }

                if (match) {
                    currentFilteredList.add(item); // Thêm mục vào danh sách lọc hiện tại
                }
            }
        }

        adapter.notifyDataSetChanged(); // Cập nhật adapter sau khi lọc dữ liệu
    }
}

