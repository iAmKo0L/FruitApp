package com.example.fruitapp.dal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studentmanager.entities.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Insert
    long insert(Product product);

    @Query("SELECT * FROM products")
    List<Product> getAll();

    @Query("SELECT * FROM products WHERE id = :id")
    Product getById(int id);

    @Query("SELECT * FROM products WHERE category_id = :categoryId")
    List<Product> getByCategoryId(int categoryId);

    @Query("SELECT COUNT(*) FROM products")
    int count();

    @Query("UPDATE products SET stock = stock - :delta WHERE id = :id AND stock >= :delta")
    void decreaseStock(int id, int delta);

    @Query("UPDATE products SET stock = stock + :delta WHERE id = :id")
    void increaseStock(int id, int delta);
}
