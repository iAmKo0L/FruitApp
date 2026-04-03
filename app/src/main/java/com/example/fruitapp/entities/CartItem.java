package com.example.fruitapp.entities;

public class CartItem {
    public int orderDetailId;
    public int productId;
    public String productName;
    public double unitPrice;
    public int quantity;

    public double getSubtotal() {
        return unitPrice * quantity;
    }
}
