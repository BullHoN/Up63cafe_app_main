package com.avit.up63cafe.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.avit.up63cafe.ui.dashboard.CartItem;

import java.util.List;

public class CartItemRepository {

    private CartItemDao cartItemDao;
    private LiveData<List<CartItem>> allItems;

    public CartItemRepository(Application application){
        CartItemDatabase database = CartItemDatabase.getInstance(application);
        cartItemDao = database.cartItemDao();

        allItems = cartItemDao.getAllItems();
    }

    public LiveData<List<CartItem>> getAllItems(){
        return allItems;
    }

    public void insert(CartItem cartItem){
        new InsertAsyncTask(cartItemDao).execute(cartItem);
    }

    public void update(CartItem cartItem){
        new UpdateAsyncTask(cartItemDao).execute(cartItem);
    }

    public void delete(CartItem cartItem){
        new DeleteAsyncTask(cartItemDao).execute(cartItem);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(cartItemDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<CartItem,Void,Void>{

        private CartItemDao cartItemDao;

        public InsertAsyncTask(CartItemDao cartItemDao) {
            this.cartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartItemDao.insert(cartItems[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<CartItem,Void,Void>{

        private CartItemDao cartItemDao;

        public UpdateAsyncTask(CartItemDao cartItemDao) {
            this.cartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartItemDao.update(cartItems[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<CartItem,Void,Void>{

        private CartItemDao cartItemDao;

        public DeleteAsyncTask(CartItemDao cartItemDao) {
            this.cartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartItemDao.delete(cartItems[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{

        private CartItemDao cartItemDao;

        public DeleteAllAsyncTask(CartItemDao cartItemDao) {
            this.cartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartItemDao.deleteAll();
            return null;
        }
    }

}
