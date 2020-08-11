package com.avit.up63cafe.ui.home;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.avit.up63cafe.db.CartItemRepository;
import com.avit.up63cafe.ui.dashboard.CartItem;

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