package com.project.eugene.imgurapp.util;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.project.eugene.imgurapp.model.GalleryItemModel;

import java.util.ArrayList;


public class GalleryUtils {


    public static final String CAMERA_IMAGE_BUCKET_NAME
            = Environment.getExternalStorageDirectory().toString() + "/Download";


    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    public static ArrayList<GalleryItemModel> getImages(Context context) {

        final String[] projection = {MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = {GalleryUtils.getBucketId(CAMERA_IMAGE_BUCKET_NAME)};
        final Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        ArrayList<GalleryItemModel> result = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            final int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            do {
                GalleryItemModel galleryItemModel = new GalleryItemModel(
                        cursor.getString(dataColumn), cursor.getString(nameColumn));
                result.add(galleryItemModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
}
