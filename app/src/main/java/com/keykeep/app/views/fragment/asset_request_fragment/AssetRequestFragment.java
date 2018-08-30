package com.keykeep.app.views.fragment.asset_request_fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeep.app.R;
import com.keykeep.app.databinding.AssetRequestSendRecieveFragmentBinding;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.adapter.AssetRequestAdapter;
import com.keykeep.app.views.base.BaseFragment;

import static android.content.ContentValues.TAG;

/**
 * Created by ashishthakur on 29/8/18.
 */
public class AssetRequestFragment extends BaseFragment implements XRecyclerView.LoadingListener {

    AssetRequestSendRecieveFragmentBinding binding;
    AssetRequestViewModel viewModel;
    public static final int ASSET_PENDING_SEND_REQUEST = 0;
    public static final int ASSET_PENDING_RECIEVE_REQUEST = 1;
    private AssetRequestAdapter assetRequestAdapter;
    private int typeRequest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.asset_request_send_recieve_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(AssetRequestViewModel.class);
        binding.setViewModel(viewModel);
        initializeViews(binding.getRoot());
        viewModel.getAssetsPendingSendRequest(binding);

        return binding.getRoot();
    }

    @Override
    public void initializeViews(View rootView) {
        typeRequest = AppUtils.STATUS_ASSET_SEND_REQUEST;
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.txt_tab_title_pending_send_request)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.txt_tab_title_pending_recieve_request)));
        View root = binding.tabLayout.getChildAt(0);

        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.black));
            drawable.setSize(1, 1);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
        binding.recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setLoadingListener(this);
        viewModel.response_validator.observe(this, response_observer);

        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "" + tab.getPosition());
                switch (tab.getPosition()) {
                    case ASSET_PENDING_SEND_REQUEST:
                        typeRequest = AppUtils.STATUS_ASSET_SEND_REQUEST;
                        viewModel.getAssetsPendingSendRequest(binding);
                        break;
                    case ASSET_PENDING_RECIEVE_REQUEST:
                        typeRequest = AppUtils.STATUS_ASSET_RECEIVE_REQUEST;
                        viewModel.getAssetsPendingRecieveRequest(binding);
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
    public void onClick(View v) {
    }

    Observer<AssetsListResponseBean> response_observer = new Observer<AssetsListResponseBean>() {

        @Override
        public void onChanged(@Nullable AssetsListResponseBean assetsListResponseBean) {

            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                assetRequestAdapter = new AssetRequestAdapter(getActivity(), assetsListResponseBean, typeRequest);
                binding.recyclerView.setAdapter(assetRequestAdapter);
            } else {
                binding.recyclerView.setVisibility(View.GONE);
                Utils.showToast(getActivity(), assetsListResponseBean.getMessage());
                // binding.tvNoDataAvailable.setText(getString(R.string.txt_no_data_available));
            }
        }
    };

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
