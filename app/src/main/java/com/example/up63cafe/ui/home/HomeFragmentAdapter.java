package com.example.up63cafe.ui.home;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.up63cafe.R;

import java.util.ArrayList;

public class HomeFragmentAdapter extends ArrayAdapter<SpecialsItem>{

    private ArrayList<SpecialsItem> items;

    public interface AddToTheCart{
        void addToCart(int position);
    }

    private AddToTheCart addToTheCartCallback;

    public HomeFragmentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SpecialsItem> objects
                ,AddToTheCart addToTheCartCallback) {
        super(context, resource, objects);
        items = objects;
        this.addToTheCartCallback = addToTheCartCallback;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null){
            listView =
                    LayoutInflater.from(getContext()).inflate(R.layout.specials_item,parent,false);
        }

        final SpecialsItem currentItem = items.get(position);

        TextView nameView = listView.findViewById(R.id.item_name);
        nameView.setText(currentItem.getItemname());

        TextView priceView = listView.findViewById(R.id.item_price);
        priceView.setText("₹" + currentItem.getPrice());

        ImageView imageView = listView.findViewById(R.id.specials_image);
        Glide.with(getContext()).load(currentItem.getItemUrl()).into(imageView);

        if (position%2 !=0){
            TextView timeView = listView.findViewById(R.id.time_text);
            timeView.setText("25 - 30 min");

//            imageView.setImageResource(R.drawable.chap);
        }

        Button addToCartButton = listView.findViewById(R.id.add_to_cart);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToTheCartCallback.addToCart(position);
            }
        });

        return listView;
    }
}
