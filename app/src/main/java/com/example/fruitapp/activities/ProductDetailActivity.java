package com.example.fruitapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fruitapp.R;
import com.example.fruitapp.dal.AppDB;
import com.example.fruitapp.entities.Category;
import com.example.fruitapp.entities.Order;
import com.example.fruitapp.entities.OrderDetail;
import com.example.fruitapp.entities.Product;
import com.example.fruitapp.utils.PriceUtils;
import com.example.fruitapp.utils.SessionManager;

public class ProductDetailActivity extends AppCompatActivity {
    private Product product;
    private SessionManager sessionManager;
    private AppDB db;
    private int currentQuantity = 1;
    private TextView tvQuantity;

    private static final int[] PLACEHOLDER_COLORS = {
        0xFF2C3E50, 0xFF1A5276, 0xFF1E8449, 0xFF784212,
        0xFF4A235A, 0xFF0E6655, 0xFF1B4F72, 0xFF922B21
    };

    private final ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK) {
                addToCart(currentQuantity);
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = AppDB.getInstance(this);
        sessionManager = new SessionManager(this);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) { finish(); return; }

        product = db.productDAO().getById(productId);
        if (product == null) { finish(); return; }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi Tiết Sản Phẩm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvQuantity = findViewById(R.id.tvQuantity);
        Button btnDecrease = findViewById(R.id.btnDecreaseQty);
        Button btnIncrease = findViewById(R.id.btnIncreaseQty);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);

        btnDecrease.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                tvQuantity.setText(String.valueOf(currentQuantity));
            }
        });

        btnIncrease.setOnClickListener(v -> {
            if (currentQuantity < product.stock) {
                currentQuantity++;
                tvQuantity.setText(String.valueOf(currentQuantity));
            } else {
                Toast.makeText(this, "Đã đạt số lượng tồn kho tối đa", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddToCart.setOnClickListener(v -> handleAddToCart());

        bindProductDetails();
    }

    private void bindProductDetails() {
        // Placeholder color based on product id
        int color = PLACEHOLDER_COLORS[product.id % PLACEHOLDER_COLORS.length];
        FrameLayout flImage = findViewById(R.id.flProductImage);
        flImage.setBackgroundColor(color);

        TextView tvInitial = findViewById(R.id.tvProductInitial);
        tvInitial.setText(product.name.length() > 0 ? String.valueOf(product.name.charAt(0)).toUpperCase() : "?");

        ((TextView) findViewById(R.id.tvProductName)).setText(product.name);
        ((TextView) findViewById(R.id.tvProductPrice)).setText(PriceUtils.format(product.price));
        ((TextView) findViewById(R.id.tvProductStock)).setText(getString(R.string.stock_label, product.stock));
        ((TextView) findViewById(R.id.tvProductDescription)).setText(product.description);

        String categoryText = getString(R.string.status); // Fallback
        if (product.categoryId != null) {
            Category cat = db.categoryDAO().getById(product.categoryId);
            if (cat != null) categoryText = cat.name;
        }
        ((TextView) findViewById(R.id.tvProductCategory)).setText(categoryText);

        tvQuantity.setText(String.valueOf(currentQuantity));
    }

    private void handleAddToCart() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm sản phẩm vào hóa đơn", Toast.LENGTH_SHORT).show();
            loginLauncher.launch(new Intent(this, LoginActivity.class));
        } else {
            addToCart(currentQuantity);
        }
    }

    private void addToCart(int quantity) {
        int userId = sessionManager.getUserId();

        // Reload product to get latest stock
        product = db.productDAO().getById(product.id);
        if (product == null) { finish(); return; }

        if (product.stock < quantity) {
            Toast.makeText(this, "Số lượng tồn kho không đủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        Order pendingOrder = db.orderDAO().getPendingOrderByUserId(userId);
        if (pendingOrder == null) {
            long orderId = db.orderDAO().insert(new Order(userId, System.currentTimeMillis(), "PENDING"));
            pendingOrder = db.orderDAO().getById((int) orderId);
        }

        OrderDetail existing = db.orderDetailDAO().getByOrderAndProduct(pendingOrder.id, product.id);
        if (existing != null) {
            existing.quantity += quantity;
            db.orderDetailDAO().update(existing);
        } else {
            db.orderDetailDAO().insert(new OrderDetail(pendingOrder.id, product.id, quantity, product.price));
        }

        // Decrease stock
        db.productDAO().decreaseStock(product.id, quantity);

        double total = db.orderDetailDAO().getTotalByOrderId(pendingOrder.id);
        db.orderDAO().updateTotal(pendingOrder.id, total);

        Toast.makeText(this,
            "Đã thêm " + quantity + " x " + product.name + " vào hóa đơn!",
            Toast.LENGTH_SHORT).show();

        // Navigate to cart
        startActivity(new Intent(this, CartActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
