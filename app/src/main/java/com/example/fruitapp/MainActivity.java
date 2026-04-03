package com.example.fruitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitapp.activities.CartActivity;
import com.example.fruitapp.activities.CategoryListActivity;
import com.example.fruitapp.activities.LoginActivity;
import com.example.fruitapp.activities.ProductListActivity;
import com.example.fruitapp.dal.AppDB;
import com.example.fruitapp.utils.SessionManager;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView tvWelcome;
    private Button btnLogin, btnLogout, btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DB và seed dữ liệu lần đầu
        AppDB.getInstance(this);

        sessionManager = new SessionManager(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);
        btnCart = findViewById(R.id.btnCart);

        findViewById(R.id.btnProducts).setOnClickListener(v ->
                startActivity(new Intent(this, ProductListActivity.class)));

        findViewById(R.id.btnCategories).setOnClickListener(v ->
                startActivity(new Intent(this, CategoryListActivity.class)));

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        btnLogout.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (d, w) -> {
                        sessionManager.logout();
                        updateUI();
                    })
                    .setNegativeButton("Hủy", null)
                    .show());

        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (sessionManager.isLoggedIn()) {
            tvWelcome.setText("Xin chào, " + sessionManager.getFullName() + "!");
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            btnCart.setVisibility(View.VISIBLE);
        } else {
            tvWelcome.setText("Vui lòng đăng nhập để mua hàng");
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
            btnCart.setVisibility(View.GONE);
        }
    }
}
