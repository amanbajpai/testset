package com.lotview.app.views.activity.history;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.databinding.ActivityHistoryBinding;
import com.lotview.app.databinding.AllAssetListFragmentBinding;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.model.bean.HistoryResponseBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.views.base.BaseViewModel;

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
