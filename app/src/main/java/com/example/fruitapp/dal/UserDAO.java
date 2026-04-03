package com.example.studentmanager.dal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studentmanager.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id = :id")
    User getById(int id);

    @Query("SELECT COUNT(*) FROM users")
    int count();

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int existsByUsername(String username);
}
