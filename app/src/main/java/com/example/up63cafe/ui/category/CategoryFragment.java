package com.example.up63cafe.ui.category;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.up63cafe.CartBottomSheetDialog;
import com.example.up63cafe.NetworkApi;
import com.example.up63cafe.R;
import com.example.up63cafe.RetroFitClient;
import com.example.up63cafe.ui.dashboard.CartItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryFragment extends Fragment implements CategoryFragmentAdapter.AddToCart {


    private CategoryFragmentAdapter adapter;
    private ListView listView;
    private CategoryFragmentViewModel viewModel;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category,container,false);

        viewModel =
                ViewModelProviders.of(this).get(CategoryFragmentViewModel.class);

        String categoryName = getArguments().getString("categoryName");
        Log.i("Data","" + categoryName);

        progressBar = root.findViewById(R.id.progressBar);

        checkForInternet();

        TextView headingView = root.findViewById(R.id.category_heading);
        headingView.setText(categoryName);

        ImageView imageView = root.findViewById(R.id.category_icon);
        imageView.setImageResource(getResourceIdByName(categoryName));

        adapter = new CategoryFragmentAdapter(getContext()
                ,0,new ArrayList<MenuItem>(),this);

        listView = root.findViewById(R.id.menu_list);
        listView.setAdapter(adapter);

//        setListViewHeightBasedOnChildren(listView);

        ImageButton imageButton = root.findViewById(R.id.back_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return root;
    }

    private int getResourceIdByName(String name){
        switch (name){
            case "Burgers":
                return R.drawable.ic_burger;
            case "Shakes":
                return R.drawable.ic_milkshake;
            case "Juices":
                return R.drawable.ic_healthy_food;
            case "Chowmein":
                return R.drawable.ic_chowmein;
            case "Rolls":
                return R.drawable.ic_rolls;
            case "Momos & Chaap":
                return R.drawable.ic_momos;
            case "Soups":
                return R.drawable.ic_soup;
            case "Biriyaani,Rice & Manchurian":
                return R.drawable.ic_rice;
        }
        return R.drawable.ic_coffee_cup;
    }


    private void checkForInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getMenuItemsFromNetwork();
        } else {
            Toast.makeText(getContext(),"Check Your Network"
                    ,Toast.LENGTH_SHORT).show();
        }

    }

    private void getMenuItemsFromNetwork(){

        Retrofit retrofit = RetroFitClient.getInstance();

        String categoryName = getArguments().getString("categoryName");

        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        Call<ArrayList<MenuItem>> call = networkApi.getMenuItemsOfCategory(categoryName);

        call.enqueue(new Callback<ArrayList<MenuItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItem>> call, Response<ArrayList<MenuItem>> response) {
                ArrayList<MenuItem> items = response.body();

                adapter.clear();
                adapter.addAll(items);

                progressBar.setVisibility(View.GONE);
                setListViewHeightBasedOnChildren(listView);

            }

            @Override
            public void onFailure(Call<ArrayList<MenuItem>> call, Throwable t) {
                Toast.makeText(getContext(),"Some Error Occured",Toast.LENGTH_SHORT)
                        .show();
            }
        });

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
            totalHeight += (listItem.getMeasuredHeight()*1.2);
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void addToCart(int position) {

        MenuItem menuItem = adapter.getItem(position);

        CartItem cartItem = new CartItem(menuItem.getMenuItemName()
            ,menuItem.getHalfPlatePrice(),menuItem.getFullPlatePrice());

        viewModel.addToCart(cartItem);

        Bundle bundle = new Bundle();
        bundle.putString("item_name",menuItem.getMenuItemName());

        CartBottomSheetDialog cartBottomSheetDialog =
                new CartBottomSheetDialog();

        cartBottomSheetDialog.setArguments(bundle);

        cartBottomSheetDialog.show(getFragmentManager(),"addToCart");

    }
}
