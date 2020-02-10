package com.example.up63cafe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.up63cafe.db.CartItemRepository;
import com.example.up63cafe.db.OrderItemRepository;
import com.example.up63cafe.ui.dashboard.CartItem;
import com.example.up63cafe.ui.orders.OrdersItem;

import java.util.List;

public class HomeActivityViewModel extends AndroidViewModel {

    private OrderItemRepository repository;

    private CartItemRepository cartItemRepository;
    private LiveData<List<CartItem>> allItems;

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderItemRepository(application);
        cartItemRepository = new CartItemRepository(application);
        allItems = cartItemRepository.getAllItems();
    }

    public void clearCart(){
        cartItemRepository.deleteAll();
    }

    public LiveData<List<CartItem>> getAllItems() {
        return allItems;
    }

    public void insert(OrdersItem ordersItem){
        repository.insert(ordersItem);
    }

}
