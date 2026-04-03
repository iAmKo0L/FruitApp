package com.example.fruitapp.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "orders",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE
    ),
    indices = @Index("user_id")
)
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "order_date")
    public long orderDate;

    @ColumnInfo(name = "status")
    public String status; // "PENDING" or "PAID"

    @ColumnInfo(name = "total_amount")
    public double totalAmount;

    public Order() {}

    public Order(int userId, long orderDate, String status) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = 0.0;
    }
}
