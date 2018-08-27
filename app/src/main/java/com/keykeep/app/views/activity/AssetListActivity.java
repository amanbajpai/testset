package com.keykeep.app.views.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.keykeep.app.R;
import com.keykeep.app.views.adapter.AssetPagerAdapter;
import com.keykeep.app.views.base.BaseActivity;
import com.keykeep.app.views.custom_view.CustomActionBar;
import com.keykeep.app.views.fragment.AllAssetListFragment;
import com.keykeep.app.views.fragment.OwnAssetFragment;

/**
 * Created by akshaydashore on 23/8/18
 */
public class AssetListActivity extends BaseActivity {

    private TabLayout tab_layout;
    private boolean isClicked;
    private String TAG = "AssetListActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_activity_layout);
        setCustomActionBar();
        initializeViews();
    }


    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar("Assets", true, false, this);
    }

    @Override
    public void initializeViews() {
        tab_layout = findViewById(R.id.tab_layout);
        ViewPager pager = findViewById(R.id.viewPager);
        AssetPagerAdapter assetPagerAdapter = new AssetPagerAdapter(getSupportFragmentManager());
        assetPagerAdapter.addFragment(new AllAssetListFragment(), "All Assets");
        assetPagerAdapter.addFragment(new OwnAssetFragment(), "Own Assets");

        pager.setAdapter(assetPagerAdapter);

        tab_layout.setupWithViewPager(pager);
        View root = tab_layout.getChildAt(0);

        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.black));
            drawable.setSize(1, 1);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "" + tab.getPosition());
                if (!isClicked) {
                    isClicked = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isClicked = false;
                        }
                    }, 1000);
                }
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.left_iv:
                finish();
                break;
        }
    }

}
