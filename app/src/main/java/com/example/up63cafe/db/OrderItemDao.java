package com.example.up63cafe.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.up63cafe.ui.orders.OrdersItem;

import java.util.List;

@Dao
public interface OrderItemDao {

    @Insert
    void insert(OrdersItem ordersItem);

    @Update
    void update(OrdersItem ordersItem);

    @Query("SELECT * FROM order_table ORDER BY _id DESC")
    LiveData<List<OrdersItem>> getAllItems();

    @Query("UPDATE order_table SET status = :status WHERE order_id = :orderId")
    int UpdateOrder(String orderId , int status);

}
