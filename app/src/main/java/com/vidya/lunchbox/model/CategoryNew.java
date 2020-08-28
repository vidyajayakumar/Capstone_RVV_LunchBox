package com.vidya.lunchbox.model;

import android.os.Parcel;
import android.os.Parcelable;


public class CategoryNew implements Parcelable {

    public static final Creator<CategoryNew> CREATOR = new Creator<CategoryNew>() {
        @Override
        public CategoryNew createFromParcel(Parcel in) {
            return new CategoryNew(in);
        }

        @Override
        public CategoryNew[] newArray(int size) {
            return new CategoryNew[size];
        }
    };
    private String catId;
    private String catName;
    private String catImage;

    public CategoryNew() {

    }

    protected CategoryNew(Parcel in) {
        catId = in.readString();
        catName = in.readString();
        catImage = in.readString();
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(catId);
        dest.writeString(catName);
        dest.writeString(catImage);
    }
}
