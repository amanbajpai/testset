package com.keykeep.app.views.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.keykeep.app.R;
import com.keykeep.app.views.base.BaseActivity;
import com.keykeep.app.views.custom_view.CustomActionBar;
import com.keykeep.app.views.fragment.all_assets_list.AllAssetListFragment;
import com.keykeep.app.views.fragment.ownAssetsFragment.MyAssetsFragment;

/**
 * Created by akshaydashore on 23/8/18
 */
public class AssetListActivity extends BaseActivity {

    public static final int ALL_ASSETS_LIST = 0;
    public static final int MY_ASSETS_LIST = 1;
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
        tab_layout.addTab(tab_layout.newTab().setText("All Assets"));
        tab_layout.addTab(tab_layout.newTab().setText("My Assets"));

        replaceFragment(false, new AllAssetListFragment(), R.id.home_layout_container);

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
                switch (tab.getPosition()) {
                    case ALL_ASSETS_LIST:
                        replaceFragment(false, new AllAssetListFragment(), R.id.home_layout_container);
                        break;
                    case MY_ASSETS_LIST:
                        replaceFragment(false, new MyAssetsFragment(), R.id.home_layout_container);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
