package com.lotview.app.views.fragment.notifications;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lotview.app.R;
import com.lotview.app.databinding.FragmentNotificationBinding;
import com.lotview.app.model.bean.NotificationsResponseBean;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.adapter.NotificationsListAdapter;
import com.lotview.app.views.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by ankurrawal on 6/9/18.
 */

public class NotificationFragment extends BaseFragment implements XRecyclerView.LoadingListener {

    private NotificationViewModel viewModel;
    private FragmentNotificationBinding binding;
    private Context context;
    private ArrayList<NotificationsResponseBean.Result> resultArrayList;
    private NotificationsListAdapter notificationsListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);

        initializeViews(binding.getRoot());
        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getNotifications(binding);

        return binding.getRoot();
    }

    @Override
    public void initializeViews(View rootView) {
        resultArrayList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recycleNotification.setLayoutManager(manager);
        notificationsListAdapter = new NotificationsListAdapter(context, resultArrayList);
        binding.recycleNotification.setAdapter(notificationsListAdapter);
        binding.recycleNotification.setLoadingListener(this);
        binding.recycleNotification.setLoadingMoreEnabled(false);
        binding.recycleNotification.setPullRefreshEnabled(true);
        viewModel.response_validator.observe(this, response_observer);
        Utils.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onClick(View v) {

    }

    Observer<NotificationsResponseBean> response_observer = new Observer<NotificationsResponseBean>() {

        @Override
        public void onChanged(@Nullable NotificationsResponseBean notificationsResponseBean) {

            Utils.hideProgressDialog();
            if (notificationsResponseBean != null && notificationsResponseBean.getResult() != null && notificationsResponseBean.getResult().size() > 0) {
                resultArrayList = notificationsResponseBean.getResult();
                notificationsListAdapter.setNotificationList(getActivity(), resultArrayList);

            } else {
                noDataView();
            }
        }
    };

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


    private void noDataView() {
        binding.recycleNotification.setVisibility(View.GONE);
        binding.tvNoRecords.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }


}
