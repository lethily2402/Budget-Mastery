//package com.finalproject.budgetmastery.Activity;
//
//import static com.finalproject.budgetmastery.Service.OTPGenerators.generateOTP;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
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
//import com.finalproject.budgetmastery.Service.GmailSender;
//
//public class PincodeActivity extends AppCompatActivity {
//    String OTP;
//
//    Button btnAccept, btnResend;
//    EditText etCode;
//    String email, userCode;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_pincode);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        Intent intent = getIntent();
//        email = intent.getStringExtra("email");;
//        OTP = generateOTP();
//        sendOTPEmail(email, OTP);
//        etCode = findViewById(R.id.etCode);
//        btnAccept = findViewById(R.id.btnAccept);
//        btnResend = findViewById(R.id.btn_register);
//
//
//        btnAccept.setOnClickListener(v -> {
//            userCode = etCode.getText().toString();
//            if (OTP.equals(userCode)) {
//                Intent intent2 = new Intent(PincodeActivity.this, UpdatepassActivity.class);
//                startActivity(intent2);
//            } else {
//                Toast.makeText(this, "Sai", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btnResend.setOnClickListener(v -> {
//            ResendPassword();
//        });
//
//
//
//    }
//
//    private void sendOTPEmail(String email, String otp) {
//        String subject = "Budget Mastery";
//        String message = otp;
//
//        new Thread(() -> {
//            boolean emailSent = GmailSender.sendEmail(email, subject, message);
//            new Handler(Looper.getMainLooper()).post(() -> {
//                if (emailSent) {
//                    Toast.makeText(this, "Chúng tôi đã gửi mã OTP đến email của bạn!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Gửi mã OTP thất bại!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }).start();
//    }
//
//    private void ResendPassword() {
//        OTP = generateOTP();
//        sendOTPEmail(email, OTP);
//    }
//}


package com.finalproject.budgetmastery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finalproject.budgetmastery.R;
import com.finalproject.budgetmastery.Service.GmailSender;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class PincodeActivity extends AppCompatActivity {
    private String OTP;
    private Button btnAccept, btnResend;
    private EditText etCode;
    private String email, userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pincode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        OTP = generateOTP();
        sendOTPEmail(email, OTP);
        etCode = findViewById(R.id.etCode);
        btnAccept = findViewById(R.id.btnAccept);
        btnResend = findViewById(R.id.btn_resend);

        btnAccept.setOnClickListener(v -> {
            userCode = etCode.getText().toString();
            if (OTP != null && OTP.equals(userCode)) {
                Intent intent2 = new Intent(PincodeActivity.this, UpdatepassActivity.class);
                startActivity(intent2);
            } else {
                Toast.makeText(this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        btnResend.setOnClickListener(v -> {
            resendOTP(email);
        });
    }

    private void sendOTPEmail(String email, String otp) {
        String subject = "Budget Mastery";
        String message = otp;

        new Thread(() -> {
            boolean emailSent = GmailSender.sendEmail(email, subject, message);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (emailSent) {
                    Toast.makeText(this, "Chúng tôi đã gửi mã OTP đến email của bạn!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Gửi mã OTP thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void resendOTP(String email) {
        OTP = generateOTP();
        sendOTPEmail(email, OTP);
    }

    private String generateOTP() {
        // Your OTP generation logic here
        return "123456"; // Replace with your actual OTP generation code
    }
}
