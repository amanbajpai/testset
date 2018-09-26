package com.keykeeper.app.views.fragment.setting;

import android.arch.lifecycle.MutableLiveData;

import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.model.bean.BaseResponse;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 14/9/18
 */

public class SettingViewModel extends BaseViewModel {


    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<BaseResponse> response_validator = new MutableLiveData<>();


    public void enableNotifications(String status) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            Utils.hideProgressDialog();
            return;
        }

        String employeeId = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getEmployeeID();

        Call<BaseResponse> call = RetrofitHolder.getService().enableNotifications(KeyKeepApplication.getInstance().getBaseEntity(false), status);

        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }

}
