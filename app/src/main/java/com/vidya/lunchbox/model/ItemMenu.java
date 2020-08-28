package com.vidya.lunchbox.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemMenu implements Parcelable {

    public static final Creator<ItemMenu> CREATOR = new Creator<ItemMenu>() {
        @Override
        public ItemMenu createFromParcel(Parcel in) {
            return new ItemMenu(in);
        }

        @Override
        public ItemMenu[] newArray(int size) {
            return new ItemMenu[size];
        }
    };
    private String itemId;
    private String catId;
    private String itemName;
    private String itemDesc;
    private double price;
    private String itemImage;
    private int quantity;
    private boolean isDeal;
    private boolean inStock = true;

    public ItemMenu() {
    }

    protected ItemMenu(Parcel in) {
        itemId = in.readString();
        catId = in.readString();
        itemName = in.readString();
        itemDesc = in.readString();
        price = in.readDouble();
        itemImage = in.readString();
        quantity = in.readInt();
        isDeal = in.readByte() != 0;
        inStock = in.readByte() != 0;

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isDeal() {
        return isDeal;
    }

    public void setDeal(boolean deal) {
        isDeal = deal;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(catId);
        dest.writeString(itemName);
        dest.writeString(itemDesc);
        dest.writeDouble(price);
        dest.writeString(itemImage);
        dest.writeInt(quantity);
        dest.writeByte((byte) (isDeal ? 1 : 0));
        dest.writeByte((byte) (inStock ? 1 : 0));
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}