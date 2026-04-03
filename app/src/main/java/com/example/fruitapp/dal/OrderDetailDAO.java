package com.example.fruitapp.dal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentmanager.entities.CartItem;
import com.example.studentmanager.entities.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDAO {
    @Insert
    long insert(OrderDetail detail);

    @Update
    void update(OrderDetail detail);

    @Query("SELECT * FROM order_details WHERE order_id = :orderId")
    List<OrderDetail> getByOrderId(int orderId);

    @Query("SELECT * FROM order_details WHERE order_id = :orderId AND product_id = :productId LIMIT 1")
    OrderDetail getByOrderAndProduct(int orderId, int productId);

    @Query("SELECT od.id as orderDetailId, od.product_id as productId, p.name as productName, " +
           "od.unit_price as unitPrice, od.quantity as quantity " +
           "FROM order_details od INNER JOIN products p ON od.product_id = p.id " +
           "WHERE od.order_id = :orderId")
    List<CartItem> getCartItems(int orderId);

    @Query("SELECT * FROM order_details WHERE id = :id")
    OrderDetail getById(int id);

    @Query("DELETE FROM order_details WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT COALESCE(SUM(unit_price * quantity), 0) FROM order_details WHERE order_id = :orderId")
    double getTotalByOrderId(int orderId);
}
