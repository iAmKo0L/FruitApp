package com.example.fruitapp.dal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentmanager.entities.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders WHERE user_id = :userId AND status = 'PENDING' LIMIT 1")
    Order getPendingOrderByUserId(int userId);

    @Query("SELECT * FROM orders WHERE id = :id")
    Order getById(int id);

    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY order_date DESC")
    List<Order> getByUserId(int userId);

    @Query("UPDATE orders SET total_amount = :total WHERE id = :orderId")
    void updateTotal(int orderId, double total);

    @Query("UPDATE orders SET status = 'PAID' WHERE id = :orderId")
    void markAsPaid(int orderId);
}
