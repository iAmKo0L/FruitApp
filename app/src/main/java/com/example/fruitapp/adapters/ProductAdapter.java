package com.example.fruitapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitapp.R;
import com.example.fruitapp.activities.ProductDetailActivity;
import com.example.fruitapp.entities.Product;
import com.example.fruitapp.utils.PriceUtils;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context context;
    private final List<Product> products;

    // Bảng màu tối giản, ít màu mè
    private static final int[] PLACEHOLDER_COLORS = {
        0xFF2C3E50,
        0xFF1A5276,
        0xFF1E8449,
        0xFF784212,
        0xFF4A235A,
        0xFF0E6655,
        0xFF1B4F72,
        0xFF922B21,
    };

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        int color = PLACEHOLDER_COLORS[product.id % PLACEHOLDER_COLORS.length];
        holder.flImage.setBackgroundColor(color);
        holder.tvInitial.setText(product.name.length() > 0
            ? String.valueOf(product.name.charAt(0)).toUpperCase() : "?");

        holder.tvName.setText(product.name);
        holder.tvPrice.setText(PriceUtils.format(product.price));
        holder.tvStock.setText("Còn: " + product.stock + " sản phẩm");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flImage;
        TextView tvInitial, tvName, tvPrice, tvStock;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            flImage = itemView.findViewById(R.id.flProductImage);
            tvInitial = itemView.findViewById(R.id.tvProductInitial);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvStock = itemView.findViewById(R.id.tvProductStock);
        }
    }
}
