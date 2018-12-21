package com.project.eugene.imgurapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.eugene.imgurapp.R;
import com.project.eugene.imgurapp.model.GalleryItemModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class SlideShowPagerAdapter extends PagerAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<GalleryItemModel> galleryItemModels;

    public SlideShowPagerAdapter(Context context, ArrayList<GalleryItemModel> galleryItemModels) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.galleryItemModels = galleryItemModels;
    }

    @Override
    public int getCount() {
        return galleryItemModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ImageView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewThumbnail);
        //load current image in viewpager
        Picasso.get().load(new File(galleryItemModels.get(position).imageUri)).fit().into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}