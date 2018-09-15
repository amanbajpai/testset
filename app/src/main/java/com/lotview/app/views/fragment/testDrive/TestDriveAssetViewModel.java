package com.lotview.app.views.fragment.testDrive;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.AssetDetailBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 15/9/18
 */
public class TestDriveAssetViewModel extends BaseViewModel {


    MutableLiveData<Integer> validator = new MutableLiveData<>();
    MutableLiveData<AssetDetailBean> response_validator = new MutableLiveData<>();



    public void getAssetDetail(String qr_code, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<AssetDetailBean> call = RetrofitHolder.getService().getAssetDetail(
                KeyKeepApplication.getBaseEntity(true),
                emp_id, qr_code);

        call.enqueue(new Callback<AssetDetailBean>() {

            @Override
            public void onResponse(Call<AssetDetailBean> call, Response<AssetDetailBean> response) {
                Utils.hideProgressDialog();
                AssetDetailBean bean = response.body();
                response_validator.setValue(bean);
            }

            @Override
            public void onFailure(Call<AssetDetailBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }
}
