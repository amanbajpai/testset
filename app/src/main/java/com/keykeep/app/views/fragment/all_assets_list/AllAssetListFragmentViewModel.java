package com.keykeep.app.views.fragment.all_assets_list;

import android.arch.lifecycle.MutableLiveData;

import com.keykeep.app.application.KeyKeepApplication;
import com.keykeep.app.databinding.AllAssetListFragmentBinding;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.netcom.retrofit.RetrofitHolder;
import com.keykeep.app.preferences.Pref;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankurrawal on 29/8/18.
 */

public class AllAssetListFragmentViewModel extends BaseViewModel {
    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<AssetsListResponseBean> response_validator = new MutableLiveData<>();

    public void getAllAssets(AllAssetListFragmentBinding binding) {

        String employeeId = Pref.getEmployeeID(KeyKeepApplication.getInstance());

        Call<AssetsListResponseBean> call = RetrofitHolder.getService().getAssetsList(KeyKeepApplication.getInstance().getBaseEntity(false), employeeId, "0");

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
