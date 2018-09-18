package com.lotview.app.views.fragment.testDrive;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.AssetDetailBean;
import com.lotview.app.model.bean.BaseResponse;
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
    MutableLiveData<BaseResponse> response_testdrive_start = new MutableLiveData<>();
    MutableLiveData<BaseResponse> response_testdrive_stop = new MutableLiveData<>();


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


    public void doStartTestDrive(String emp_id, int asset_id, String start_latitude, String start_logitude,
                                 String start_date_time, String start_date_time_utc) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<BaseResponse> call = RetrofitHolder.getService().doStartTestDrive(
                KeyKeepApplication.getBaseEntity(true),
                emp_id, asset_id, start_latitude, start_logitude, start_date_time, start_date_time_utc);

        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                BaseResponse bean = response.body();
                response_testdrive_start.setValue(bean);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }

    public void doStopTestDrive(String emp_id, int asset_id, String start_latitude, String start_logitude,
                                String start_date_time, String start_date_time_utc) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<BaseResponse> call = RetrofitHolder.getService().doStopTestDrive(
                KeyKeepApplication.getBaseEntity(true),
                emp_id, asset_id, start_latitude, start_logitude, start_date_time, start_date_time_utc);

        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                BaseResponse bean = response.body();
                response_testdrive_stop.setValue(bean);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }


}
