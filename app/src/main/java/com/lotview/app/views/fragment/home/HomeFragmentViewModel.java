package com.lotview.app.views.fragment.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.EmployeeOwnedAssetsListResponse;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;

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

        String employeeId = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getEmployeeID();

        Call<EmployeeOwnedAssetsListResponse> call = RetrofitHolder.getService().getAssetOwnedByEmployee(KeyKeepApplication.getInstance().getBaseEntity(false), employeeId);

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
