package com.lotview.app.views.activity.transfer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lotview.app.R;
import com.lotview.app.databinding.ActivityTransferAssetBinding;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.adapter.TransferAssetAdapter;
import com.lotview.app.views.base.BaseActivity;
import com.lotview.app.views.custom_view.CustomActionBar;

/**
 * Created by akshaydashore on 31/8/18
 */
public class TransferActivity extends BaseActivity implements XRecyclerView.LoadingListener, TransferAssetAdapter.ActivityForResult {


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
        customActionBar.setActionbar(getString(R.string.transfer_asset), true, false,false,false, this);
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer_asset);
        viewModel = ViewModelProviders.of(this).get(TransferViewModel.class);
        binding.setViewModel(viewModel);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setLoadingListener(this);
        binding.recyclerView.setLoadingMoreEnabled(false);
        viewModel.validator.observe(this, observer);
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

            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                binding.recyclerView.setLayoutManager(manager);
                myAssetAdapter = new TransferAssetAdapter(context, assetsListResponseBean, TransferActivity.this);
                binding.recyclerView.setAdapter(myAssetAdapter);
            } else {
                noDataView();
            }
        }
    };

    Observer<Integer> observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.NO_INTERNET:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.internet_connection));
                    break;

                case AppUtils.SERVER_ERROR:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.server_error));
                    break;
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


    private void noDataView() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.noDataFountLayout.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }


    @Override
    public void onCallActivityResult(Intent intent) {
        startActivityForResult(intent, AppUtils.REQ_REFRESH_VIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtils.REQ_REFRESH_VIEW) {
            viewModel.getMyAssets(binding);
        }
    }

}

