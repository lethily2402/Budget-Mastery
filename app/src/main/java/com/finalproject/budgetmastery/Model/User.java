package com.finalproject.budgetmastery.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Locale;

public class User {
    private String name;
    private String email;
    private String password;
    private String rePass;
    private String sdt;


    // Constructor mặc định cần thiết cho Firebase
    public User() {
    }

    public User(String name, String email, String password, String rePass, String sdt) {
        this.name = name;
        this.email = email;
        this.password = hashPassword(password);
        this.rePass = hashPassword(rePass);
        this.sdt = sdt;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Các getter và setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public String getRePass() {
        return rePass;
    }

    public void setRePass(String rePass) {
        this.rePass = hashPassword(rePass);
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    // Phương thức băm mật khẩu
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
