package com.project.eugene.imgurapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.eugene.imgurapp.model.GalleryItemModel;
import com.project.eugene.imgurapp.R;
import com.project.eugene.imgurapp.util.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter {
    //Declare GalleryItems List
    List<GalleryItemModel> galleryItemModels;
    Context context;
    //Declare GalleryAdapterCallBacks
    GalleryAdapterCallBacks mAdapterCallBacks;

    public GalleryAdapter(Context context) {
        this.context = context;
        //get GalleryAdapterCallBacks from contex
        this.mAdapterCallBacks = (GalleryAdapterCallBacks) context;
        //Initialize GalleryItemModel List
        this.galleryItemModels = new ArrayList<>();
    }

    //This method will take care of adding new Gallery items to RecyclerView
    public void addGalleryItems(List<GalleryItemModel> galleryItemModels) {
        int previousSize = this.galleryItemModels.size();
        this.galleryItemModels.addAll(galleryItemModels);
        notifyItemRangeInserted(previousSize, galleryItemModels.size());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_row_gallery_item, parent, false);
        return new GalleryItemHolder(row);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //get current Gallery Item
        GalleryItemModel currentItem = galleryItemModels.get(position);
        //Create file to load with Picasso lib
        File imageViewThoumb = new File(currentItem.imageUri);
        //cast holder with gallery holder
        GalleryItemHolder galleryItemHolder = (GalleryItemHolder) holder;
        //Load with Picasso
        Picasso.get()
                .load(imageViewThoumb)
                .centerCrop()
                .resize(ScreenUtils.getScreenWidth(context) / 2, ScreenUtils.getScreenHeight(context) / 3)
                .into(galleryItemHolder.imageViewThumbnail);
        //set name of Image
        galleryItemHolder.textViewImageName.setText(currentItem.imageName);
        //set on click listener on imageViewThumbnail
        galleryItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call onItemSelected method and pass the position and let activity decide what to do when item selected
                mAdapterCallBacks.onItemSelected(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return galleryItemModels.size();
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

    //Interface for communication of Adapter and MainActivity
    public interface GalleryAdapterCallBacks {
        //call this method to notify about item is clicked
        void onItemSelected(int position);
    }


}
