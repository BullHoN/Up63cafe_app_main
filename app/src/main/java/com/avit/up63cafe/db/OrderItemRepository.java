package com.avit.up63cafe.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.avit.up63cafe.notification.NotificationReceiveData;
import com.avit.up63cafe.ui.orders.OrdersItem;

import java.util.List;

public class OrderItemRepository {

    private OrderItemDao orderItemDao;
    private LiveData<List<OrdersItem>> allItems;

    public OrderItemRepository(Application application){
        CartItemDatabase database = CartItemDatabase.getInstance(application);
        orderItemDao = database.orderItemDao();

        allItems = orderItemDao.getAllItems();
    }

    public LiveData<List<OrdersItem>> getAllItems() {
        return allItems;
    }

    public void insert(OrdersItem ordersItem){
        new InsertAsyncTask(orderItemDao).execute(ordersItem);
    }

    public void update(OrdersItem ordersItem){
        new UpdateAsyncTask(orderItemDao).execute(ordersItem);
    }

    public void updateData(NotificationReceiveData data){
        new UpdateOrderAsyncTask(orderItemDao).execute(data);
    }


    private static class InsertAsyncTask extends AsyncTask<OrdersItem,Void,Void>{

        private OrderItemDao dao;

        public InsertAsyncTask(OrderItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(OrdersItem... ordersItems) {
            dao.insert(ordersItems[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<OrdersItem,Void,Void>{

        private OrderItemDao dao;

        public UpdateAsyncTask(OrderItemDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(OrdersItem... ordersItems) {
            dao.update(ordersItems[0]);
            return null;
        }
    }

    private static class UpdateOrderAsyncTask extends AsyncTask<NotificationReceiveData,Void,Void>{

        private OrderItemDao dao;

        public UpdateOrderAsyncTask(OrderItemDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NotificationReceiveData... notificationReceiveData) {
            dao.UpdateOrder(notificationReceiveData[0].getOrderId(),notificationReceiveData[0].getStatus(),notificationReceiveData[0].getMessage());
            return null;
        }
    }

}
