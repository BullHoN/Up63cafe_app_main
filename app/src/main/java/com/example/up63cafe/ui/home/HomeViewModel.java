package com.example.up63cafe.ui.home;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.up63cafe.db.CartItemRepository;
import com.example.up63cafe.ui.dashboard.CartItem;

import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {

    private CartItemRepository repository;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new CartItemRepository(application);
    }

    public void addToCart(CartItem cartItem){
        repository.insert(cartItem);
    }
}