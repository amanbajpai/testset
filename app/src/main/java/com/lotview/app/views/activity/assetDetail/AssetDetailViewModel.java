package com.lotview.app.views.activity.assetDetail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.AssetDetailBean;
import com.lotview.app.model.bean.BaseResponse;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 27/8/18
 */

public class AssetDetailViewModel extends ViewModel {


    MutableLiveData<Integer> validator = new MutableLiveData<>();
    MutableLiveData<AssetDetailBean> response_validator = new MutableLiveData<>();
    MutableLiveData<AssetDetailBean> asset_request_validator = new MutableLiveData<>();
    MutableLiveData<BaseResponse> asset_req_approved_validator = new MutableLiveData<>();
    MutableLiveData<BaseResponse> asset_req_cancel_validator = new MutableLiveData<>();


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

    /**
     * send request for handover
     *
     * @param qr_code
     * @param emp_id
     */
    public void sendHandoverRequest(String qr_code, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String lat = AppSharedPrefs.getLatitude();
        String lng = AppSharedPrefs.getLongitude();

        Call<AssetDetailBean> call = RetrofitHolder.getService().sendHandoverRequest(
                KeyKeepApplication.getBaseEntity(true),
                qr_code,
                "1", lat, lng
        );

        call.enqueue(new Callback<AssetDetailBean>() {
            @Override
            public void onResponse(Call<AssetDetailBean> call, Response<AssetDetailBean> response) {
                Utils.hideProgressDialog();
                AssetDetailBean bean = response.body();
                asset_request_validator.setValue(bean);
            }

            @Override
            public void onFailure(Call<AssetDetailBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }


    /**
     * send request for handover
     *
     * @param qr_code
     * @param emp_id
     */
    public void sendTransferRequest(String qr_code, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<AssetDetailBean> call = RetrofitHolder.getService().sendTransferRequest(
                KeyKeepApplication.getBaseEntity(true),
                qr_code
        );


        call.enqueue(new Callback<AssetDetailBean>() {
            @Override
            public void onResponse(Call<AssetDetailBean> call, Response<AssetDetailBean> response) {
                Utils.hideProgressDialog();
                AssetDetailBean bean = response.body();
                asset_request_validator.setValue(bean);
            }

            @Override
            public void onFailure(Call<AssetDetailBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }


    /**
     * call when emp id zero means asset not allotted to any one
     *
     * @param qr_code
     * @param emp_id
     */
    public void keepAssetRequest(String qr_code, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String lat = AppSharedPrefs.getLatitude();
        String lng = AppSharedPrefs.getLongitude();

        Call<AssetDetailBean> call = RetrofitHolder.getService().keepAssetRequest(
                KeyKeepApplication.getBaseEntity(true),
                qr_code, lat, lng
        );

        call.enqueue(new Callback<AssetDetailBean>() {
            @Override
            public void onResponse(Call<AssetDetailBean> call, Response<AssetDetailBean> response) {
                Utils.hideProgressDialog();
                AssetDetailBean bean = response.body();
                asset_request_validator.setValue(bean);
            }

            @Override
            public void onFailure(Call<AssetDetailBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }


    /**
     * approved api for requested asset to transfer
     *
     * @param req_id
     * @param emp_id
     */
    public void approveAssetRequest(int req_id, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String lat = AppSharedPrefs.getLatitude();
        String lng = AppSharedPrefs.getLongitude();

        Call<BaseResponse> call = RetrofitHolder.getService().approveAssetRequest(
                KeyKeepApplication.getBaseEntity(true),
                req_id, lat, lng);

        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                BaseResponse bean = response.body();
                asset_req_approved_validator.setValue(bean);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }

    public void cancelAssetRequest(int req_id, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String lat = AppSharedPrefs.getLatitude();
        String lng = AppSharedPrefs.getLongitude();
        Call<BaseResponse> call = RetrofitHolder.getService().cancelAssetRequest(
                KeyKeepApplication.getBaseEntity(true),
                req_id, lat, lng);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                BaseResponse bean = response.body();
                asset_req_cancel_validator.setValue(bean);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }

}
