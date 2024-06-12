package com.finalproject.budgetmastery.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.finalproject.budgetmastery.Adapter.AdapterListHome;
import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;
import com.finalproject.budgetmastery.TimKiem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class HomeFragment extends Fragment {

    ListView listView;
    AdapterListHome adapterFrame;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Setup ListView
        listView = view.findViewById(R.id.listview_ds);
        adapterFrame = new AdapterListHome(requireContext(), R.layout.home_list_item_by_date);
        listView.setAdapter(adapterFrame);

        // Firebase Realtime Database instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Payment");

        // Static ModelListHome object
        ModelListHome Payment = new ModelListHome("8/6/2024", "Thứ bảy", "Ăn uống", "50000");

        // Upload to Firebase
//        reference.push().setValue(Payment);


        // Add some data to the adapter
//        adapterFrame.add(new ModelListHome("8/6/2024", "Thứ bảy", "Ăn uống", "50000"));
//        adapterFrame.add(new ModelListHome("8/6/2024", "Thứ bảy", "Ăn uống", "50000"));
//        adapterFrame.add(new ModelListHome("8/6/2024", "Thứ bảy", "Ăn uống", "50000"));


        // Retrieve data from Firebase and update the adapter
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapterFrame.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        // Log the raw data
                        Log.d("FirebaseData", snapshot.toString());

                        // Retrieve data as a HashMap
                        Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                        if (data != null) {
                            String tvDate = (String) data.get("tvDate");
                            String tvDay = (String) data.get("tvDay");
                            String tvTitle = (String) data.get("tvTitle");
                            String tvAmount = (String) data.get("tvAmount");

                            // Create a ModelListHome object
                            ModelListHome expense = new ModelListHome(tvDate, tvDay, tvTitle, tvAmount);
                            adapterFrame.add(expense);
                        }
                    } catch (ClassCastException e) {
                        Log.e("FirebaseData", "Failed to convert data to ModelListHome", e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                Log.e("FirebaseData", "Database error: " + databaseError.getMessage());
            }
        });



        ImageView imgIcon = view.findViewById(R.id.imgSearch);
        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), TimKiem.class);
                startActivity(intent);
            }
        });

        return view;
    }
}