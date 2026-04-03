package com.example.fruitapp.dal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studentmanager.entities.Category;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert
    long insert(Category category);

    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE id = :id")
    Category getById(int id);

    @Query("SELECT COUNT(*) FROM categories")
    int count();
}
