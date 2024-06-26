//package com.finalproject.budgetmastery.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.finalproject.budgetmastery.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class UpdatepassActivity extends AppCompatActivity {
//    private EditText edtUpPw, edtReUpPw;
//    private Button btnSave;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_updatepass);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        initUi();
//        initListener();
//    }
//
//    private void initUi() {
//        edtUpPw = findViewById(R.id.edt_up_pw);
//        edtReUpPw = findViewById(R.id.edt_re_up_pw);
//        btnSave = findViewById(R.id.btn_save);
//    }
//
//    private void initListener() {
//        btnSave.setOnClickListener(v -> {
//            String newPassword = edtUpPw.getText().toString().trim();
//            String confirmPassword = edtReUpPw.getText().toString().trim();
//
//            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
//                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!newPassword.equals(confirmPassword)) {
//                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            updatePassword(newPassword);
//        });
//    }
//
//    private void updatePassword(String newPassword) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            user.updatePassword(newPassword)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
//                            // Chuyển về màn hình đăng nhập
//                            Intent intent = new Intent(UpdatepassActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Toast.makeText(this, "Thay đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            Toast.makeText(this, "Không thể thay đổi mật khẩu. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
//            // Chuyển về màn hình đăng nhập
//            Intent intent = new Intent(UpdatepassActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
//}


package com.finalproject.budgetmastery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.budgetmastery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatepassActivity extends AppCompatActivity {
    private EditText edtUpPw, edtReUpPw;
    private Button btnSave;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass);

        // Initialize Firebase Authentication and Database
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        initUi();
        initListener();
    }

    private void initUi() {
        edtUpPw = findViewById(R.id.edt_up_pw);
        edtReUpPw = findViewById(R.id.edt_re_up_pw);
        btnSave = findViewById(R.id.btn_save);
    }

    private void initListener() {
        btnSave.setOnClickListener(v -> {
            String newPassword = edtUpPw.getText().toString().trim();
            String confirmPassword = edtReUpPw.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            updatePassword(newPassword);
        });
    }

    private void updatePassword(String newPassword) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            // Cập nhật dữ liệu lên Realtime Database (ví dụ: cập nhật mật khẩu)
                            updateUserPasswordInDatabase(newPassword);
                            // Chuyển về màn hình đăng nhập
                            Intent intent = new Intent(UpdatepassActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Thay đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Không thể thay đổi mật khẩu. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            // Chuyển về màn hình đăng nhập
            Intent intent = new Intent(UpdatepassActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateUserPasswordInDatabase(String newPassword) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference currentUserDb = databaseReference.child("users").child(userId);

            // Cập nhật mật khẩu mới vào Realtime Database
            currentUserDb.child("password").setValue(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Cập nhật mật khẩu trên Realtime Database thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Cập nhật mật khẩu trên Realtime Database thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
