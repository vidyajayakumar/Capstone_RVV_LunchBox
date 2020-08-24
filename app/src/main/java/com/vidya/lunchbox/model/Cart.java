package com.vidya.lunchbox.model;


public class Cart implements java.io.Serializable {

    private String mProductid;
    private String mDesc;
    private int mPrice;
    private String mName;
    private int mThumbnail;
    private String mStrength;
    private int mQuantity;

    public String getmStrength() {
        return mStrength;
    }

    public void setmStrength(String mStrength) {
        this.mStrength = mStrength;
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

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        this.mPrice = price;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }

} 