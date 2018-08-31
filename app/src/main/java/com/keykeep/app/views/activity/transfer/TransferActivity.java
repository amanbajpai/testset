package com.keykeep.app.views.activity.transfer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeep.app.R;
import com.keykeep.app.databinding.ActivityTransferAssetBinding;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.adapter.TransferAssetAdapter;
import com.keykeep.app.views.base.BaseActivity;
import com.keykeep.app.views.custom_view.CustomActionBar;

/**
 * Created by akshaydashore on 31/8/18
 */
public class TransferActivity extends BaseActivity implements XRecyclerView.LoadingListener {


    private Context context;
    private ActivityTransferAssetBinding binding;
    TransferViewModel viewModel;
    private TransferAssetAdapter myAssetAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setCustomActionBar();
        initializeViews();
    }


    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.transfer_asset), true, false, this);
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer_asset);
        viewModel = ViewModelProviders.of(this).get(TransferViewModel.class);
        binding.setViewModel(viewModel);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setLoadingListener(this);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(this);

        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getMyAssets(binding);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.left_iv:
                finish();
                break;

        }
    }


    Observer<AssetsListResponseBean> response_observer = new Observer<AssetsListResponseBean>() {

        @Override
        public void onChanged(@Nullable AssetsListResponseBean assetsListResponseBean) {

            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null) {
                LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                binding.recyclerView.setLayoutManager(manager);
                myAssetAdapter = new TransferAssetAdapter(context, assetsListResponseBean);
                binding.recyclerView.setAdapter(myAssetAdapter);
            }
        }
    };

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        Log.e("onLoadMore: ", "call load more");
    }
}

