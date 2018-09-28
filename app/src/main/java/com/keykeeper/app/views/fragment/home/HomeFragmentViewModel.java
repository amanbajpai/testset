package com.keykeeper.app.views.fragment.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.model.bean.EmployeeOwnedAssetsListResponse;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by akshaydashore on 29/8/18
 */

public class HomeFragmentViewModel extends ViewModel {


    public final MutableLiveData<Integer> validator = new MutableLiveData<>();

    public final MutableLiveData<EmployeeOwnedAssetsListResponse> response_allassets_owned = new MutableLiveData<>();

    public void getCurrentAssetsOwned() {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<EmployeeOwnedAssetsListResponse> call = RetrofitHolder.getService().getAssetOwnedByEmployee(KeyKeepApplication.getInstance().getBaseEntity(false));

        call.enqueue(new Callback<EmployeeOwnedAssetsListResponse>() {
            @Override
            public void onResponse(Call<EmployeeOwnedAssetsListResponse> call, Response<EmployeeOwnedAssetsListResponse> response) {
                response_allassets_owned.setValue(response.body());
            }

            @Override
            public void onFailure(Call<EmployeeOwnedAssetsListResponse> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }

}
