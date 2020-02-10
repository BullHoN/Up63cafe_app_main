package com.example.up63cafe.ui.home;

import com.google.gson.annotations.SerializedName;

public class SpecialsItem {

    @SerializedName("fullPlatePrice")
    private int mPrice;

    @SerializedName("itemName")
    private String mItemname;

    @SerializedName("itemImageUrl")
    private String mItemUrl;

    @SerializedName("halfPlatePrice")
    private int mHalfPlatePrice;

    public SpecialsItem(String itemName,int price,int halfPlatePrice,String mItemUrl){
        mItemname = itemName;
        mPrice = price;
        mHalfPlatePrice = halfPlatePrice;
    }

    public String getItemUrl() {
        return mItemUrl;
    }

    public int getmHalfPlatePrice() {
        return mHalfPlatePrice;
    }

    public int getPrice() {
        return mPrice;
    }

    public String getItemname() {
        return mItemname;
    }
}
