
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

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

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            loadDataFromFirebase();
        } else {
            Toast.makeText(SearchActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
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
                performSearch(searchText);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = etSearch.getText().toString().toLowerCase().trim();
                performSearch(searchText);
                Toast.makeText(SearchActivity.this, "Searching for: " + searchText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataFromFirebase() {
        originalList.clear();
        databaseReference.child("addkhoanchi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelListHome item = snapshot.getValue(ModelListHome.class);
                    if (item != null) {
                        item.setTvDate(item.getTvDate());
                        originalList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
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
                        item.setTvDate(item.getTvDate());
                        originalList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Error loading data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch(String searchText) {
        currentFilteredList.clear();

        if (searchText.isEmpty()) {
            currentFilteredList.addAll(originalList);
        } else {
            String[] keywords = searchText.split("\\s+");

            for (ModelListHome item : originalList) {
                boolean match = false;

                for (String keyword : keywords) {
                    String regex = ".*" + keyword.toLowerCase() + ".*";
                    if (item.getTvAmount().toLowerCase().matches(regex)
                            || item.getTvTitle().toLowerCase().matches(regex)
                            || item.getTvDate().toLowerCase().matches(regex)) {
                        match = true;
                        break;
                    }
                }
                if (match) {
                    currentFilteredList.add(item);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}
