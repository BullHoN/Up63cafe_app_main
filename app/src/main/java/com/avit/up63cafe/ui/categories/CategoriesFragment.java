package com.avit.up63cafe.ui.categories;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.avit.up63cafe.NetworkApi;
import com.avit.up63cafe.R;
import com.avit.up63cafe.RetroFitClient;
import com.avit.up63cafe.ui.category.CategoryFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoriesFragment extends Fragment {

    private CategoriesFragmentAdapter adapter;
    private GridView gridView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories,container,false);

        checkForInternet();

        adapter = new CategoriesFragmentAdapter(getContext(),0,new ArrayList<CategoriesItem>());

        gridView = root.findViewById(R.id.category_list);
        gridView.setAdapter(adapter);
        progressBar = root.findViewById(R.id.progressBar);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CategoriesItem selectedItem = (CategoriesItem) parent.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putString("categoryName",selectedItem.getCategoryName());

                Fragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment
                ,categoryFragment)
                .addToBackStack(null)
                .commit();
            }
        });

        ImageButton backButton = root.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click","clicked");
                getFragmentManager().popBackStack();
            }
        });

        return root;
    }

    private void checkForInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getCategoriesForNetwork();
        } else {
            Toast.makeText(getContext(),"Check Your Network"
                    ,Toast.LENGTH_SHORT).show();
        }

    }

    private void getCategoriesForNetwork(){

        Retrofit retrofit = RetroFitClient.getInstance();

        NetworkApi networkApi = retrofit.create(NetworkApi.class);

        Call<ArrayList<CategoriesItem>> call = networkApi.getCategories();

        call.enqueue(new Callback<ArrayList<CategoriesItem>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoriesItem>> call, Response<ArrayList<CategoriesItem>> response) {
                ArrayList<CategoriesItem> list = response.body();

                adapter.clear();
                adapter.addAll(list);

                progressBar.setVisibility(View.GONE);
                setGridViewHeightBasedOnChildren(gridView,2);

            }

            @Override
            public void onFailure(Call<ArrayList<CategoriesItem>> call, Throwable t) {
                Toast.makeText(getContext(),"Some Error Occured",Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        if(items%2 == 0) {
            params.height = (int) (totalHeight * 1.014);
        }else {
            params.height = (int) (totalHeight * 1.4);
        }
        gridView.setLayoutParams(params);

    }
}
