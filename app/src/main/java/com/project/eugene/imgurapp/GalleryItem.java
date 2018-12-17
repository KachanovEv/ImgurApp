package com.project.eugene.imgurapp;


//This class represents single gallery item
public class GalleryItem {
    public String imageUri;
    public String imageName;
    public boolean isSelected = false;

    public GalleryItem(String imageUri, String imageName) {
        this.imageUri = imageUri;
        this.imageName = imageName;
    }
}
