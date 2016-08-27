package com.codepath.timeline.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.timeline.R;
import com.codepath.timeline.fragments.AutoPlayFragment;
import com.codepath.timeline.models.Moment;
import com.codepath.timeline.models.Story;
import com.codepath.timeline.network.TimelineClient;
import com.qslll.library.ExpandingPagerFactory;
import com.qslll.library.ExpandingViewPagerAdapter;
import com.qslll.library.fragments.ExpandingFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/*
      Expanding view for displaying list of moments: https://github.com/qs-lll/ExpandingPager
      Automatic scroll used for auto-playing list of moments: https://github.com/Trinea/android-auto-scroll-view-pager
 */
public class AutoPlayActivity extends AppCompatActivity
    implements ExpandingFragment.OnExpandingClickListener {

  @BindView(R.id.vpMoment)
  AutoScrollViewPager vpMoment;
  @BindView(R.id.collapsing_toolbar)
  CollapsingToolbarLayout collapsing_toolbar;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.ivStoryBackground)
  ImageView ivStoryBackground;

  private List<Moment> mMomentList;
  private Moment mMoment;
  private AutoPlayPagerAdapter mPagerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auto_play);
    ButterKnife.bind(this);

    toolbar.setTitle("");
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    getMomentList();
    initView();
  }

  private void initView() {
    // Todo: Use the story extracted from the intent
    collapsing_toolbar.setTitle("Baby Matthew Smith");
    collapsing_toolbar.setCollapsedTitleTextColor(Color.WHITE);

    // load the image url for the background of the story into the image view
    // Todo: Change drawable to the image loaded from the intent
    String imageUrl = getIntent().getStringExtra("imageUrl");
    Glide.with(this)
        .load(R.drawable.image_test2)
        .centerCrop()
        .into(ivStoryBackground);

    mPagerAdapter = new AutoPlayPagerAdapter(getSupportFragmentManager());
    vpMoment.setAdapter(mPagerAdapter);

    ExpandingPagerFactory.setupViewPager(vpMoment);
    vpMoment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        ExpandingFragment expandingFragment = ExpandingPagerFactory.getCurrentFragment(vpMoment);
        if (expandingFragment != null && expandingFragment.isOpenend()) {
          expandingFragment.close();
        }
      }

      @Override
      public void onPageSelected(int position) {
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });

    // initialize viewpager for automatically scrolling through the list of moments
    vpMoment.setInterval(2000);
    vpMoment.startAutoScroll();
    vpMoment.setCurrentItem(0);
  }

  private void getMomentList() {
    mMomentList = new ArrayList<>();
    mMomentList.addAll(TimelineClient.getInstance().getMomentsList(this, -1));
  }

  @Override
  public void onBackPressed() {
    if (!ExpandingPagerFactory.onBackPressed(vpMoment)) {
      super.onBackPressed();
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void setupWindowAnimations() {
    Explode slideTransition = new Explode();
    getWindow().setReenterTransition(slideTransition);
    getWindow().setExitTransition(slideTransition);
  }

  @SuppressWarnings("unchecked")
  private void startInfoActivity(View view, Moment moment) {
    Activity activity = this;
    ActivityCompat.startActivity(activity,
        AutoPlayInfoActivity.newInstance(activity, moment),
        ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity,
            new Pair<>(view, getString(R.string.transition_image)))
            .toBundle());
  }

  @Override
  public void onExpandingClick(View v) {
    //v is expandingfragment layout
    View view = v.findViewById(R.id.ivMedia);
    Moment moment = mMomentList.get(vpMoment.getCurrentItem());
    startInfoActivity(view, moment);
  }

  private class AutoPlayPagerAdapter extends ExpandingViewPagerAdapter {
    public AutoPlayPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      mMoment = mMomentList.get(position);
      return AutoPlayFragment.newInstance(mMoment);
    }

    @Override
    public int getCount() {
      return mMomentList.size();
    }
  }
}
