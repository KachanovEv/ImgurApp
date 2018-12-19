package com.project.eugene.imgurapp.gallery;



 public class GalleryItemModel {

     public String imageUri;
   public String imageName;
    public boolean isSelected = false;

   GalleryItemModel(String imageUri, String imageName) {
        this.imageUri = imageUri;
        this.imageName = imageName;
    }
}
