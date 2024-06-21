package com.finalproject.budgetmastery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.budgetmastery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnContinue;
    private TextView tvDangky, tvForgotpw;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        initListener();

        // Khởi tạo Firebase Auth
        auth = FirebaseAuth.getInstance();
    }

    private void initListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignIn();
            }
        });
        tvForgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword();
            }
        });
        tvDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onClickSignIn() {
        String email = edtUsername.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công, chuyển đến MainActivity
                            Log.d("Login", "Đăng nhập thành công.");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            // Đóng LoginActivity
                            finish(); // hoặc finishAffinity();
                        } else {
                            // Đăng nhập thất bại, hiển thị thông báo lỗi
                            Log.e("Login", "Đăng nhập thất bại.", task.getException());
                            Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra lại email và mật khẩu.", Toast.LENGTH_SHORT).show();

                            // In chi tiết lỗi ra logcat
                            if (task.getException() != null) {
                                Log.e("LoginError", task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    private void onClickForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ResetpassActivity.class);
        startActivity(intent);
    }

    private void initUi() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_pword);
        btnContinue = findViewById(R.id.btnAccept);
        tvDangky = findViewById(R.id.txt_l_dangky);
        tvForgotpw = findViewById(R.id.tv_forgotpw);
    }
}