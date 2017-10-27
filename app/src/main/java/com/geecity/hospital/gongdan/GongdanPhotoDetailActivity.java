package com.geecity.hospital.gongdan;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geecity.hospital.R;
import com.geecity.hospital.base.AbsBaseActivity;
import com.geecity.hospital.bean.JiedianBean;
import com.jaydenxiao.common.commonutils.SystemUiVisibilityUtil;
import com.jaydenxiao.common.commonwidget.HackyViewPager;

import java.util.List;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 工单图片详情
 */
public class GongdanPhotoDetailActivity extends AbsBaseActivity {

    private static final String PHOTO_DETAIL = "photo_detail";
    private static final String CURRENT_TAB_IDX = "current_tab_idx";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.photo_viewpager)
    HackyViewPager mHackyViewPager;

    @Bind(R.id.photo_text_layout)
    FrameLayout mPhotoTextLayout;

    @Bind(R.id.photo_count_tv)
    TextView mPhotoCountTv;

    @Bind(R.id.photo_news_title_tv)
    TextView mPhotoTitleTv;

    @Bind(R.id.photo_news_desc_tv)
    TextView mPhotoDescTv;

    private int mCurrentTabIdx = 0;
    private JiedianBean mJiedianDetail;
    private List<String> mPictureList;

    private boolean isHidden = false;
    private boolean mIsStatusBarHidden = false;

    public static Intent deliverPhotoDetailIntent(Context context, int idx, JiedianBean jiedianBean) {
        Intent intent = new Intent(context, GongdanPhotoDetailActivity.class);
        intent.putExtra(CURRENT_TAB_IDX, idx);
        intent.putExtra(PHOTO_DETAIL, jiedianBean);
        return intent;
    }

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_gongdan_photo_detail;
    }

    @Override
    protected void parseIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mCurrentTabIdx = intent.getIntExtra(CURRENT_TAB_IDX, 0);
        mJiedianDetail = intent.getParcelableExtra(PHOTO_DETAIL);
        mPictureList = mJiedianDetail.getPhotos();
    }

    @Override
    protected void initUi() {
        mPhotoTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mPhotoTitleTv.setText(mJiedianDetail.getTitle());
        mPhotoDescTv.setText(mJiedianDetail.getCzcontent());
        mPhotoCountTv.setText(mCurrentTabIdx + "/" + mPictureList.size());
        mHackyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPhotoCountTv.setText((position + 1) + "/" + mPictureList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mHackyViewPager.setAdapter(new PhotoPagerAdapter());
        mHackyViewPager.setCurrentItem(mCurrentTabIdx);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void initListener() {

    }

    private void hideOrShowStatusBar() {
        if (mIsStatusBarHidden) {
            SystemUiVisibilityUtil.enter(GongdanPhotoDetailActivity.this);
        } else {
            SystemUiVisibilityUtil.exit(GongdanPhotoDetailActivity.this);
        }
        mIsStatusBarHidden = !mIsStatusBarHidden;
    }

    public void hideToolBarAndTextView() {
        isHidden = !isHidden;
        if (isHidden) {
            startAnimation(true, 1.0f, 0.0f);
        } else {
            startAnimation(false, 0.1f, 1.0f);
        }
    }

    private void startAnimation(final boolean endState, float startValue, float endValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(startValue, endValue).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y1, y2;
                if (endState) {
                    y1 = (0 - animation.getAnimatedFraction()) * mToolbar.getHeight();
                    y2 = animation.getAnimatedFraction() * mPhotoTextLayout.getHeight();
                } else {
                    y1 = (animation.getAnimatedFraction() - 1) * mToolbar.getHeight();
                    y2 = (1 - animation.getAnimatedFraction()) * mPhotoTextLayout.getHeight();
                }
                mToolbar.setTranslationY(y1);
                mPhotoTextLayout.setTranslationY(y2);
            }
        });
        animator.start();
    }

    class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPictureList == null ? 0 : mPictureList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(GongdanPhotoDetailActivity.this);

            Glide.with(GongdanPhotoDetailActivity.this)
                    .load(mPictureList.get(position))
                    .placeholder(com.jaydenxiao.common.R.drawable.ic_image_loading)
                    .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photoView);

            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    hideToolBarAndTextView();
                    hideOrShowStatusBar();
                }
            });
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            return photoView;
        }
    }
}
