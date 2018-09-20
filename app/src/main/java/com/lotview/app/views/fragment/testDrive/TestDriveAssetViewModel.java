package com.lotview.app.views.fragment.testDrive;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.AssetDetailBean;
import com.lotview.app.model.bean.TestDriveResponseBean;
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
    MutableLiveData<TestDriveResponseBean> response_testdrive_start = new MutableLiveData<>();
    MutableLiveData<TestDriveResponseBean> response_testdrive_stop = new MutableLiveData<>();


    public void getAssetDetail(String qr_code, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<AssetDetailBean> call = RetrofitHolder.getService().getAssetDetail(
                KeyKeepApplication.getBaseEntity(true),
                qr_code);

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


    public void doStartTestDrive(String emp_id, int asset_id, String start_latitude, String start_logitude,
                                 String start_date_time, String start_date_time_utc, String qr_code_no) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<TestDriveResponseBean> call = RetrofitHolder.getService().doStartTestDrive(
                KeyKeepApplication.getBaseEntity(true),
                 asset_id, start_latitude, start_logitude, start_date_time, start_date_time_utc, qr_code_no);

        call.enqueue(new Callback<TestDriveResponseBean>() {

            @Override
            public void onResponse(Call<TestDriveResponseBean> call, Response<TestDriveResponseBean> response) {
                Utils.hideProgressDialog();
                TestDriveResponseBean bean = response.body();
                response_testdrive_start.setValue(bean);
            }

            @Override
            public void onFailure(Call<TestDriveResponseBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }


}
