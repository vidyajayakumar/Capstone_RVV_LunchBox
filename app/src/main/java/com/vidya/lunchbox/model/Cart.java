package com.vidya.lunchbox.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cart implements Parcelable {

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    private String mProductid;
    private String mDesc;
    private double mPrice;
    private String mName;
    //    public String getmStrength() {
//        return mStrength;
//    }
//
//    public void setmStrength(String mStrength) {
//        this.mStrength = mStrength;
//    }
    private String mThumbnail;
    private String mStrength;
    private int mQuantity;

    public Cart() {
    }

    protected Cart(Parcel in) {
        mProductid = in.readString();
        mDesc = in.readString();
        mPrice = in.readDouble();
        mName = in.readString();
        mThumbnail = in.readString();
        mStrength = in.readString();
        mQuantity = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProductid);
        dest.writeString(mDesc);
        dest.writeDouble(mPrice);
        dest.writeString(mName);
        dest.writeString(mThumbnail);
        dest.writeString(mStrength);
        dest.writeInt(mQuantity);
    }

    public String getProductid() {
        return mProductid;
    }

    public void setProductid(String mProductid) {
        this.mProductid = mProductid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDesc;
    }

    public void setDescription(String desc) {
        this.mDesc = desc;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }

}