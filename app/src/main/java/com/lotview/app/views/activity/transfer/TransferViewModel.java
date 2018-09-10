package com.lotview.app.views.activity.transfer;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.databinding.ActivityTransferAssetBinding;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 31/8/18
 */
public class TransferViewModel extends BaseViewModel {


    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<AssetsListResponseBean> response_validator = new MutableLiveData<>();

    public void getMyAssets(ActivityTransferAssetBinding binding) {

        String employeeId = AppSharedPrefs.getEmployeeID();

        Call<AssetsListResponseBean> call = RetrofitHolder.getService().getAssetsList(KeyKeepApplication.getInstance().getBaseEntity(false), employeeId, "1");

        call.enqueue(new Callback<AssetsListResponseBean>() {
            @Override
            public void onResponse(Call<AssetsListResponseBean> call, Response<AssetsListResponseBean> response) {
                Utils.hideProgressDialog();
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AssetsListResponseBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }

}
