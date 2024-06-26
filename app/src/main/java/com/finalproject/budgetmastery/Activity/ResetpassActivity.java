package com.finalproject.budgetmastery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finalproject.budgetmastery.R;

public class ResetpassActivity extends AppCompatActivity {

    Button btnContious;
    EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resetpass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnContious = findViewById(R.id.btnContinous);
        etEmail = findViewById(R.id.etEmail);

        btnContious.setOnClickListener(v -> {
            Intent intent = new Intent(ResetpassActivity.this, PincodeActivity.class);
            intent.putExtra("email", etEmail.getText().toString());
            startActivity(intent);
        });

    }
}