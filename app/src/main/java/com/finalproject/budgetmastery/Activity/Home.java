package com.finalproject.budgetmastery.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finalproject.budgetmastery.Model.ModelListHome;
import com.finalproject.budgetmastery.R;

public class Home extends AppCompatActivity {

    ListView listView;
    AdapterFrameHome adapterFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        listView = (ListView) findViewById(R.id.listview_ds);
        adapterFrame = new AdapterFrameHome(this,R.layout.home_list_item_by_date);


        adapterFrame.add(new ModelListHome("12/03/2003","2345341","abcac","123454"));
        adapterFrame.add(new ModelListHome("12/03/2003","2345341","abcac","123454"));
        adapterFrame.add(new ModelListHome("12/03/2003","2345341","abcac","123454"));
        adapterFrame.add(new ModelListHome("12/03/2003","2345341","abcac","123454"));
        adapterFrame.add(new ModelListHome("12/03/2003","2345341","abcac","123454"));

        listView.setAdapter(adapterFrame);



    }
}