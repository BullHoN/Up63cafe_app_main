package com.example.up63cafe.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.up63cafe.R;
import com.example.up63cafe.db.OrderItemRepository;
import com.example.up63cafe.db.SharedPrefNames;
import com.example.up63cafe.home;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    public static int NOTIFICATION_ID = 1;
    private SharedPreferences sharedPreferences;


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;
        sharedPreferences = getSharedPreferences(dbName, Context.MODE_PRIVATE);

        String socketIdName = SharedPrefNames.SOCKET_ID;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(socketIdName,s);

        editor.apply();

        Log.i("Token",s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> data = remoteMessage.getData();

        String orderId = data.get("orderId");
        String status = data.get("status");
        String summary = data.get("summary");

        OrderItemRepository repository = new OrderItemRepository(getApplication());

        NotificationReceiveData notificationReceiveData = new NotificationReceiveData(orderId,Integer.parseInt(status));

        repository.updateData(notificationReceiveData);

        Log.i("Notification",orderId + " " + status + " " +summary);

        String title,body;

        if(status.equals("1")){
            title = "Your order is out for delivery";
            body = summary + " will be arrived soon ";
        }else if(status.equals("2")){
            title = "Your order is Successfully delivered";
            body = summary + " is successfully delivered";
        } else{
            title  = "zeher";
            body = "zeher2";
        }

        generateNotification(title,body);

    }

    private void generateNotification(String title,String body){

        Bundle bundle = new Bundle();
        bundle.putString("order_id","asfasf_safasfsaf");

        Intent intent = new Intent(this, home.class);
        intent.putExtra("notification",bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent
                ,PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_coffee_cup)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(NOTIFICATION_ID > 1073741824){
            NOTIFICATION_ID = 0;
        }

        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());
    }
}
