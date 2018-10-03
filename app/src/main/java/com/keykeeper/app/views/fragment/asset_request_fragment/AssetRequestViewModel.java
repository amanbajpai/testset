package com.keykeeper.app.views.fragment.asset_request_fragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.databinding.AssetRequestSendRecieveFragmentBinding;
import com.keykeeper.app.model.bean.AssetsListResponseBean;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ashishthakur on 29/8/18.
 */
public class AssetRequestViewModel extends ViewModel {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<AssetsListResponseBean> response_validator = new MutableLiveData<>();

    public void getAssetsPendingSendRequest(AssetRequestSendRecieveFragmentBinding binding) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }


        String employeeId = AppSharedPrefs.getEmployeeID();

        Call<AssetsListResponseBean> call = RetrofitHolder.getService().getAssetPendingSendRequest(KeyKeepApplication.getInstance().getBaseEntity(false));

        call.enqueue(new Callback<AssetsListResponseBean>() {
            @Override
            public void onResponse(Call<AssetsListResponseBean> call, Response<AssetsListResponseBean> response) {
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AssetsListResponseBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }

    public void getAssetsPendingRecieveRequest(AssetRequestSendRecieveFragmentBinding binding) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String employeeId = AppSharedPrefs.getEmployeeID();

        Call<AssetsListResponseBean> call = RetrofitHolder.getService().getAssetPendingRecieveRequest(KeyKeepApplication.getInstance().getBaseEntity(false));

        call.enqueue(new Callback<AssetsListResponseBean>() {

            @Override
            public void onResponse(Call<AssetsListResponseBean> call, Response<AssetsListResponseBean> response) {
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AssetsListResponseBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }


}
