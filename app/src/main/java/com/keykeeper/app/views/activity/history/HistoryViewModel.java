package com.keykeeper.app.views.activity.history;

import android.arch.lifecycle.MutableLiveData;

import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.model.bean.HistoryResponseBean;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankurrawal on 29/8/18.
 */

public class HistoryViewModel extends BaseViewModel {
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<HistoryResponseBean> response_validator = new MutableLiveData<>();

    public void getHistoryList(int last_asset_transaction_log_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            Utils.hideProgressDialog();
            return;
        }
        Call<HistoryResponseBean> call = RetrofitHolder.getService().getHistoryList(KeyKeepApplication.getInstance().getBaseEntity(false), last_asset_transaction_log_id);
        call.enqueue(new Callback<HistoryResponseBean>() {
            @Override
            public void onResponse(Call<HistoryResponseBean> call, Response<HistoryResponseBean> response) {
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HistoryResponseBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }

}
