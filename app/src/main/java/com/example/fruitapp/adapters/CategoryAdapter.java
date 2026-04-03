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

import com.example.studentmanager.R;
import com.example.studentmanager.activities.ProductListActivity;
import com.example.studentmanager.entities.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final Context context;
    private final List<Category> categories;

    // Màu tối giản, đồng nhất
    private static final int[] PLACEHOLDER_COLORS = {
        0xFF2C3E50,
        0xFF1A5276,
        0xFF1E8449,
        0xFF784212,
        0xFF4A235A,
    };

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        int color = PLACEHOLDER_COLORS[position % PLACEHOLDER_COLORS.length];
        holder.flIcon.setBackgroundColor(color);
        holder.tvInitial.setText(category.name.length() > 0
            ? String.valueOf(category.name.charAt(0)).toUpperCase() : "?");

        holder.tvName.setText(category.name);
        holder.tvDescription.setText(category.description);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra("category_id", category.id);
            intent.putExtra("category_name", category.name);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flIcon;
        TextView tvInitial, tvName, tvDescription;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            flIcon = itemView.findViewById(R.id.flProductImage);
            tvInitial = itemView.findViewById(R.id.tvCategoryInitial);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
        }
    }
}
