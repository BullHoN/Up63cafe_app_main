package com.example.up63cafe.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.up63cafe.R;

import java.util.ArrayList;

public class CategoriesFragmentAdapter extends ArrayAdapter<CategoriesItem> {

    private ArrayList<CategoriesItem> items;

    public CategoriesFragmentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CategoriesItem> objects) {
        super(context, resource, objects);
        items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View categoryView = convertView;

        if (categoryView == null){
            categoryView =
                    LayoutInflater.from(getContext()).inflate(R.layout.category_item,parent,false);
        }

        CategoriesItem currentItem = items.get(position);

        TextView nameView = categoryView.findViewById(R.id.category_name);
        nameView.setText(currentItem.getCategoryName());

        ImageView imageView = categoryView.findViewById(R.id.category_icon);
        imageView.setImageResource(currentItem.getCategoryResourceId());

        return categoryView;

    }
}
