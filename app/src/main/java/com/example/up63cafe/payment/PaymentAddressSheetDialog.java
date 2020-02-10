package com.example.up63cafe.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.up63cafe.R;
import com.example.up63cafe.db.SharedPrefNames;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentAddressSheetDialog extends BottomSheetDialogFragment  {

    private SharedPreferences sharedPreferences;
    private LinearLayout addressLinearLayout;
    private LinearLayout phoneNoLinearLayout;
    private TextView addressView;
    private TextView phoneNoView;
    private Button changeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.bottom_sheet_address_checkout,container,false);

        Checkout.preload(getContext());

        phoneNoView = root.findViewById(R.id.phoneNo);
        addressView = root.findViewById(R.id.address);

        addressLinearLayout = root.findViewById(R.id.address_view);
        phoneNoLinearLayout = root.findViewById(R.id.phoneNo_View);
        changeButton = root.findViewById(R.id.change_address);

        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;
        sharedPreferences = getActivity().getSharedPreferences(dbName, Context.MODE_PRIVATE);

        root.findViewById(R.id.checkout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addressName = SharedPrefNames.ADDRESS;

                if (sharedPreferences.contains(addressName)) {
                    startPaymentProcess();
                }else {
                    Toast.makeText(getContext(),"Add address to proceed",Toast.LENGTH_SHORT)
                            .show();
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

    private void startPaymentProcess(){
        String orderName = SharedPrefNames.ORDER_ID;
        String amountName = SharedPrefNames.AMOUNT;
        String emailName = SharedPrefNames.EMAIL;
        String phoneNoName = SharedPrefNames.PH_NUMBER;

        int amount = sharedPreferences.getInt(amountName,0);
        String orderId = sharedPreferences.getString(orderName,"");
        String email = sharedPreferences.getString(emailName,"");
        String phoneNo = sharedPreferences.getString(phoneNoName,"");

        if (orderId.length() !=0 && amount!=0){
//            Log.i("Payment",amount+"");
            startPayment(orderId,amount,email,phoneNo);
            dismiss();
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

    public void startPayment(String orderId,int amount,String email,String phoneNo) {

        Checkout checkout = new Checkout();

        checkout.setImage(R.drawable.ic_coffee_cup);

        final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Up63Cafe");


            options.put("description", "Order Id." + orderId);
            options.put("order_id", orderId);
            options.put("currency", "INR");

            options.put("amount", amount*100);

            options.put("prefill.email",email);
            options.put("prefill.contact",phoneNo);

            options.put("theme.color","#FF891B");

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("Payment", "Error in starting Razorpay Checkout", e);
        }
    }

}
