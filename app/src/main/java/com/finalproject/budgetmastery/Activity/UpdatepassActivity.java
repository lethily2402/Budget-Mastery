package com.finalproject.budgetmastery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finalproject.budgetmastery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatepassActivity extends AppCompatActivity {
    private EditText edtUpPw, edtReUpPw;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_updatepass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
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
}