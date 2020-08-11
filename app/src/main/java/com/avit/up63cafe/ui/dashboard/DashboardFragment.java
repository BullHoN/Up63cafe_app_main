package com.avit.up63cafe.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.avit.up63cafe.NetworkApi;
import com.avit.up63cafe.R;
import com.avit.up63cafe.RetroFitClient;
import com.avit.up63cafe.db.SharedPrefNames;
import com.avit.up63cafe.payment.PaymentAddressSheetDialog;
import com.avit.up63cafe.payment.PaymentOrderPostData;
import com.avit.up63cafe.payment.PaymentOrderResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashboardFragment extends Fragment implements DashbordFragmentAdapter.UpdateTheCart {

    private ListView listView;
    private DashbordFragmentAdapter adapter;
    private TextView subTotalView;
    private TextView totalPriceView,cartItemsView , deliveryChargeView;
    private int mTotal=0;
    private int DIVELERY_CHARGE = 5;
    private DashboardViewModel viewModel;
    private LinearLayout checkOutLayout;
    private SharedPreferences sharedPreferences;
    private String upiId;
    private String upiName;

    private boolean allowPayment = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);

        deliveryChargeView = root.findViewById(R.id.delivery_charge);

        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;
        sharedPreferences = getActivity().getSharedPreferences(dbName, Context.MODE_PRIVATE);

        // checkout button
        root.findViewById(R.id.checkout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allowPayment) {
                    getOrderIdFromNetwork();
                    saveStringToPrefs();
                }else{
                    Toast.makeText(getContext(),"Sorry cafe is currently closed",Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        checkOutLayout = root.findViewById(R.id.checkout_layout);

        cartItemsView = root.findViewById(R.id.count_item);
        subTotalView = root.findViewById(R.id.subtotal_amount);
        totalPriceView = root.findViewById(R.id.total_amount);

        viewModel.getAllItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                Log.i("Cart",cartItems.size()+"");

                adapter = new DashbordFragmentAdapter(getContext()
                        ,0,cartItems,DashboardFragment.this);

                listView = root.findViewById(R.id.list);
                listView.setAdapter(adapter);

                setListViewHeightBasedOnChildren(listView);

                toogleTheCheckoutLayout(cartItems.size());

                getCheckoutDetails();

            }
        });

        return root;
    }


    private void getCheckoutDetails(){

        Retrofit retrofit = RetroFitClient.getInstance();

        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        Call<CheckoutDetailsReponseData> call = networkApi.getCheckoutDetails();

        call.enqueue(new Callback<CheckoutDetailsReponseData>() {
            @Override
            public void onResponse(Call<CheckoutDetailsReponseData> call, Response<CheckoutDetailsReponseData> response) {
                CheckoutDetailsReponseData reponseData = response.body();

                upiId = reponseData.getUpiId();
                upiName = reponseData.getUpiName();
                allowPayment = reponseData.isAllowPayment();
                if(reponseData.getDeliveryPrice() == 0)
                    deliveryChargeView.setText("Free Delivery");
                else
                    deliveryChargeView.setText("₹" + reponseData.getDeliveryPrice());
                DIVELERY_CHARGE = reponseData.getDeliveryPrice();
                updateCartUi();

            }

            @Override
            public void onFailure(Call<CheckoutDetailsReponseData> call, Throwable t) {
                allowPayment = false;
                Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    private void saveStringToPrefs(){
        List<CartItem> cartItems = adapter.items;

        StringBuilder stringBuilder = new StringBuilder();

        for (CartItem ct : cartItems) {
            stringBuilder.append(ct.getQuantity() + " " + ct.getItemName() + "_");
            String plateSize = ct.getActiveState() ? "h" : "f";
            stringBuilder.append(plateSize + " , ");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        String mainString = stringBuilder.toString();


        String orderStringName = SharedPrefNames.ORDER_STRING;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(orderStringName,mainString);

        editor.apply();

    }

    private void getOrderIdFromNetwork(){

        final PaymentAddressSheetDialog dialog = new PaymentAddressSheetDialog();

        Bundle bundle = new Bundle();
        bundle.putString("UPIID",upiId);
        bundle.putString("UPINAME",upiName);

        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(),"get_address");

        Retrofit retrofit = RetroFitClient.getInstance();

        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        String userName = SharedPrefNames.USER_NAME;
        String userEmailName = SharedPrefNames.EMAIL;

        String email = sharedPreferences.getString(userEmailName,"");
        String username = sharedPreferences.getString(userName,"");

        PaymentOrderPostData data = new PaymentOrderPostData(email,username,mTotal+DIVELERY_CHARGE);

        Call<PaymentOrderResponseData> call = networkApi.generatePaymentOrderId(data);

        call.enqueue(new Callback<PaymentOrderResponseData>() {
            @Override
            public void onResponse(Call<PaymentOrderResponseData> call, Response<PaymentOrderResponseData> response) {
                PaymentOrderResponseData responseData = response.body();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                String orderName = SharedPrefNames.ORDER_ID;
                String amountName = SharedPrefNames.AMOUNT;

                editor.putInt(amountName,mTotal+DIVELERY_CHARGE);
                editor.putString(orderName,responseData.getOrder_id());

                editor.apply();

            }

            @Override
            public void onFailure(Call<PaymentOrderResponseData> call, Throwable t) {
                Toast.makeText(getContext(),"There was some error while fetching your order please try again later",Toast.LENGTH_SHORT)
                        .show();
                dialog.dismiss();
            }
        });

    }

    private void toogleTheCheckoutLayout(int count){
        if (count == 0){
            checkOutLayout.setVisibility(View.INVISIBLE);
            cartItemsView.setText("No Items in cart");
        }else {
            checkOutLayout.setVisibility(View.VISIBLE);
            updateCartUi();
        }
    }

    private void updateCartUi(){
        cartItemsView.setText(adapter.getCount()+" Items in Cart");

        int total=0;
        List<CartItem> items = adapter.items;

        for(int i=0;i<adapter.getCount();i++){
            total+=items.get(i).getTotal();
        }

        mTotal = total;
        subTotalView.setText("₹" + total);
        totalPriceView.setText("₹" + (total+DIVELERY_CHARGE));
    }



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += (listItem.getMeasuredHeight()*1.15);
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    public void updateCart(int position) {
        viewModel.update(adapter.getItem(position));
    }

    @Override
    public void deleteItemFromCart(int position) {
        viewModel.delete(adapter.getItem(position));
    }
}