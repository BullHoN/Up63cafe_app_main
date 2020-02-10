package com.example.up63cafe.ui.dashboard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "itemName")
    private String mItemName;

    @ColumnInfo(name = "halfPlatePrice")
    private int mHalfPlatePrice;

    @ColumnInfo(name = "fullPlatePrice")
    private int mFullPlatePrice;

    @ColumnInfo(name = "quantity")
    private int mQuantity=1;

    @ColumnInfo(name = "halfPlateOrdered")
    private boolean isHalfActive = false;

    public CartItem(String mItemName, int mHalfPlatePrice, int mFullPlatePrice) {
        this.mItemName = mItemName;
        this.mHalfPlatePrice = mHalfPlatePrice;
        this.mFullPlatePrice = mFullPlatePrice;
    }

    public void setHalfActive(boolean halfActive) {
        isHalfActive = halfActive;
    }

    public boolean isHalfActive() {
        return isHalfActive;
    }

    public void quantity(int quantity){
        this.mQuantity = quantity;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public boolean getActiveState(){
        return isHalfActive;
    }

    public int getTotal(){
        if (isHalfActive){
            return mQuantity*mHalfPlatePrice;
        }else {
            return mQuantity*mFullPlatePrice;
        }
    }

    public void incrementedQuantity(){
        ++mQuantity;
    }

    public void decrementedQuantity(){
        mQuantity = mQuantity > 1 ? mQuantity-1 : mQuantity;
    }

    public void changeActiveState(){
        isHalfActive = !isHalfActive;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getItemName() {
        return mItemName;
    }

    public int getHalfPlatePrice() {
        return mHalfPlatePrice;
    }

    public int getFullPlatePrice() {
        return mFullPlatePrice;
    }
}
