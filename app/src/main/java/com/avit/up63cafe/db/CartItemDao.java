package com.avit.up63cafe.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.avit.up63cafe.ui.dashboard.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {

    @Insert
    void insert(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Query("SELECT * FROM cart_table")
    LiveData<List<CartItem>> getAllItems();

    @Query("DELETE FROM cart_table")
    void deleteAll();
}
