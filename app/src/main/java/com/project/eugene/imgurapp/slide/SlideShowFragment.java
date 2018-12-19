package com.project.eugene.imgurapp.slide;


import android.os.Bundle;
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
import com.project.eugene.imgurapp.base.MainActivity;
import com.project.eugene.imgurapp.gallery.GalleryItemModel;
import com.project.eugene.imgurapp.gallery.GalleryStripAdapter;

import java.util.ArrayList;
import java.util.List;



public class SlideShowFragment extends DialogFragment implements GalleryStripAdapter.GalleryStripCallBacks {

    private static final String ARG_CURRENT_POSITION = "position";

    List<GalleryItemModel> galleryItems;

    GalleryStripAdapter mGalleryStripAdapter;

    SlideShowPagerAdapter mSlideShowPagerAdapter;

    ViewPager mViewPagerGallery;
    TextView textViewImageName;
    RecyclerView recyclerViewGalleryStrip;

    private int mCurrentPosition;

    boolean isBottomBarVisible = true;


    public SlideShowFragment() {

    }


    public static SlideShowFragment newInstance(int position) {
        SlideShowFragment fragment = new SlideShowFragment();

        Bundle args = new Bundle();

        args.putInt(ARG_CURRENT_POSITION, position);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleryItems = new ArrayList<>();
        if (getArguments() != null) {

            mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);

            galleryItems = ((MainActivity) getActivity()).galleryItems;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_silde_show, container, false);
        mViewPagerGallery = view.findViewById(R.id.viewPagerGallery);

        mViewPagerGallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isBottomBarVisible) {

                        FadeOutBottomBar();
                    } else {

                        FadeInBottomBar();
                    }
                }
                return false;
            }

        });
        textViewImageName = view.findViewById(R.id.textViewImageName);

        mSlideShowPagerAdapter = new SlideShowPagerAdapter(getContext(), galleryItems);

        mViewPagerGallery.setAdapter(mSlideShowPagerAdapter);
        recyclerViewGalleryStrip = view.findViewById(R.id.recyclerViewGalleryStrip);

        final RecyclerView.LayoutManager mGalleryStripLayoutManger = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewGalleryStrip.setLayoutManager(mGalleryStripLayoutManger);

        mGalleryStripAdapter = new GalleryStripAdapter(galleryItems, getContext(), this, mCurrentPosition);

        recyclerViewGalleryStrip.setAdapter(mGalleryStripAdapter);

        mViewPagerGallery.setCurrentItem(mCurrentPosition);

        textViewImageName.setText(galleryItems.get(mCurrentPosition).imageName);

        mViewPagerGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                textViewImageName.setText(galleryItems.get(position).imageName);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {

                    int currentSelected = mViewPagerGallery.getCurrentItem();

                    mGalleryStripLayoutManger.smoothScrollToPosition(recyclerViewGalleryStrip, null, currentSelected);

                    mGalleryStripAdapter.setSelected(currentSelected);

                }

            }
        });
        return view;
    }


    @Override
    public void onGalleryStripItemSelected(int position) {

        mViewPagerGallery.setCurrentItem(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mGalleryStripAdapter.removeSelection();
    }


    public void FadeInBottomBar() {

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);

        fadeIn.setDuration(1200);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                textViewImageName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textViewImageName.startAnimation(fadeIn);
        isBottomBarVisible = true;
    }

    public void FadeOutBottomBar() {

        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);

        fadeOut.setDuration(1200);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                textViewImageName.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textViewImageName.startAnimation(fadeOut);
        isBottomBarVisible = false;

    }
}
