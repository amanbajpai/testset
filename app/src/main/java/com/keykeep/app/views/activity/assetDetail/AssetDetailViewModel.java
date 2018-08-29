package com.keykeep.app.views.activity.assetDetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.keykeep.app.application.KeyKeepApplication;
import com.keykeep.app.model.bean.AssetDetailBean;
import com.keykeep.app.netcom.retrofit.RetrofitHolder;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Connectivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 27/8/18
 */

public class AssetDetailViewModel extends ViewModel {


    MutableLiveData<Integer> validator = new MutableLiveData<>();
    MutableLiveData<AssetDetailBean> response_validator = new MutableLiveData<>();


    public void getAssetDetail(String qr_code, String emp_id) {

        qr_code ="521055563";
        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<AssetDetailBean> call = RetrofitHolder.getService().getAssetDetail(
                KeyKeepApplication.getBaseEntity(true),
                emp_id,
                qr_code
        );

        call.enqueue(new Callback<AssetDetailBean>() {
            @Override
            public void onResponse(Call<AssetDetailBean> call, Response<AssetDetailBean> response) {
                AssetDetailBean bean = response.body();
                response_validator.setValue(bean);
            }

            @Override
            public void onFailure(Call<AssetDetailBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }
}
