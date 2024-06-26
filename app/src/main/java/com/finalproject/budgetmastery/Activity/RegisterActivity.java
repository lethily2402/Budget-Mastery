package com.finalproject.budgetmastery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.budgetmastery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPass, edtRePass, edtSdt;
    private Button btnRegister;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize EditText and Button
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        edtRePass = findViewById(R.id.edt_re_pass);
        edtSdt = findViewById(R.id.edt_sdt);
        btnRegister = findViewById(R.id.btn_resend);

        // Initialize Firebase Authentication and Database
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Set click listener for btnRegister
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                String rePass = edtRePass.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(rePass) || TextUtils.isEmpty(sdt)) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(rePass)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(name, email, pass, sdt);
                }
            }
        });
    }

    private void registerUser(String name, String email, String password, String phone) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = auth.getCurrentUser().getUid();

                            DatabaseReference currentUserDb = databaseReference.child(userId); // Sử dụng userId trực tiếp làm tham chiếu

                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("phone", phone);
                            user.put("password", password);

                            currentUserDb.setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Đăng ký người dùng thành công", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Đăng ký người dùng thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký người dùng thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
