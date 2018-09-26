package com.keykeeper.app.views.fragment.notifications;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeeper.app.R;
import com.keykeeper.app.databinding.FragmentNotificationBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.BaseResponse;
import com.keykeeper.app.model.bean.NotificationsResponseBean;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.home.HomeActivity;
import com.keykeeper.app.views.adapter.NotificationsListAdapter;
import com.keykeeper.app.views.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by ankurrawal on 6/9/18.
 */

public class NotificationFragment extends BaseFragment implements XRecyclerView.LoadingListener, DialogClickListener {

    private NotificationViewModel viewModel;
    private FragmentNotificationBinding binding;
    private Context context;
    private ArrayList<NotificationsResponseBean.Result> resultArrayList;
    private NotificationsListAdapter notificationsListAdapter;
    HomeActivity activity;
    boolean isLoadMore;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = ((HomeActivity) context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        binding.setViewModel(viewModel);

        initializeViews(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void initializeViews(View rootView) {

        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getNotifications(0);
        resultArrayList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.recycleNotification.setLayoutManager(manager);
        notificationsListAdapter = new NotificationsListAdapter(context, resultArrayList);
        binding.recycleNotification.setAdapter(notificationsListAdapter);
        binding.recycleNotification.setLoadingListener(this);
        binding.recycleNotification.setLoadingMoreEnabled(true);
        binding.recycleNotification.setPullRefreshEnabled(true);

        viewModel.validator.observe(this, validatorObserver);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.validator_clear_notification.observe(this, observer_clear_notification);

        Utils.hideSoftKeyboard(getActivity());
        activity.setRightButtonEnable("", true, null);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.icon_right:
                Utils.showAlert(context, "", getString(R.string.notification_alert), "ok", "cancel", AppUtils.dialog_ok_click, this);
                break;
        }
    }


    Observer validatorObserver = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.NO_INTERNET:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.internet_connection));
                    break;

                case AppUtils.SERVER_ERROR:
                    Utils.showSnackBar(binding, getString(R.string.server_error));
                    break;
            }
        }
    };


    Observer<NotificationsResponseBean> response_observer = new Observer<NotificationsResponseBean>() {

        @Override
        public void onChanged(@Nullable NotificationsResponseBean notificationsResponseBean) {

            if (notificationsResponseBean != null && notificationsResponseBean.getResult() != null && notificationsResponseBean.getResult().size() > 0) {
                /*resultArrayList = notificationsResponseBean.getResult();
                notificationsListAdapter = new NotificationsListAdapter(context, resultArrayList);
                binding.recycleNotification.setAdapter(notificationsListAdapter);*/

                for (int i = 0; i < notificationsResponseBean.getResult().size(); i++) {
                    resultArrayList.add(notificationsResponseBean.getResult().get(i));
                }
                notificationsListAdapter.setNotificationList(context, resultArrayList);
                activity.setRightButtonEnable("Clear All", true, NotificationFragment.this);

            } else {
                if (resultArrayList.size() > 0) {
                    Utils.showSnackBar(binding, getString(R.string.no_more_data));
                    activity.setRightButtonEnable("Clear All", true, NotificationFragment.this);
                } else {
                    noDataView();
                    activity.setRightButtonEnable("", false, null);

                }
            }
        }
    };


    Observer<BaseResponse> observer_clear_notification = new Observer<BaseResponse>() {

        @Override
        public void onChanged(@Nullable BaseResponse bean) {

            if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                resultArrayList.clear();
                notificationsListAdapter.notifyDataSetChanged();
                Utils.showAlert(context, "", bean.getMessage(), "ok", "", AppUtils.dialog_request_succes, NotificationFragment.this);
                noDataView();
                activity.setRightButtonEnable("", false, null);

            } else {
                Utils.showSnackBar(binding, bean.getMessage());
            }
        }
    };

    @Override
    public void onRefresh() {
        resultArrayList.clear();
        viewModel.getNotifications(0);
        //clear all data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recycleNotification.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        viewModel.getNotifications(Integer.valueOf(resultArrayList.get(resultArrayList.size() - 1).getNotificationId()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recycleNotification.loadMoreComplete();
            }
        }, 1000);
    }


    private void noDataView() {
        binding.recycleNotification.setVisibility(View.GONE);
        binding.noDataFountLayout.setVisibility(View.VISIBLE);
        binding.tvNoRecords.setText(getString(R.string.txt_no_records_avialable));
    }


    @Override
    public void onDialogClick(int which, int requestCode) {

        switch (requestCode) {

            case AppUtils.dialog_ok_click:
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Utils.showProgressDialog(context, getString(R.string.loading));
                        viewModel.clearNotification();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
                break;

        }
    }
}
