package com.example.fruitapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitapp.R;
import com.example.fruitapp.adapters.CartItemAdapter;
import com.example.fruitapp.dal.AppDB;
import com.example.fruitapp.entities.CartItem;
import com.example.fruitapp.entities.Order;
import com.example.fruitapp.entities.OrderDetail;
import com.example.fruitapp.utils.PriceUtils;
import com.example.fruitapp.utils.SessionManager;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private AppDB db;
    private RecyclerView recyclerView;
    private TextView tvTotal, tvEmpty;
    private View layoutFooter;
    private Order currentPendingOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hóa Đơn Hiện Tại");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDB.getInstance(this);
        sessionManager = new SessionManager(this);

        recyclerView = findViewById(R.id.recyclerCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvTotal = findViewById(R.id.tvCartTotal);
        tvEmpty = findViewById(R.id.tvCartEmpty);
        layoutFooter = findViewById(R.id.layoutCartFooter);

        if (!sessionManager.isLoggedIn()) {
            showEmpty("Vui lòng đăng nhập để xem hóa đơn");
            return;
        }

        Button btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(v -> showCheckoutDialog());

        Button btnContinue = findViewById(R.id.btnContinueShopping);
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.isLoggedIn()) loadCart();
    }

    private void loadCart() {
        int userId = sessionManager.getUserId();
        currentPendingOrder = db.orderDAO().getPendingOrderByUserId(userId);

        if (currentPendingOrder == null) {
            showEmpty("Hóa đơn trống. Hãy thêm sản phẩm!");
            return;
        }

        List<CartItem> items = db.orderDetailDAO().getCartItems(currentPendingOrder.id);
        if (items.isEmpty()) {
            showEmpty("Hóa đơn trống. Hãy thêm sản phẩm!");
            return;
        }

        tvEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        layoutFooter.setVisibility(View.VISIBLE);

        final int orderId = currentPendingOrder.id;

        recyclerView.setAdapter(new CartItemAdapter(this, items, new CartItemAdapter.CartItemListener() {
            @Override
            public void onIncrease(CartItem item) {
                OrderDetail detail = db.orderDetailDAO().getById(item.orderDetailId);
                if (detail != null) {
                    // Check stock before increasing
                    com.example.studentmanager.entities.Product p = db.productDAO().getById(item.productId);
                    if (p != null && p.stock < 1) {
                        Toast.makeText(CartActivity.this, "Sản phẩm đã hết hàng!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    detail.quantity++;
                    db.orderDetailDAO().update(detail);
                    db.productDAO().decreaseStock(item.productId, 1);
                    refreshTotal(orderId);
                    loadCart();
                }
            }

            @Override
            public void onDecrease(CartItem item) {
                OrderDetail detail = db.orderDetailDAO().getById(item.orderDetailId);
                if (detail != null) {
                    if (detail.quantity > 1) {
                        detail.quantity--;
                        db.orderDetailDAO().update(detail);
                        db.productDAO().increaseStock(item.productId, 1);
                    } else {
                        db.orderDetailDAO().deleteById(detail.id);
                        db.productDAO().increaseStock(item.productId, detail.quantity);
                    }
                    refreshTotal(orderId);
                    loadCart();
                }
            }

            @Override
            public void onDelete(CartItem item) {
                new AlertDialog.Builder(CartActivity.this)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc muốn xóa " + item.productName + " khỏi hóa đơn?")
                    .setPositiveButton("Xóa", (d, w) -> {
                        // Restore stock for the full quantity being removed
                        db.productDAO().increaseStock(item.productId, item.quantity);
                        db.orderDetailDAO().deleteById(item.orderDetailId);
                        refreshTotal(orderId);
                        loadCart();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            }
        }));

        refreshTotal(orderId);
    }

    private void refreshTotal(int orderId) {
        double total = db.orderDetailDAO().getTotalByOrderId(orderId);
        db.orderDAO().updateTotal(orderId, total);
        tvTotal.setText("Tổng tiền: " + PriceUtils.format(total));
        if (currentPendingOrder != null) currentPendingOrder.totalAmount = total;
    }

    private void showEmpty(String message) {
        tvEmpty.setText(message);
        tvEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutFooter.setVisibility(View.GONE);
    }

    private void showCheckoutDialog() {
        if (currentPendingOrder == null) return;
        double total = db.orderDetailDAO().getTotalByOrderId(currentPendingOrder.id);

        new AlertDialog.Builder(this)
            .setTitle("Xác nhận thanh toán")
            .setMessage("Bạn xác nhận thanh toán " + PriceUtils.format(total) + "?")
            .setPositiveButton("Thanh Toán", (dialog, which) -> checkout())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void checkout() {
        if (currentPendingOrder == null) return;
        double total = db.orderDetailDAO().getTotalByOrderId(currentPendingOrder.id);
        db.orderDAO().updateTotal(currentPendingOrder.id, total);
        db.orderDAO().markAsPaid(currentPendingOrder.id);
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, InvoiceActivity.class);
        intent.putExtra("order_id", currentPendingOrder.id);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
