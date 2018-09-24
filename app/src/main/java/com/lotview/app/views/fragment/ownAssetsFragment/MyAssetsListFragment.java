package com.lotview.app.views.fragment.ownAssetsFragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lotview.app.R;
import com.lotview.app.databinding.MyAssetListFragmentBinding;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.adapter.MyAssetsAdapter;
import com.lotview.app.views.base.BaseFragment;
import com.lotview.app.views.services.LocationListenerService;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 23/8/18
 */

public class MyAssetsListFragment extends BaseFragment implements XRecyclerView.LoadingListener,MyAssetsAdapter.OnActivityResult {

    private Context context;
    private MyAssetListFragmentBinding binding;
    MyAssetsFragmentViewModel viewModel;
    private MyAssetsAdapter myAssetAdapter;
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
        viewModel.getMyAssets(binding, AppSharedPrefs.getInstance(context).getEmployeeID(), "");
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
        myAssetAdapter = new MyAssetsAdapter(context, resultArrayList, AppUtils.STATUS_TRANSFER_ASSET_LIST,this);
        binding.recyclerView.setAdapter(myAssetAdapter);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(getActivity());
        binding.simpleSearchView.setQueryHint("Search here");
        binding.noDataFountLayout.setVisibility(View.GONE);

        binding.simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myAssetAdapter.notifyDataSetChanged();
                viewModel.getMyAssets(binding, AppSharedPrefs.getInstance(context).getEmployeeID(), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equalsIgnoreCase("")) {
                    Utils.hideSoftKeyboard(getActivity());
                    Utils.hideSoftKeyboard(getActivity());
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.noDataFountLayout.setVisibility(View.GONE);
                    myAssetAdapter.setAssetList(getContext(), resultArrayList);
                    viewModel.getMyAssets(binding, AppSharedPrefs.getInstance(context).getEmployeeID(), "");
                } else {
//                    try {
//                        myAssetAdapter.getFilter().filter(newText);
//                        myAssetAdapter.notifyDataSetChanged();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

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
                storeOwnedKeyIdsPreferences(assetsListResponseBean);
                  startLocationStorage();             // Enable this line to start location  Storage to databse need to test with API submission

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
                binding.noDataFountLayout.setVisibility(View.GONE);
            } else {
                stopLocationStorage();
                noDataView();
            }
        }
    };

    @Override
    public void onRefresh() {

        Utils.showProgressDialog(context, getString(R.string.loading));
        resultArrayList.clear();
        viewModel.getMyAssets(binding, AppSharedPrefs.getInstance(context).getEmployeeID(), "");
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

    private void startLocationStorage() {
        Intent serviceIntent = new Intent(context, LocationListenerService.class);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(serviceIntent);
        } else {
            getActivity().startService(serviceIntent);
        }
    }

    private void stopLocationStorage() {
        LocationListenerService.stopLocationUpdate();
    }

    private void storeOwnedKeyIdsPreferences(AssetsListResponseBean assetsListResponseBean) {
        String ownedKeys = null;
        if (assetsListResponseBean != null && assetsListResponseBean.getResult().size() > 0) {
            for (int i = 0; i < assetsListResponseBean.getResult().size(); i++) {
                if (!TextUtils.isEmpty(assetsListResponseBean.getResult().get(i).getAssetId())) {
                    if (ownedKeys != null) {
                        ownedKeys = ownedKeys + "," + assetsListResponseBean.getResult().get(i).getAssetId();
                    } else {
                        ownedKeys = assetsListResponseBean.getResult().get(i).getAssetId();
                    }
                }
            }
            AppSharedPrefs.getInstance(context).setOwnedKeyIds(ownedKeys);
        }
    }


    @Override
    public void CallOnActivityResult(Intent intent) {
        startActivityForResult(intent,AppUtils.REQ_REFRESH_VIEW);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtils.REQ_REFRESH_VIEW) {
            resultArrayList.clear();
            Utils.showProgressDialog(context, getString(R.string.loading));
            viewModel.getMyAssets(binding, AppSharedPrefs.getInstance(context).getEmployeeID(), "");
        }
    }

}
