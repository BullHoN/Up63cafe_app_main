package com.example.up63cafe.ui.dashboard;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.up63cafe.db.CartItemRepository;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private CartItemRepository repository;
    private LiveData<List<CartItem>> allItems;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repository = new CartItemRepository(application);
        allItems = repository.getAllItems();
    }

    public void insert(CartItem cartItem){
        repository.insert(cartItem);
    }

    public void delete(CartItem cartItem){
        repository.delete(cartItem);
    }

    public void update(CartItem cartItem){
        repository.update(cartItem);
    }

    public LiveData<List<CartItem>> getAllItems(){
        return allItems;
    }

}