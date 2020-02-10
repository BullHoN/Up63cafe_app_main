package com.example.up63cafe.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.up63cafe.R;

import java.util.ArrayList;

public class CategoryFragmentAdapter extends ArrayAdapter<MenuItem> {

    private ArrayList<MenuItem> items;

    public interface AddToCart {
        void addToCart(int position);
    }

    AddToCart addToCartCallback;

    public CategoryFragmentAdapter(@NonNull Context context, int resource
            , @NonNull ArrayList<MenuItem> objects,AddToCart addToCartCallback) {
        super(context, resource, objects);
        items = objects;
        this.addToCartCallback = addToCartCallback;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View categoryView = convertView;
        if (categoryView == null){
            categoryView =
                    LayoutInflater.from(getContext()).inflate(R.layout.menu_item,parent,false);
        }

        MenuItem currentItem = items.get(position);

        TextView itemName = categoryView.findViewById(R.id.item_name);
        itemName.setText(currentItem.getMenuItemName());

        TextView fullPlateView = categoryView.findViewById(R.id.full_plate_price);
        fullPlateView.setText("₹" + currentItem.getFullPlatePrice());

        if (currentItem.getHalfPlatePrice() != 0){
            TextView halfPlateView = categoryView.findViewById(R.id.half_plate_price);
            halfPlateView.setText("₹" + currentItem.getHalfPlatePrice());
        }

        ImageButton addToCartButton = categoryView.findViewById(R.id.add_to_cart);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartCallback.addToCart(position);
            }
        });

        return categoryView;
    }
}
