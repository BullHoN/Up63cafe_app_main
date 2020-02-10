package com.example.up63cafe.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.up63cafe.ui.dashboard.CartItem;
import com.example.up63cafe.ui.orders.OrdersItem;


@Database(entities = {CartItem.class, OrdersItem.class},version = 1)
public abstract class CartItemDatabase extends RoomDatabase {

    private static CartItemDatabase instance;

    public abstract  CartItemDao cartItemDao();

    public abstract OrderItemDao orderItemDao();

    public static synchronized CartItemDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext()
            ,CartItemDatabase.class,"up63cafe_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

//    private static RoomDatabase.Callback callback =
//            new RoomDatabase.Callback(){
//                @Override
//                public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                    super.onCreate(db);
//                    new PopulatedbAsyncTask(instance).execute();
//                }
//            };

//    private static class PopulatedbAsyncTask extends AsyncTask<Void,Void,Void>{
//
//        private OrderItemDao orderItemDao;
//
//        private PopulatedbAsyncTask(CartItemDatabase db){
//
//            orderItemDao = db.orderItemDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            OrdersItem item = new OrdersItem(0,5,2,"asdfasf","afasf","2 soyachap");
//
//            orderItemDao.insert(item);
//
//
//            return null;
//        }
//    }

}
