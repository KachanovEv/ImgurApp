package com.project.eugene.imgurapp.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.project.eugene.imgurapp.R;
import com.project.eugene.imgurapp.ScreenSize.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter implements GalleryAdapterCallBacks {

    List<GalleryItemModel> galleryItems;
    Context context;

    GalleryAdapterCallBacks mAdapterCallBacks;

    public GalleryAdapter(Context context) {
        this.context = context;

        this.mAdapterCallBacks = (GalleryAdapterCallBacks) context;

        this.galleryItems = new ArrayList<>();
    }


    public void addGalleryItems(List<GalleryItemModel> galleryItems) {
        int previousSize = this.galleryItems.size();
        this.galleryItems.addAll(galleryItems);
        notifyItemRangeInserted(previousSize, galleryItems.size());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_row_gallery_item, parent, false);
        return new GalleryItemHolder(row);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        GalleryItemModel currentItem = galleryItems.get(position);

        File imageViewThoumb = new File(currentItem.imageUri);

        GalleryItemHolder galleryItemHolder = (GalleryItemHolder) holder;

        Picasso.get()
                .load(imageViewThoumb)
                .centerCrop()
                .resize(ScreenUtils.getScreenWidth(context) / 2, ScreenUtils.getScreenHeight(context) / 3)
                .into(galleryItemHolder.imageViewThumbnail);

        galleryItemHolder.textViewImageName.setText(currentItem.imageName);

        galleryItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAdapterCallBacks.onItemSelected(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    @Override
    public void onItemSelected(int position) {

    }

    public class GalleryItemHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewImageName;

        public GalleryItemHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewImageName = itemView.findViewById(R.id.textViewImageName);

        }
    }




}
