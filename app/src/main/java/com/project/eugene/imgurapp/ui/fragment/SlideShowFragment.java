package com.project.eugene.imgurapp.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.project.eugene.imgurapp.R;
import com.project.eugene.imgurapp.adapter.GalleryStripAdapter;
import com.project.eugene.imgurapp.adapter.SlideShowPagerAdapter;
import com.project.eugene.imgurapp.model.GalleryItemModel;

import java.util.ArrayList;


public class SlideShowFragment extends DialogFragment implements GalleryStripAdapter.GalleryStripCallBacks {

    private static final String ARG_CURRENT_POSITION = "position";
    private static final String ARG_ITEMS_KEY = "ARG_ITEMS_KEY";

    private View parentView;
    private ViewPager mViewPagerGallery;
    private TextView textViewImageName;
    private RecyclerView recyclerViewGalleryStrip;
    private RecyclerView.LayoutManager mGalleryStripLayoutManger;

    private ArrayList<GalleryItemModel> galleryItemModels;
    private GalleryStripAdapter mGalleryStripAdapter;
    private int mCurrentPosition;
    boolean isBottomBarVisible = true;


    public SlideShowFragment() {
        // Required empty public constructor
    }

    public static SlideShowFragment newInstance(int position, ArrayList<GalleryItemModel> models) {

        SlideShowFragment fragment = new SlideShowFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION, position);
        args.putParcelableArrayList(ARG_ITEMS_KEY, models);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryItemModels = new ArrayList<>();
        if (getArguments() != null) {
            mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
            galleryItemModels = getArguments().getParcelableArrayList(ARG_ITEMS_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initViews(inflater, container);
        addListeners();

        return parentView;
    }

    private void initViews(@NonNull LayoutInflater inflater, ViewGroup container) {
        parentView = inflater.inflate(R.layout.fragment_silde_show, container, false);
        mViewPagerGallery = parentView.findViewById(R.id.viewPagerGallery);
        mViewPagerGallery.setAdapter(new SlideShowPagerAdapter(getContext(), galleryItemModels));
        mViewPagerGallery.setCurrentItem(mCurrentPosition);

        textViewImageName = parentView.findViewById(R.id.textViewImageName);
        textViewImageName.setText(galleryItemModels.get(mCurrentPosition).imageName);

        mGalleryStripAdapter = new GalleryStripAdapter(
                galleryItemModels, getContext(), this, mCurrentPosition);
        recyclerViewGalleryStrip = parentView.findViewById(R.id.recyclerViewGalleryStrip);

        mGalleryStripLayoutManger = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewGalleryStrip.setLayoutManager(mGalleryStripLayoutManger);
        recyclerViewGalleryStrip.setAdapter(mGalleryStripAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListeners() {
        mViewPagerGallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isBottomBarVisible) {
                        //bottom bar is visible make it invisible
                        FadeOutBottomBar();
                    } else {
                        //bottom bar is invisible make it visible
                        FadeInBottomBar();
                    }
                }
                return false;
            }
        });

        mViewPagerGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textViewImageName.setText(galleryItemModels.get(position).imageName);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //first check When Page is scrolled and gets stable
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //get current  item on parentView pager
                    int currentSelected = mViewPagerGallery.getCurrentItem();
                    //scroll strip smoothly to current  position of viewpager
                    mGalleryStripLayoutManger.smoothScrollToPosition(
                            recyclerViewGalleryStrip, null, currentSelected);
                    //select current item of viewpager on gallery strip at bottom
                    mGalleryStripAdapter.setSelected(currentSelected);
                }
            }
        });
    }

    @Override
    public void onGalleryStripItemSelected(int position) {
        //set current item of viewpager
        mViewPagerGallery.setCurrentItem(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //remove selection on destroy
        mGalleryStripAdapter.removeSelection();
    }

    //Method to fadeIn bottom bar which is image textview name
    public void FadeInBottomBar() {
        //define alpha animation
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        //set duration
        fadeIn.setDuration(1200);
        //set animation listener
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //set textview visible on animation ends
                textViewImageName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //start animation
        textViewImageName.startAnimation(fadeIn);
        isBottomBarVisible = true;
    }

    public void FadeOutBottomBar() {
        //define alpha animation
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        //set duration
        fadeOut.setDuration(1200);
        //set animation listener
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //set textview Visibility gone on animation ends
                textViewImageName.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //start animation
        textViewImageName.startAnimation(fadeOut);
        isBottomBarVisible = false;
    }
}
