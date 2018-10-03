package com.keykeeper.app.views.fragment.all_assets_list;

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
import com.keykeeper.app.R;
import com.keykeeper.app.databinding.AllAssetListFragmentBinding;
import com.keykeeper.app.model.bean.AssetsListResponseBean;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.adapter.AllAssetsAdapter;
import com.keykeeper.app.views.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 23/8/18
 */
public class AllAssetListFragment extends BaseFragment implements XRecyclerView.LoadingListener, AllAssetsAdapter.OnActivityResult {

    AllAssetListFragmentViewModel viewModel;
    private Context context;
    private AllAssetListFragmentBinding binding;
    private AllAssetsAdapter allAssetAdapter;
    private ArrayList<AssetsListResponseBean.Result> resultArrayList;

    Observer<AssetsListResponseBean> response_observer = new Observer<AssetsListResponseBean>() {

        @Override
        public void onChanged(@Nullable AssetsListResponseBean assetsListResponseBean) {

            Utils.hideProgressDialog();
            if (assetsListResponseBean != null && assetsListResponseBean.getResult() != null && assetsListResponseBean.getResult().size() > 0) {
                resultArrayList = assetsListResponseBean.getResult();
                allAssetAdapter.setAssetList(getActivity(), resultArrayList);

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.all_asset_list_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(AllAssetListFragmentViewModel.class);
        binding.setViewModel(viewModel);
        initializeViews(binding.getRoot());
        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getAllAssets(binding, "");
        return binding.getRoot();
    }

    @Override
    public void initializeViews(View rootView) {
        resultArrayList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(manager);
        allAssetAdapter = new AllAssetsAdapter(context, resultArrayList, AppUtils.STATUS_ALL_ASSET_LIST, this);
        binding.recyclerView.setAdapter(allAssetAdapter);
        binding.recyclerView.setLoadingListener(this);
        binding.recyclerView.setLoadingMoreEnabled(false);
        binding.recyclerView.setPullRefreshEnabled(true);

        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);

        Utils.hideSoftKeyboard(getActivity());
        binding.simpleSearchView.setQueryHint("Search here");

        binding.simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                allAssetAdapter.notifyDataSetChanged();
                //  Added Search API here
                viewModel.getAllAssets(binding, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")) {
                    Utils.hideSoftKeyboard(getActivity());
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.noDataFountLayout.setVisibility(View.GONE);
                    allAssetAdapter.setAssetList(getContext(), resultArrayList);
                    viewModel.getAllAssets(binding, "");
                } else {
                    // Comitted for Local search
//                    try {
//                        allAssetAdapter.getFilter().filter(newText);
//                        allAssetAdapter.notifyDataSetChanged();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

                return false;
            }
        });
    }


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

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onRefresh() {

        if (Connectivity.isConnected()) {
            Utils.showProgressDialog(context, getString(R.string.loading));
            resultArrayList.clear();
            viewModel.getAllAssets(binding, "");
        } else {
            Utils.showSnackBar(binding, getString(R.string.internet_connection));
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
        Log.e("onLoadMore: ", "call load more");
    }


    public BroadcastReceiver aAssetListStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            boolean isAssetListAvailable = intent.getBooleanExtra(AppUtils.ASSET_AVAILABLE_STATUS, false);
            if (isAssetListAvailable) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.noDataFountLayout.setVisibility(View.GONE);
            } else {
                noDataView();
            }
        }
    };

    private void noDataView() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.noDataFountLayout.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }

    @Override
    public void CallOnActivityResult(Intent intent) {
        startActivityForResult(intent, AppUtils.REQ_REFRESH_VIEW);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtils.REQ_REFRESH_VIEW) {
            resultArrayList.clear();
            Utils.showProgressDialog(context, getString(R.string.loading));
            viewModel.getAllAssets(binding, "");
        }
    }
}
