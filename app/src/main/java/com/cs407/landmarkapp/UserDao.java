package com.cs407.landmarkapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM users WHERE user_id = :userId")
    LiveData<User> getUserById(int userId);

    @Query("SELECT * FROM users WHERE user_id IN (:userIds)")
    LiveData<List<User>> getUsersByIds(List<Integer> userIds);

    @Query("SELECT * FROM users where username = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User login(String username, String password);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int checkUsernameExists(String username);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int checkEmailExists(String email);

    @Query("DELETE FROM users WHERE user_id = :userId")
    void deleteUserById(int userId);
}
