package com.lotview.app.views.fragment.notifications;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.databinding.FragmentNotificationBinding;
import com.lotview.app.model.bean.NotificationsResponseBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
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

    public void getNotifications(FragmentNotificationBinding binding) {

        String employeeId = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getEmployeeID();

        Call<NotificationsResponseBean> call = RetrofitHolder.getService().getNotificationsRequest(KeyKeepApplication.getInstance().getBaseEntity(false), employeeId);

        call.enqueue(new Callback<NotificationsResponseBean>() {
            @Override
            public void onResponse(Call<NotificationsResponseBean> call, Response<NotificationsResponseBean> response) {
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NotificationsResponseBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }
}
