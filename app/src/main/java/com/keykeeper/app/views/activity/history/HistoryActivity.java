package com.keykeeper.app.views.activity.history;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeeper.app.R;
import com.keykeeper.app.databinding.ActivityHistoryBinding;
import com.keykeeper.app.model.bean.HistoryResponseBean;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.adapter.HistoryAdapter;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity implements XRecyclerView.LoadingListener {

    HistoryViewModel viewModel;
    private Context context;
    private ActivityHistoryBinding binding;
    private HistoryAdapter historyAdapter;
    private ArrayList<HistoryResponseBean.ResponseResult> resultArrayList;

    Observer<HistoryResponseBean> response_observer = new Observer<HistoryResponseBean>() {

        @Override
        public void onChanged(@Nullable HistoryResponseBean historyResponseBean) {

            Utils.hideProgressDialog();
            if (historyResponseBean != null && historyResponseBean.getResultArray() != null
                    && historyResponseBean.getResultArray().size() > 0) {
                // resultArrayList = historyResponseBean.getResultArray();
                for (int i = 0; i < historyResponseBean.getResultArray().size(); i++) {
                    resultArrayList.add(historyResponseBean.getResultArray().get(i));
                }
                historyAdapter.setHistoryList(context, resultArrayList);

            } else {
                if (resultArrayList.size() > 0) {
                    Utils.showSnackBar(binding, getString(R.string.no_more_data));
                } else {
                    noDataView();
                }

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
        context = this;
        setCustomActionBar();
        initializeViews();
    }

    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.history), true, false, false, false, this);
    }

    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        binding.setViewModel(viewModel);

        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getHistoryList(0);

        resultArrayList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recyclerView.setLayoutManager(manager);
        historyAdapter = new HistoryAdapter(context, resultArrayList);
        binding.recyclerView.setAdapter(historyAdapter);
        binding.recyclerView.setLoadingListener(this);
        binding.recyclerView.setLoadingMoreEnabled(true);
        binding.recyclerView.setPullRefreshEnabled(true);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_iv:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (Connectivity.isConnected()) {
            resultArrayList.clear();
            viewModel.getHistoryList(0);
        } else {
            Utils.showSnackBar(binding, getString(R.string.internet_connection));
        }
        //clear all data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.refreshComplete();
            }
        }, 1000);

    }

    @Override
    public void onLoadMore() {
        Log.e("onLoadMore: ", "call load more");
        //pagination
        viewModel.getHistoryList(resultArrayList.get(resultArrayList.size() - 1).getAsset_transaction_log_id());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.loadMoreComplete();
            }
        }, 2000);

    }

    private void noDataView() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.noDataFountLayout.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }
}