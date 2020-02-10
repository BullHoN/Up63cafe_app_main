package com.example.up63cafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.up63cafe.ui.dashboard.DashboardFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CartBottomSheetDialog extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet_layout,container,false);

        Bundle bundle = getArguments();

        TextView textView = v.findViewById(R.id.item_name);
        textView.setText(bundle.getString("item_name") + " is added to cart");

        v.findViewById(R.id.go_to_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment cartFragment = new DashboardFragment();
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .replace(R.id.nav_host_fragment
                                ,cartFragment)
                        .commit();
                dismiss();
            }
        });

        return v;
    }
}
