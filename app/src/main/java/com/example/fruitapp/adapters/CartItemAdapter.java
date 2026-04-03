package com.example.fruitapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanager.R;
import com.example.studentmanager.entities.CartItem;
import com.example.studentmanager.utils.PriceUtils;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {
    private final Context context;
    private final List<CartItem> items;
    private final CartItemListener listener;

    public interface CartItemListener {
        void onIncrease(CartItem item);
        void onDecrease(CartItem item);
        void onDelete(CartItem item);
    }

    public CartItemAdapter(Context context, List<CartItem> items, CartItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);

        holder.tvName.setText(item.productName);
        holder.tvQuantity.setText(String.valueOf(item.quantity));
        holder.tvUnitPrice.setText(PriceUtils.format(item.unitPrice));
        holder.tvSubtotal.setText(PriceUtils.format(item.getSubtotal()));

        if (listener != null) {
            holder.btnIncrease.setVisibility(View.VISIBLE);
            holder.btnDecrease.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnIncrease.setOnClickListener(v -> listener.onIncrease(item));
            holder.btnDecrease.setOnClickListener(v -> listener.onDecrease(item));
            holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
        } else {
            holder.btnIncrease.setVisibility(View.GONE);
            holder.btnDecrease.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvUnitPrice, tvSubtotal;
        Button btnIncrease, btnDecrease;
        ImageButton btnDelete;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartItemName);
            tvQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            tvUnitPrice = itemView.findViewById(R.id.tvCartItemUnitPrice);
            tvSubtotal = itemView.findViewById(R.id.tvCartItemSubtotal);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
