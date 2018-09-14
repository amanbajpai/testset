package com.lotview.app.views.fragment.notifications;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.databinding.FragmentNotificationBinding;
import com.lotview.app.model.bean.BaseResponse;
import com.lotview.app.model.bean.LoginResponseBean;
import com.lotview.app.model.bean.NotificationsResponseBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankurrawal on 6/9/18.
 */

public class NotificationViewModel extends BaseViewModel {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<NotificationsResponseBean> response_validator = new MutableLiveData<>();
    public final MutableLiveData<BaseResponse> validator_clear_notification = new MutableLiveData<>();


    public void getNotifications(FragmentNotificationBinding binding) {


        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            Utils.hideProgressDialog();
            return;
        }

        String employeeId = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getEmployeeID();

        Call<NotificationsResponseBean> call = RetrofitHolder.getService().getNotificationsRequest(KeyKeepApplication.getInstance().getBaseEntity(false), employeeId);

        call.enqueue(new Callback<NotificationsResponseBean>() {
            @Override
            public void onResponse(Call<NotificationsResponseBean> call, Response<NotificationsResponseBean> response) {
                Utils.hideProgressDialog();
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NotificationsResponseBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }


    public void clearNotification() {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            Utils.hideProgressDialog();
            return;
        }

        String employeeId = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getEmployeeID();

        Call<BaseResponse> call = RetrofitHolder.getService().clearAllNotification(KeyKeepApplication.getInstance().getBaseEntity(false), employeeId);

        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                validator_clear_notification.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }

}
