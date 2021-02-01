package com.avit.up63cafe.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avit.up63cafe.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderFragmentCustomAdapter extends ArrayAdapter<OrdersItem> {

    List<OrdersItem> items;
    private Context context;

    public OrderFragmentCustomAdapter(@NonNull Context context, int resource, @NonNull List<OrdersItem> objects) {
        super(context, resource, objects);
        items = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ordersListView = convertView;
        if (ordersListView == null){
            ordersListView = LayoutInflater.from(getContext())
                    .inflate(R.layout.order_item,parent,false);
        }

        LinearLayout phoneInqueryView = ordersListView.findViewById(R.id.call);
        phoneInqueryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "9653090858"));
                context.startActivity(intent);
            }
        });

        OrdersItem currentItem = items.get(position);

        Log.i("Order_id",currentItem.getOrderId() + " " + items.size() + " " + position);

        TextView addressView = ordersListView.findViewById(R.id.address);
        addressView.setText(currentItem.getAddress());

        TextView itemsView = ordersListView.findViewById(R.id.items);
        itemsView.setText(currentItem.getNoItems() + " items");

        TextView detailsView = ordersListView.findViewById(R.id.items_details);
        detailsView.setText(currentItem.getItems());

        TextView totalView = ordersListView.findViewById(R.id.total);
        totalView.setText("₹" + currentItem.getTotal());

        ImageView imageView = ordersListView.findViewById(R.id.image);
        TextView imageTextView = ordersListView.findViewById(R.id.image_text);
        TextView deliveryTimeView = ordersListView.findViewById(R.id.delivery_times);

        deliveryTimeView.setText("Will Be Delivered Soon");

        TextView reasonView = ordersListView.findViewById(R.id.reason);
        if(currentItem.getStatus() == -1){
            reasonView.setText("Reason");
        }else {
            reasonView.setText("Delivery Time");
        }

        switch (currentItem.getStatus()){
            case -1:
                imageView.setImageResource(R.drawable.ic_canceled);
                imageTextView.setText("Order Was Canceled");
                deliveryTimeView.setText(currentItem.getDate());
                break;
            case 0:
                imageView.setImageResource(R.drawable.ic_pending);
                imageTextView.setText("We Got Your Order");
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_out_dilevery);
                imageTextView.setText("Order Out For Delivery");
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_dilevered);
                Date currentTime = Calendar.getInstance().getTime();
                String time = currentTime.toString().split(" G")[0];
                deliveryTimeView.setText(time);
                imageTextView.setText("Your Order Has Been Delivered");
                break;
            default:
                imageView.setImageResource(R.drawable.ic_pending);
                imageTextView.setText("Order in process");
        }


        return  ordersListView;
    }
}
