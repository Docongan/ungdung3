package com.example.myapplicationth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
    }

    private void setupListeners() {
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> performLogin());

        tvRegister.setOnClickListener(v -> 
            Toast.makeText(this, getString(R.string.register_toast), Toast.LENGTH_SHORT).show()
        );
    }

    private void performLogin() {
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập tài khoản!");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu!");
            etPassword.requestFocus();
            return;
        }

        // Kiểm tra chính xác tài khoản và mật khẩu
        if ("abc123".equals(username) && "abc".equals(password)) {
            navigateToMain("Nguyễn Văn A", "doc123", username);
        } else {
            Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            etPassword.setError("Tên đăng nhập hoặc mật khẩu không chính xác!");
            etPassword.requestFocus();
        }
    }

    private void navigateToMain(String displayName, String docId, String username) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("EXTRA_DISPLAY_NAME", displayName);
        intent.putExtra("EXTRA_DOC_ID", docId);
        intent.putExtra("EXTRA_USERNAME", username);
        startActivity(intent);
        finish();
    }
}
