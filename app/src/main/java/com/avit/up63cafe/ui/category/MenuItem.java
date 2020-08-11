package com.avit.up63cafe.ui.category;

import com.google.gson.annotations.SerializedName;

public class MenuItem {

    @SerializedName("itemName")
    private String mMenuItemName;

    @SerializedName("halfPlatePrice")
    private int mHalfPlatePrice;

    @SerializedName("fullPlatePrice")
    private int mFullPlatePrice;

    public MenuItem(String menuItemName,int halfPlatePrice,int fullPlatePrice){
        mMenuItemName = menuItemName;
        mHalfPlatePrice = halfPlatePrice;
        mFullPlatePrice = fullPlatePrice;
    }

    public int getFullPlatePrice() {
        return mFullPlatePrice;
    }

    public int getHalfPlatePrice() {
        return mHalfPlatePrice;
    }

    public String getMenuItemName() {
        return mMenuItemName;
    }
}
