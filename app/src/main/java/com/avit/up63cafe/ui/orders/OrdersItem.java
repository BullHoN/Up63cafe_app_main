package com.avit.up63cafe.ui.orders;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_table")
public class OrdersItem {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "status")
    private int mStatus;

    @ColumnInfo(name = "total")
    private int mTotal;

    @ColumnInfo(name = "no_items")
    private int mNoItems;

    @ColumnInfo(name = "address")
    private String mAddress;

    @ColumnInfo(name = "date")
    private String mDate;

    @ColumnInfo(name = "items")
    private String mItems;

    @ColumnInfo(name =  "order_id")
    private String mOrderId;

    public OrdersItem(int mStatus, int mTotal, int mNoItems, String mAddress, String mDate, String mItems,String mOrderId) {
        this.mStatus = mStatus;
        this.mTotal = mTotal;
        this.mNoItems = mNoItems;
        this.mAddress = mAddress;
        this.mDate = mDate;
        this.mItems = mItems;
        this.mOrderId = mOrderId;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public int get_id() {
        return _id;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getTotal() {
        return mTotal;
    }

    public int getNoItems() {
        return mNoItems;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getDate() {
        return mDate;
    }

    public String getItems() {
        return mItems;
    }
}
