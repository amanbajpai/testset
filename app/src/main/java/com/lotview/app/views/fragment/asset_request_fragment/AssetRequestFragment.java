package com.lotview.app.views.fragment.asset_request_fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lotview.app.R;
import com.lotview.app.databinding.AssetRequestSendRecieveFragmentBinding;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.adapter.AssetRequestAdapter;
import com.lotview.app.views.base.BaseFragment;

/**
 * Created by ashishthakur on 29/8/18.
 */
public class AssetRequestFragment extends BaseFragment implements XRecyclerView.LoadingListener, AssetRequestAdapter.ActivityForResult {

    AssetRequestSendRecieveFragmentBinding binding;
    AssetRequestViewModel viewModel;
    private AssetRequestAdapter assetRequestAdapter;
    private int typeRequest;
    private boolean isSentRequestSelected = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.asset_request_send_recieve_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(AssetRequestViewModel.class);
        binding.setViewModel(viewModel);
        initializeViews(binding.getRoot());
        getListForWebservice();
        return binding.getRoot();
    }


    public void getListForWebservice() {
        Utils.showProgressDialog(getActivity(), getString(R.string.loading));
        viewModel.getAssetsPendingSendRequest(binding);
    }

    @Override
    public void initializeViews(View rootView) {
        typeRequest = AppUtils.STATUS_ASSET_SEND_REQUEST1;
        binding.recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setLoadingListener(this);
        binding.recyclerView.setLoadingMoreEnabled(false);
        binding.recyclerView.setPullRefreshEnabled(true);
        viewModel.response_validator.observe(this, response_observer);
        binding.tvPendingSendRequest.setOnClickListener(this);
        binding.tvPendingReceiveRequest.setOnClickListener(this);
        binding.tvPendingSendRequest.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.send_request_selector));
        binding.tvPendingReceiveRequest.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.received_request_deselector));
        binding.tvPendingReceiveRequest.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        binding.tvPendingSendRequest.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pending_send_request:
                isSentRequestSelected = true;
                binding.noDataFountLayout.setVisibility(View.GONE);
                Utils.showProgressDialog(getActivity(), getString(R.string.loading));
                binding.tvPendingSendRequest.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.send_request_selector));
                binding.tvPendingReceiveRequest.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.received_request_deselector));
                binding.tvPendingReceiveRequest.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                binding.tvPendingSendRequest.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                typeRequest = AppUtils.STATUS_ASSET_SEND_REQUEST1;
                viewModel.getAssetsPendingSendRequest(binding);
                break;
            case R.id.tv_pending_receive_request:
                isSentRequestSelected = false;
                binding.noDataFountLayout.setVisibility(View.GONE);
                Utils.showProgressDialog(getActivity(), getString(R.string.loading));
                binding.tvPendingSendRequest.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.send_request_deselector));
                binding.tvPendingReceiveRequest.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.received_request_selector));
                binding.tvPendingSendRequest.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                binding.tvPendingReceiveRequest.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                typeRequest = AppUtils.STATUS_ASSET_SEND_REQUEST;
                viewModel.getAssetsPendingRecieveRequest(binding);
                break;
        }
    }

    Observer<AssetsListResponseBean> response_observer = new Observer<AssetsListResponseBean>() {

        @Override
        public void onChanged(@Nullable AssetsListResponseBean assetsListResponseBean) {
            Utils.hideProgressDialog();
            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                assetRequestAdapter = new AssetRequestAdapter(getActivity(), assetsListResponseBean, typeRequest);
                binding.recyclerView.setAdapter(assetRequestAdapter);
                if (typeRequest == AppUtils.STATUS_ASSET_SEND_REQUEST1) {
                    assetRequestAdapter.setListener(AssetRequestFragment.this);
                }
            } else {
                noDataView();
            }
        }
    };

    @Override
    public void onRefresh() {
        if (isSentRequestSelected) {
            viewModel.getAssetsPendingSendRequest(binding);
        } else {
            viewModel.getAssetsPendingRecieveRequest(binding);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {

    }


    @Override
    public void onCallActivityResult(Intent intent) {
        startActivityForResult(intent, AppUtils.REQ_REFRESH_VIEW);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtils.REQ_REFRESH_VIEW) {
            getListForWebservice();
        }
    }

    private void noDataView() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.noDataFountLayout.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }

}
