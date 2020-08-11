package com.avit.up63cafe.ui.categories;

import com.avit.up63cafe.R;
import com.google.gson.annotations.SerializedName;

public class CategoriesItem {

    @SerializedName("categoryName")
    private String mCategoryName;

    public CategoriesItem(String categoryName){
        mCategoryName = categoryName;
    }

    private int setResourceid(){

        switch (mCategoryName){
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

        return R.drawable.ic_open_box;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public int getCategoryResourceId() {
        return setResourceid();
    }
}
