package com.example.studentmanager.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studentmanager.R;
import com.example.studentmanager.dal.AppDB;
import com.example.studentmanager.entities.User;
import com.example.studentmanager.utils.SessionManager;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etConfirmPassword, etFullName, etEmail;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Đăng Ký");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sessionManager = new SessionManager(this);
        etFullName = findViewById(R.id.etRegFullName);
        etUsername = findViewById(R.id.etRegUsername);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etRegConfirmPassword);

        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnRegister.setOnClickListener(v -> attemptRegister());
        btnGoToLogin.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void attemptRegister() {
        String username = etUsername.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ và tên");
            etFullName.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return;
        }
        if (username.length() < 3) {
            etUsername.setError("Tên đăng nhập tối thiểu 3 ký tự");
            etUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Mật khẩu tối thiểu 6 ký tự");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        AppDB db = AppDB.getInstance(this);
        if (db.userDAO().existsByUsername(username) > 0) {
            etUsername.setError("Tên đăng nhập đã tồn tại");
            etUsername.requestFocus();
            return;
        }

        User newUser = new User(username, password, fullName, email);
        long userId = db.userDAO().insert(newUser);
        if (userId > 0) {
            sessionManager.saveLoginSession((int) userId, username, fullName);
            Toast.makeText(this, "Đăng ký thành công! Chào mừng " + fullName, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
