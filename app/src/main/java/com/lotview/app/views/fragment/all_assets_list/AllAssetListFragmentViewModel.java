package com.lotview.app.views.fragment.all_assets_list;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.databinding.AllAssetListFragmentBinding;
import com.lotview.app.model.bean.AssetsListResponseBean;
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

public class AllAssetListFragmentViewModel extends BaseViewModel {
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<AssetsListResponseBean> response_validator = new MutableLiveData<>();

    public void getAllAssets(AllAssetListFragmentBinding binding, String text_to_search) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String employeeId = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getEmployeeID();

        Call<AssetsListResponseBean> call = RetrofitHolder.getService().getAssetsList(KeyKeepApplication.getInstance().getBaseEntity(false), "0", text_to_search);

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
