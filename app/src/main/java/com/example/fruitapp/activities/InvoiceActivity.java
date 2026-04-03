package com.example.fruitapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanager.MainActivity;
import com.example.studentmanager.R;
import com.example.studentmanager.adapters.CartItemAdapter;
import com.example.studentmanager.dal.AppDB;
import com.example.studentmanager.entities.CartItem;
import com.example.studentmanager.entities.Order;
import com.example.studentmanager.entities.User;
import com.example.studentmanager.utils.PriceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hóa Đơn");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int orderId = getIntent().getIntExtra("order_id", -1);
        if (orderId == -1) { finish(); return; }

        AppDB db = AppDB.getInstance(this);
        Order order = db.orderDAO().getById(orderId);
        if (order == null) { finish(); return; }

        User user = db.userDAO().getById(order.userId);
        List<CartItem> items = db.orderDetailDAO().getCartItems(orderId);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        ((TextView) findViewById(R.id.tvInvoiceId)).setText("#" + order.id);
        ((TextView) findViewById(R.id.tvInvoiceUser)).setText(user != null ? user.fullName : "—");
        ((TextView) findViewById(R.id.tvInvoiceDate)).setText(sdf.format(new Date(order.orderDate)));
        ((TextView) findViewById(R.id.tvInvoiceStatus)).setText("ĐÃ THANH TOÁN");
        ((TextView) findViewById(R.id.tvInvoiceTotal)).setText(PriceUtils.format(order.totalAmount));

        RecyclerView recyclerView = findViewById(R.id.recyclerInvoice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Pass null listener so CartItemAdapter hides quantity controls (read-only view)
        recyclerView.setAdapter(new CartItemAdapter(this, items, null));

        ((Button) findViewById(R.id.btnContinueShopping)).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
