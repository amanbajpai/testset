package com.lotview.app.views.activity.keyMap;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.AssetDetailBean;
import com.lotview.app.model.bean.AssetLocationResponseBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nazimakauser on 19/9/18.
 */

public class KeyOnMapViewModel extends ViewModel{
    MutableLiveData<Integer> validator = new MutableLiveData<>();
    MutableLiveData<AssetLocationResponseBean> response_validator = new MutableLiveData<>();

    public void getLatLong(int asset_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<AssetLocationResponseBean> call = RetrofitHolder.getService().getAssetCurrentLocation(KeyKeepApplication.getBaseEntity(true), asset_id);

        call.enqueue(new Callback<AssetLocationResponseBean>() {

            @Override
            public void onResponse(Call<AssetLocationResponseBean> call, Response<AssetLocationResponseBean> response) {
                Utils.hideProgressDialog();
                AssetLocationResponseBean bean = response.body();
                response_validator.setValue(bean);
            }

            @Override
            public void onFailure(Call<AssetLocationResponseBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }
}
