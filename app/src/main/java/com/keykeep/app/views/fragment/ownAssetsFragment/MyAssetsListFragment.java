package com.keykeep.app.views.fragment.ownAssetsFragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeep.app.R;
import com.keykeep.app.databinding.MyAssetListFragmentBinding;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.adapter.AllAssetsAdapter;
import com.keykeep.app.views.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 23/8/18
 */

public class MyAssetsListFragment extends BaseFragment implements XRecyclerView.LoadingListener {

    private Context context;
    private MyAssetListFragmentBinding binding;
    MyAssetsFragmentViewModel viewModel;
    private AllAssetsAdapter myAssetAdapter;
    private ArrayList<AssetsListResponseBean.Result> resultArrayList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.my_asset_list_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(MyAssetsFragmentViewModel.class);
        binding.setViewModel(viewModel);
        initializeViews(binding.getRoot());
        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getMyAssets(binding);
        return binding.getRoot();

    }

    @Override
    public void initializeViews(View rootView) {
        resultArrayList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setLoadingListener(this);
        binding.recyclerView.setLoadingMoreEnabled(false);
        binding.recyclerView.setPullRefreshEnabled(true);
        myAssetAdapter = new AllAssetsAdapter(context, resultArrayList, AppUtils.STATUS_TRANSFER_ASSET_LIST);
        binding.recyclerView.setAdapter(myAssetAdapter);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(getActivity());
        binding.simpleSearchView.setQueryHint("Search here");
        binding.tvNoRecords.setVisibility(View.GONE);
        binding.simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myAssetAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")) {
                    Utils.hideSoftKeyboard(getActivity());
                    myAssetAdapter.setAssetList(getContext(), resultArrayList);
                } else {
                    try {
                        myAssetAdapter.getFilter().filter(newText);
                        myAssetAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
    }


    Observer<AssetsListResponseBean> response_observer = new Observer<AssetsListResponseBean>() {

        @Override
        public void onChanged(@Nullable AssetsListResponseBean assetsListResponseBean) {
            Utils.hideProgressDialog();
            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                resultArrayList = assetsListResponseBean.getResult();
                myAssetAdapter.setAssetList(getActivity(), resultArrayList);

            } else {
                noDataView();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).
                registerReceiver(aAssetListStatusReceiver,
                        new IntentFilter(AppUtils.IS_ASSET_LIST_AVAILABLE));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(aAssetListStatusReceiver);
    }


    public BroadcastReceiver aAssetListStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            boolean isAssetListAvailable = intent.getBooleanExtra(AppUtils.ASSET_AVAILABLE_STATUS, false);
            if (isAssetListAvailable) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.tvNoRecords.setVisibility(View.GONE);
            } else {
                noDataView();
            }
        }
    };

    @Override
    public void onRefresh() {
        viewModel.getMyAssets(binding);
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
        binding.tvNoRecords.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }

}
