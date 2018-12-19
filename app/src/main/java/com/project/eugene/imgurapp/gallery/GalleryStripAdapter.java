package com.project.eugene.imgurapp.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.eugene.imgurapp.R;
import com.project.eugene.imgurapp.ScreenSize.ScreenUtils;
import com.project.eugene.imgurapp.SquareLayout.SquareLayout;

import java.util.List;


public class GalleryStripAdapter extends RecyclerView.Adapter {

    List<GalleryItemModel> galleryItems;
    Context context;
    GalleryStripCallBacks mStripCallBacks;
    GalleryItemModel mCurrentSelected;

    final int thumbSize = 6;

    public GalleryStripAdapter(List<GalleryItemModel> galleryItems, Context context, GalleryStripCallBacks StripCallBacks, int CurrentPosition) {

        this.galleryItems = galleryItems;
        this.context = context;

        this.mStripCallBacks = StripCallBacks;

        mCurrentSelected = galleryItems.get(CurrentPosition);

        mCurrentSelected.isSelected = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_row_gallery_strip_item, parent, false);
        SquareLayout squareLayout = row.findViewById(R.id.squareLayout);
        return new GalleryStripItemHolder(squareLayout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        GalleryItemModel mCurrentItem = galleryItems.get(position);



        GalleryStripItemHolder galleryStripItemHolder = (GalleryStripItemHolder) holder;

        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mCurrentItem.imageUri),
                thumbSize, thumbSize);

        galleryStripItemHolder.imageViewThumbnail.setImageBitmap(ThumbImage);

        if (mCurrentItem.isSelected) {
            galleryStripItemHolder.imageViewThumbnail.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        } else {

            galleryStripItemHolder.imageViewThumbnail.setBackgroundColor(0);
        }
        galleryStripItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mStripCallBacks.onGalleryStripItemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public class GalleryStripItemHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;

        public GalleryStripItemHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
        }
    }


    public interface GalleryStripCallBacks {
        void onGalleryStripItemSelected(int position);
    }


    public void setSelected(int position) {

        mCurrentSelected.isSelected = false;

        notifyItemChanged(galleryItems.indexOf(mCurrentSelected));

        galleryItems.get(position).isSelected = true;

        notifyItemChanged(position);

        mCurrentSelected = galleryItems.get(position);

    }


    public void removeSelection() {
        mCurrentSelected.isSelected = false;
    }


}
