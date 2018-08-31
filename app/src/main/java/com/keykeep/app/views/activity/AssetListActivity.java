package com.keykeep.app.views.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.keykeep.app.R;
import com.keykeep.app.views.base.BaseActivity;
import com.keykeep.app.views.custom_view.CustomActionBar;
import com.keykeep.app.views.fragment.all_assets_list.AllAssetListFragment;
import com.keykeep.app.views.fragment.ownAssetsFragment.MyAssetsListFragment;

/**
 * Created by akshaydashore on 23/8/18
 */
public class AssetListActivity extends BaseActivity {
    private String TAG = "AssetListActivity";
    private AppCompatTextView allAssetBtn,myAssetBtn;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_activity_layout);
        context=AssetListActivity.this;
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
        replaceFragment(false, new AllAssetListFragment(), R.id.home_layout_container);
        allAssetBtn = findViewById(R.id.tv_all_assets);
        myAssetBtn = findViewById(R.id.tv_my_assets);
        allAssetBtn.setOnClickListener(this);
        myAssetBtn.setOnClickListener(this);
        allAssetBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.all_asset_selector));
        myAssetBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_asset_deselector));
        myAssetBtn.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        allAssetBtn.setTextColor(ContextCompat.getColor(context, R.color.white));
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
