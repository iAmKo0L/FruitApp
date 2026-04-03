package com.example.fruitapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitapp.R;
import com.example.fruitapp.adapters.ProductAdapter;
import com.example.fruitapp.dal.AppDB;
import com.example.fruitapp.entities.Product;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvCount;
    private int categoryId = -1;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryId = getIntent().getIntExtra("category_id", -1);
        categoryName = getIntent().getStringExtra("category_name");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(categoryName != null ? categoryName : "Tất Cả Sản Phẩm");
        }

        recyclerView = findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        tvCount = findViewById(R.id.tvProductCount);

        loadProducts();
    }

    private void loadProducts() {
        AppDB db = AppDB.getInstance(this);
        List<Product> products = (categoryId != -1)
                ? db.productDAO().getByCategoryId(categoryId)
                : db.productDAO().getAll();

        recyclerView.setAdapter(new ProductAdapter(this, products));
        tvCount.setText("Tìm thấy " + products.size() + " sản phẩm");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
