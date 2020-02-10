package com.example.up63cafe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.up63cafe.db.SharedPrefNames;
import com.example.up63cafe.notification.NotificationPostData;
import com.example.up63cafe.notification.NotificationResponseData;
import com.example.up63cafe.ui.dashboard.CartItem;
import com.example.up63cafe.ui.dashboard.DashboardFragment;
import com.example.up63cafe.ui.home.HomeFragment;
import com.example.up63cafe.ui.notifications.NotificationsFragment;
import com.example.up63cafe.ui.orders.OrdersFragment;
import com.example.up63cafe.ui.orders.OrdersItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.razorpay.PaymentResultListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class home extends AppCompatActivity implements PaymentResultListener {

    private HomeActivityViewModel viewModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);

        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;
        sharedPreferences = getSharedPreferences(dbName, Context.MODE_PRIVATE);

        getFirebaseToken();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

        Bundle bundle = getIntent().getBundleExtra("notification");

        Fragment fragment;

        if(bundle!= null){
            Log.i("notification",bundle.getString("order_id")+"");
            fragment = new OrdersFragment();
        }else{
            fragment = new HomeFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }

    private void getFirebaseToken() {

        final String socketIdName = SharedPrefNames.SOCKET_ID;
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("Token", "getInstanceId failed", task.getException());
                    return;
                }

                String token = task.getResult().getToken();
                editor.putString(socketIdName,token);

                editor.apply();

                Log.i("Token",token);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Log.i("info", "" + getSupportFragmentManager().getBackStackEntryCount());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are You Sure You Want To Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        home.super.onBackPressed();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_dashboard:
                            selectedFragment = new DashboardFragment();
                            break;
                        case R.id.navigation_notifications:
                            selectedFragment = new NotificationsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.nav_host_fragment
                                    , selectedFragment)
                            .commit();

                    return true;
                }
            };

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT)
                .show();

        String addressName = SharedPrefNames.ADDRESS;
        String totalName = SharedPrefNames.AMOUNT;
        String orderIdName = SharedPrefNames.ORDER_ID;
        String orderStringName = SharedPrefNames.ORDER_STRING;
        String socketIdName = SharedPrefNames.SOCKET_ID;

        String orderString = sharedPreferences.getString(orderStringName,"");
        String orderId = sharedPreferences.getString(orderIdName,"none");
        String socketId = sharedPreferences.getString(socketIdName,"");

        final String address = sharedPreferences.getString(addressName, "");
        final int total = sharedPreferences.getInt(totalName, 0);

        try {
            int nItems = orderString.split(" , ").length;

            String mainString = orderString.replace("_f", "").replace("_h", "");

            Log.i("Payment", orderString + " " + nItems);

            OrdersItem item = new OrdersItem(0, total, nItems, address, "will be dileverd soon", mainString,orderId);

            Log.i("Order_id",orderId+" id");

            viewModel.insert(item);

            sendNotification(address,orderString,total,orderId,socketId);

            startNewFragment();

        }catch (Exception e){
            Log.e("Payment","error in success",e);
        }

    }

    private void startNewFragment() {
        Fragment ordersFragment = new OrdersFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment
                        , ordersFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();

        viewModel.clearCart();
    }

    private void sendNotification(final String address, final String orders
            , final int total , String orderId , String socketId){

        String userName = sharedPreferences.getString(SharedPrefNames.USER_NAME,"error");
        String email = sharedPreferences.getString(SharedPrefNames.EMAIL,"");
        String phoneNo = sharedPreferences.getString(SharedPrefNames.PH_NUMBER,"");
        String nearBy = sharedPreferences.getString(SharedPrefNames.NEAR_ADDRESS,"");

        Retrofit retrofit = RetroFitClient.getInstance();

        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        NotificationPostData data = new NotificationPostData(userName,email
                ,phoneNo,socketId,total,orders,address,nearBy,orderId);


        Call<NotificationResponseData> call = networkApi.sendNotification(data);

        call.enqueue(new Callback<NotificationResponseData>() {
            @Override
            public void onResponse(Call<NotificationResponseData> call, Response<NotificationResponseData> response) {
                NotificationResponseData responseData = response.body();

                Log.i("Notification",responseData.isStatus()+"");
            }

            @Override
            public void onFailure(Call<NotificationResponseData> call, Throwable t) {
//                sendNotification(address,orders,total);
                Log.i("Notification","error in sending notificaiton",t);
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Some error occurred try again later", Toast.LENGTH_SHORT)
                .show();
        Log.i("Payment", s);
    }
}
