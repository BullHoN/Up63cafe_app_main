package com.avit.up63cafe.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.avit.up63cafe.R;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private OrderFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders,container,false);

        viewModel = ViewModelProviders.of(this).get(OrderFragmentViewModel.class);

        root.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        final OrderFragmentCustomAdapter adapter = new OrderFragmentCustomAdapter(getContext(),0,new ArrayList<OrdersItem>());

        ListView listView = root.findViewById(R.id.list);
        listView.setAdapter(adapter);


        viewModel.getAllItems().observe(this, new Observer<List<OrdersItem>>() {
            @Override
            public void onChanged(List<OrdersItem> ordersItems) {
                adapter.clear();
                adapter.addAll(ordersItems);
            }
        });

        return root;
    }
}
