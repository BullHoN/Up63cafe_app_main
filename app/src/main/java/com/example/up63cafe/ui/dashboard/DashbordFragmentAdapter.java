package com.example.up63cafe.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.up63cafe.R;

import java.util.ArrayList;
import java.util.List;

public class DashbordFragmentAdapter extends ArrayAdapter<CartItem> {

    List<CartItem> items;

    public interface UpdateTheCart{
        void updateCart(int position);
        void deleteItemFromCart(int position);
    }

    private UpdateTheCart updateCartCallback;

    public DashbordFragmentAdapter(@NonNull Context context, int resource
            , @NonNull List<CartItem> objects, UpdateTheCart updateCartCallback) {
        super(context, resource, objects);
        items =  objects;
        this.updateCartCallback = updateCartCallback;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View cartView = convertView;
        if (cartView == null){
            cartView =
                    LayoutInflater.from(getContext()).inflate(R.layout.cart_item,parent,false);
        }

        final CartItem currentItem = items.get(position);

        TextView textView = cartView.findViewById(R.id.item_name);
        textView.setText(currentItem.getItemName());

        TextView halfPlateView = cartView.findViewById(R.id.half_plate_price);
        TextView fullPlateView  = cartView.findViewById(R.id.full_plate_price);

        final TextView quantityView = cartView.findViewById(R.id.quantity);
        quantityView.setText(currentItem.getQuantity()+"");

        final Button fullPlateButton = cartView.findViewById(R.id.full_plate_button);
        final Button halfPlateButton = cartView.findViewById(R.id.half_plate_button);
        final TextView totalView = cartView.findViewById(R.id.total);

        if (!currentItem.getActiveState()){
            Log.i("Cart","Full is active");
            halfPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.text_color));
            fullPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.dark_primary));
        }else {
            Log.i("Cart","Half is active");
            fullPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.text_color));
            halfPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.dark_primary));
        }


        fullPlateView.setText("₹" + currentItem.getFullPlatePrice());

        if (currentItem.getHalfPlatePrice() == 0){
            LinearLayout layout = cartView.findViewById(R.id.half_plate_layout);
            layout.setVisibility(View.GONE);

            totalView.setText("₹" + currentItem.getTotal());

            fullPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.dark_primary));

        }else {

//            if (!currentItem.getActiveState())
//                currentItem.changeActiveState();

            halfPlateView.setText("₹" + currentItem.getHalfPlatePrice());
            totalView.setText("₹" + currentItem.getTotal());

            fullPlateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentItem.changeActiveState();

                    totalView.setText("₹" +currentItem.getTotal());

                    if (!currentItem.getActiveState()){
                        updateCartCallback.updateCart(position);
//                        halfPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.text_color));
//                        fullPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.dark_primary));
                    }

                }
            });

            halfPlateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.changeActiveState();

                    totalView.setText("₹" +currentItem.getTotal());

                    if (currentItem.getActiveState()){
                        updateCartCallback.updateCart(position);
//                        fullPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.text_color));
//                        halfPlateButton.setBackgroundColor(getContext().getResources().getColor(R.color.dark_primary));
                    }
                }
            });

        }

        ImageButton addBtn = cartView.findViewById(R.id.add_button);
        ImageButton subBtn = cartView.findViewById(R.id.sub_button);

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentItem.getQuantity() > 1){
                    currentItem.decrementedQuantity();
                    updateCartCallback.updateCart(position);
                    totalView.setText("₹" + currentItem.getTotal());
                    quantityView.setText(currentItem.getQuantity()+"");

                }else{
                    updateCartCallback.deleteItemFromCart(position);
                    Toast.makeText(getContext(),currentItem.getItemName() + " removed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.incrementedQuantity();
                updateCartCallback.updateCart(position);
                totalView.setText("₹" + currentItem.getTotal());
                quantityView.setText(currentItem.getQuantity()+"");
            }
        });

        return cartView;
    }

}
