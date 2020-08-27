package com.vidya.lunchbox.model;

public class Cart implements java.io.Serializable {

    private String mProductid;
    private String mDesc;
    private double mPrice;
    private String mName;
    private String mThumbnail;

//    public String getmStrength() {
//        return mStrength;
//    }
//
//    public void setmStrength(String mStrength) {
//        this.mStrength = mStrength;
//    }

    private String mStrength;
    private int mQuantity;

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