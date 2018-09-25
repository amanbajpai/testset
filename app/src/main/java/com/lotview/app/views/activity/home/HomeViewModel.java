package com.lotview.app.views.activity.home;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.EmployeeOwnedAssetsListResponse;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 27/8/18
 */
public class HomeViewModel extends BaseViewModel {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();

    public final MutableLiveData<EmployeeOwnedAssetsListResponse> response_allassets_owned = new MutableLiveData<>();


    public void getCurrentAssetsOwned() {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String employeeId = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getEmployeeID();

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
