package com.project.eugene.imgurapp.model;


import android.os.Parcel;
import android.os.Parcelable;

public class GalleryItemModel implements Parcelable {
    public static final Creator<GalleryItemModel> CREATOR = new Creator<GalleryItemModel>() {
        @Override
        public GalleryItemModel createFromParcel(Parcel source) {
            return new GalleryItemModel(source);
        }

        @Override
        public GalleryItemModel[] newArray(int size) {
            return new GalleryItemModel[size];
        }
    };
    public String imageUri;
    public String imageName;
    public boolean isSelected = false;

    public GalleryItemModel(String imageUri, String imageName) {
        this.imageUri = imageUri;
        this.imageName = imageName;
    }

    protected GalleryItemModel(Parcel in) {
        this.imageUri = in.readString();
        this.imageName = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUri);
        dest.writeString(this.imageName);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }
}
