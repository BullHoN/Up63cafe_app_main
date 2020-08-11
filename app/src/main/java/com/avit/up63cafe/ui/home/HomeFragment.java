package com.avit.up63cafe.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.avit.up63cafe.CartBottomSheetDialog;
import com.avit.up63cafe.CoronaActivity;
import com.avit.up63cafe.NetworkApi;
import com.avit.up63cafe.R;
import com.avit.up63cafe.RetroFitClient;
import com.avit.up63cafe.db.SharedPrefNames;
import com.avit.up63cafe.ui.categories.CategoriesFragment;
import com.avit.up63cafe.ui.category.CategoryFragment;
import com.avit.up63cafe.ui.dashboard.CartItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements HomeFragmentAdapter.AddToTheCart {

    private HomeViewModel homeViewModel;
    private HomeFragmentAdapter adapter;
    private ListView listView;
    private LinearLayout mainLayout,errorLayout;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        String dbName = SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME;

        sharedPreferences = getActivity().getSharedPreferences(dbName, Context.MODE_PRIVATE);

        checkPrefrenceData();

        if(!sharedPreferences.contains(SharedPrefNames.BANNER_BOOL)){
            openCoronaVirusBanner();
            String Banner_bool = SharedPrefNames.BANNER_BOOL;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Banner_bool,true);

            editor.apply();
        }

        progressBar = root.findViewById(R.id.loading);

//        progressBar.(getResources().getColor(R.color.white));

        mainLayout = root.findViewById(R.id.main_view);
        errorLayout = root.findViewById(R.id.failure_view);

        TextView mainText = root.findViewById(R.id.main_text);
        String text = "<font color=#FF891B>Hello,</font> <font color=#5B5B5B>Looking for Great Food Near You ?</font>";
        mainText.setText(Html.fromHtml(text));

        Log.i("Invoked","i am agian invoked");

        checkForInternet();

        Button refreshButton = root.findViewById(R.id.refresh_button);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForInternet();
            }
        });


        adapter = new HomeFragmentAdapter(getContext()
                ,0,new ArrayList<SpecialsItem>(),this);

        listView = root.findViewById(R.id.specials_list);

        listView.setAdapter(adapter);

        getSpecialsItemsForNetwork();


        TextView categoriesView = root.findViewById(R.id.view_categories);
        categoriesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment categoriesFragment = new CategoriesFragment();
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                        .replace(R.id.nav_host_fragment
                        ,categoriesFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        RelativeLayout buttonsLayout = root.findViewById(R.id.burger_category);
        buttonsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpenFragmentOnClick("Burgers");
            }
        });

        root.findViewById(R.id.juice_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpenFragmentOnClick("Rolls");
            }
        });

        root.findViewById(R.id.shakes_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpenFragmentOnClick("Biriyaani,Rice & Manchurian");
            }
        });

        root.findViewById(R.id.chowmein_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpenFragmentOnClick("Chowmein");
            }
        });


        return root;
    }

    private void checkPrefrenceData(){

        String userName = SharedPrefNames.USER_NAME;
        String userEmail = SharedPrefNames.EMAIL;
        String userAuth = SharedPrefNames.ALLOW_ACCESS;

        String name = sharedPreferences.getString(userName,null);
        String email = sharedPreferences.getString(userEmail,null);
        boolean isAuthenticated = sharedPreferences.getBoolean(userAuth,false);

        Log.i("Pref",name + " " + email + " " + isAuthenticated);

    }

    private void checkForInternet(){

        progressBar.setVisibility(View.VISIBLE);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i("Data","Internet is coneected");
            getSpecialsItemsForNetwork();
        } else {
            Log.i("Data","No Internet");
            Toast.makeText(getContext(),"Check Your Network"
            ,Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }

    }

    private void getSpecialsItemsForNetwork(){

        Retrofit retrofit = RetroFitClient.getInstance();

        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        Call<ArrayList<SpecialsItem>> call = networkApi.getSpecialItems();

        call.enqueue(new Callback<ArrayList<SpecialsItem>>() {
            @Override
            public void onResponse(Call<ArrayList<SpecialsItem>> call, Response<ArrayList<SpecialsItem>> response) {
                ArrayList<SpecialsItem> items = response.body();

                adapter.clear();
                adapter.addAll(items);

                setListViewHeightBasedOnChildren(listView);

                progressBar.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ArrayList<SpecialsItem>> call, Throwable t) {
                Log.i("Data Error","Network error",t);
                progressBar.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
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

    private void addOpenFragmentOnClick(String name){
        Bundle bundle = new Bundle();
        bundle.putString("categoryName",name);

        Fragment categoryFragment = new CategoryFragment();
        categoryFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment
                ,categoryFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void addToCart(int position) {
        SpecialsItem currentItem = adapter.getItem(position);

        CartItem cartItem = new CartItem(currentItem.getItemname()
        ,currentItem.getmHalfPlatePrice(),currentItem.getPrice());


        homeViewModel.addToCart(cartItem);

        Bundle bundle = new Bundle();
        bundle.putString("item_name",currentItem.getItemname());

        CartBottomSheetDialog cartBottomSheetDialog = new CartBottomSheetDialog();
        cartBottomSheetDialog.setArguments(bundle);
        cartBottomSheetDialog.show(getFragmentManager(),"addToCart");

    }

    private void openCoronaVirusBanner(){
        Intent intent = new Intent(getContext(), CoronaActivity.class);
        startActivity(intent);
    }

}