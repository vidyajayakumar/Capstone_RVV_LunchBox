package com.vidya.lunchbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Order implements Parcelable {

    private String orderId;
    private String userId;
    private String productIds;
    private String status;

//    private HashMap<String, String> hashMap = new HashMap<>();

    public Order(){

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    private long dateTime;

    protected Order(Parcel in) {
        orderId = in.readString();
        userId = in.readString();
        productIds = in.readString();
        status = in.readString();
        dateTime = in.readLong();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(userId);
        dest.writeString(productIds);
        dest.writeString(status);
        dest.writeLong(dateTime);
    }

//    public HashMap<String, String> getHashMap() {
//        return hashMap;
//    }
//
//    public void setHashMap(HashMap<String, String> hashMap) {
//        this.hashMap = hashMap;
//    }
}
