package com.project.eugene.imgurapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.project.eugene.imgurapp.R;
import com.project.eugene.imgurapp.adapter.GalleryAdapter;
import com.project.eugene.imgurapp.model.GalleryItemModel;
import com.project.eugene.imgurapp.ui.fragment.SlideShowFragment;
import com.project.eugene.imgurapp.util.GalleryUtils;

import java.util.ArrayList;

import com.project.eugene.imgurapp.service.GetImageService;

public class MainActivity extends AppCompatActivity implements GalleryAdapter.GalleryAdapterCallBacks {

    private static final int RC_READ_STORAGE = 5;
    public static final int SPAN_COUNT = 3;

    private ArrayList<GalleryItemModel> galleryItemModels;
    private GalleryAdapter mGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        checkPermissionsAndGetImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetImageService();
    }

    private void initViews() {
        mGalleryAdapter = new GalleryAdapter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Album");
        setSupportActionBar(toolbar);

        RecyclerView recyclerViewGallery = (RecyclerView) findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        recyclerViewGallery.setAdapter(mGalleryAdapter);
    }

    private void checkPermissionsAndGetImages() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            galleryItemModels = GalleryUtils.getImages(this);
            mGalleryAdapter.addGalleryItems(galleryItemModels);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_READ_STORAGE);
        }
    }

    @Override
    public void onItemSelected(int position) {
        SlideShowFragment slideShowFragment = SlideShowFragment.newInstance(position, galleryItemModels);
        slideShowFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        slideShowFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryItemModels = GalleryUtils.getImages(this);
                mGalleryAdapter.addGalleryItems(galleryItemModels);
            } else {
                Toast.makeText(this, "Storage Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startGetImageService() {
        Intent intent = new Intent(this, GetImageService.class);
        startService(intent);
    }
}
