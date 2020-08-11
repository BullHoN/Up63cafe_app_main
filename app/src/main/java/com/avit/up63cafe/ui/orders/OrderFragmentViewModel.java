package com.avit.up63cafe.ui.orders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avit.up63cafe.db.OrderItemRepository;

import java.util.List;

public class OrderFragmentViewModel extends AndroidViewModel {

    private OrderItemRepository repository;
    private LiveData<List<OrdersItem>> allItems;

    public OrderFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderItemRepository(application);
        allItems = repository.getAllItems();
    }

    public void insert(OrdersItem ordersItem){
        repository.insert(ordersItem);
    }

    public void update(OrdersItem ordersItem){
        repository.update(ordersItem);
    }

    public LiveData<List<OrdersItem>> getAllItems() {
        return allItems;
    }
}
