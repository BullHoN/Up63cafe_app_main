package com.avit.up63cafe.payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.avit.up63cafe.HomeActivityViewModel;
import com.avit.up63cafe.NetworkApi;
import com.avit.up63cafe.R;
import com.avit.up63cafe.RetroFitClient;
import com.avit.up63cafe.db.SharedPrefNames;
import com.avit.up63cafe.notification.NotificationPostData;
import com.avit.up63cafe.notification.NotificationResponseData;
import com.avit.up63cafe.ui.orders.OrdersFragment;
import com.avit.up63cafe.ui.orders.OrdersItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaymentAddressSheetDialog extends BottomSheetDialogFragment  {

    private SharedPreferences sharedPreferences;
    private LinearLayout addressLinearLayout;
    private LinearLayout phoneNoLinearLayout;
    private TextView addressView;
    private TextView phoneNoView;
    private Button changeButton;
    private HomeActivityViewModel viewModel;
    final int UPI_PAYMENT = 0;
    private String upiId,upiName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.bottom_sheet_address_checkout,container,false);

        Bundle bundle = getArguments();
        Log.i("UPI_ID",bundle.getString("UPIID","not fpund") + " " +
                bundle.getString("UPINAME","Not Found"));

        upiId = bundle.getString("UPIID","Not Found");
        upiName = bundle.getString("UPINAME","Not Found");

        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);

        phoneNoView = root.findViewById(R.id.phoneNo);
        addressView = root.findViewById(R.id.address);

        addressLinearLayout = root.findViewById(R.id.address_view);
        phoneNoLinearLayout = root.findViewById(R.id.phoneNo_View);
        changeButton = root.findViewById(R.id.change_address);

        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;
        sharedPreferences = getActivity().getSharedPreferences(dbName, Context.MODE_PRIVATE);

        // cod button
        root.findViewById(R.id.cash_on_delivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateAddress()){
                    askFinalPayment();
                }
            }
        });

        // upi payment button
        root.findViewById(R.id.checkout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateAddress()){
                    startPaymentProcess(true);
                }
            }
        });

        String addressName = SharedPrefNames.ADDRESS;

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddressActivity.class);
                startActivity(intent);
            }
        });

        if (!sharedPreferences.contains(addressName)){
            addressLinearLayout.setVisibility(View.GONE);
            phoneNoLinearLayout.setVisibility(View.GONE);
            changeButton.setText("Add Address");

        }else {
            updateTheDetails();
        }
        return root;
    }

    private void askFinalPayment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do You Want To Place The Order ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startPaymentProcess(false);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean validateAddress(){
        String addressName = SharedPrefNames.ADDRESS;

        if (sharedPreferences.contains(addressName)) {
            return true;
        }else {
            Toast.makeText(getContext(),"Add address to proceed",Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
    }

    private void startPaymentProcess(boolean upi){
        String orderName = SharedPrefNames.ORDER_ID;
        String amountName = SharedPrefNames.AMOUNT;

        int amount = sharedPreferences.getInt(amountName,0);
        String orderId = sharedPreferences.getString(orderName,"");

        if (orderId.length() !=0 && amount!=0){
            if(upi){
                startUPIPayment(amount+"");
            }else {
                SendNotificationAndSaveOrder(false);
            }
            dismiss();
        }
    }

    private void SendNotificationAndSaveOrder(boolean isPaid){
        Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_SHORT)
                .show();

        String addressName = SharedPrefNames.ADDRESS;
        String totalName = SharedPrefNames.AMOUNT;
        String orderIdName = SharedPrefNames.ORDER_ID;
        String orderStringName = SharedPrefNames.ORDER_STRING;
        String socketIdName = SharedPrefNames.SOCKET_ID;

        String orderString = sharedPreferences.getString(orderStringName,"");
        String orderId = sharedPreferences.getString(orderIdName,"none");
        String socketId = sharedPreferences.getString(socketIdName,"");

        String address = sharedPreferences.getString(addressName, "");
        int total = sharedPreferences.getInt(totalName, 0);
        int nItems = orderString.split(" , ").length;

        String mainString = orderString.replace("_f", "").replace("_h", "");

        Log.i("Payment", orderString + " " + nItems);

        OrdersItem item = new OrdersItem(0, total, nItems, address, "will be dileverd soon", mainString,orderId);

        Log.i("Order_id",orderId+" id");

        viewModel.insert(item);

        sendNotification(address,orderString,total,orderId,socketId,isPaid);

        startNewFragment();

    }

    private void sendNotification(final String address, final String orders
            , final int total , String orderId , String socketId,boolean ispaid){

        String userName = sharedPreferences.getString(SharedPrefNames.USER_NAME,"error");
        String email = sharedPreferences.getString(SharedPrefNames.EMAIL,"");
        String phoneNo = sharedPreferences.getString(SharedPrefNames.PH_NUMBER,"");
        String nearBy = sharedPreferences.getString(SharedPrefNames.NEAR_ADDRESS,"");

        Retrofit retrofit = RetroFitClient.getInstance();

        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        NotificationPostData data = new NotificationPostData(userName,email
                ,phoneNo,socketId,total,orders,address,nearBy,orderId,ispaid);


        Call<NotificationResponseData> call = networkApi.sendNotification(data);

        call.enqueue(new Callback<NotificationResponseData>() {
            @Override
            public void onResponse(Call<NotificationResponseData> call, Response<NotificationResponseData> response) {
                NotificationResponseData responseData = response.body();

                Log.i("Notification",responseData.isStatus()+"");
            }

            @Override
            public void onFailure(Call<NotificationResponseData> call, Throwable t) {
                Toast.makeText(getContext(),"Contact Support some error occucered",Toast.LENGTH_LONG)
                        .show();
                Log.i("Notification","error in sending notificaiton",t);
            }
        });
    }

    private void startNewFragment() {
        Fragment ordersFragment = new OrdersFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment
                        , ordersFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();

        viewModel.clearCart();
    }

    private void startUPIPayment(String amount){

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", upiName)
                .appendQueryParameter("tr", "25584584")
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);

        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getActivity().getPackageManager())) {
            getActivity().startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getContext(),"No UPI app found, please install one to continue",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void updateTheDetails(){
        addressLinearLayout.setVisibility(View.VISIBLE);
        phoneNoLinearLayout.setVisibility(View.VISIBLE);

        String addressName = SharedPrefNames.ADDRESS;
        String phoneNoName = SharedPrefNames.PH_NUMBER;

        changeButton.setText("Change Address/Phone No");

        addressView.setText(sharedPreferences.getString(addressName,"some error occured"));
        phoneNoView.setText(sharedPreferences.getString(phoneNoName,"some error occured"));

    }

    @Override
    public void onResume() {
        super.onResume();

        String addressName = SharedPrefNames.ADDRESS;

        if (sharedPreferences.contains(addressName)) {
            updateTheDetails();
        }
    }

}
