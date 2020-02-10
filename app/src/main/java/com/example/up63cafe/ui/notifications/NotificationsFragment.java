package com.example.up63cafe.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.up63cafe.R;
import com.example.up63cafe.ui.orders.OrdersFragment;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        LinearLayout myOrderView = root.findViewById(R.id.my_orders);

        myOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ordersFragment = new OrdersFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment
                                ,ordersFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }
}