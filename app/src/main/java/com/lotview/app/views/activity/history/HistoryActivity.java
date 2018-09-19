package com.lotview.app.views.activity.history;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lotview.app.R;
import com.lotview.app.databinding.ActivityHistoryBinding;
import com.lotview.app.model.bean.HistoryResponseBean;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.adapter.HistoryAdapter;
import com.lotview.app.views.base.BaseActivity;

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
            if (historyResponseBean != null && historyResponseBean.getResultArray() != null && historyResponseBean.getResultArray().size() > 0) {
                resultArrayList = historyResponseBean.getResultArray();
                historyAdapter.setHistoryList(context, resultArrayList);

            } else {
                noDataView();
            }
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initializeViews();
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
        binding.recyclerView.setLoadingMoreEnabled(false);
        binding.recyclerView.setPullRefreshEnabled(true);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(this);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onRefresh() {
        viewModel.getHistoryList(0);
        //clear all data
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
        //pagination
        viewModel.getHistoryList(0);
    }

        private void noDataView() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.noDataFountLayout.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }
}