package com.keykeeper.app.views.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.keykeeper.app.R;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;
import com.keykeeper.app.views.fragment.all_assets_list.AllAssetListFragment;
import com.keykeeper.app.views.fragment.ownAssetsFragment.MyAssetsListFragment;

/**
 * Created by akshaydashore on 23/8/18
 */
public class AssetListActivity extends BaseActivity {
    private AppCompatTextView allAssetBtn, myAssetBtn;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_activity_layout);
        context = AssetListActivity.this;
        setCustomActionBar();
        initializeViews();
    }


    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.actionbar_keys), true, false,false, false, false, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void initializeViews() {
        replaceFragment(false, new MyAssetsListFragment(), R.id.home_layout_container);
        allAssetBtn = findViewById(R.id.tv_all_assets);
        myAssetBtn = findViewById(R.id.tv_my_assets);
        allAssetBtn.setOnClickListener(this);
        myAssetBtn.setOnClickListener(this);

        myAssetBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_asset_selector));
        allAssetBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.all_asset_deselector));
        allAssetBtn.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        myAssetBtn.setTextColor(ContextCompat.getColor(context, R.color.white));

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.left_iv:
                finish();
                break;
            case R.id.tv_all_assets:
                allAssetBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.all_asset_selector));
                myAssetBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_asset_deselector));
                myAssetBtn.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                allAssetBtn.setTextColor(ContextCompat.getColor(context, R.color.white));
                replaceFragment(false, new AllAssetListFragment(), R.id.home_layout_container);
                break;

            case R.id.tv_my_assets:
                allAssetBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.all_asset_deselector));
                myAssetBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_asset_selector));
                allAssetBtn.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                myAssetBtn.setTextColor(ContextCompat.getColor(context, R.color.white));
                replaceFragment(false, new MyAssetsListFragment(), R.id.home_layout_container);
                break;
        }
    }

}
