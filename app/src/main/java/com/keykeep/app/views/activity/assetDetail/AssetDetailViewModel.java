package com.keykeep.app.views.activity.assetDetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.keykeep.app.R;
import com.keykeep.app.application.KeyKeepApplication;
import com.keykeep.app.databinding.ActivityAssetDetailLayoutBinding;
import com.keykeep.app.model.bean.AssetDetailBean;
import com.keykeep.app.model.bean.BaseRequestEntity;
import com.keykeep.app.model.bean.BaseResponse;
import com.keykeep.app.netcom.retrofit.RetrofitHolder;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Connectivity;
import com.keykeep.app.utils.Utils;

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
                emp_id,
                qr_code
        );

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
     * @param qr_code
     * @param emp_id
     */
    public void sendHandoverRequest(String qr_code, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<AssetDetailBean> call = RetrofitHolder.getService().sendHandoverRequest(
                KeyKeepApplication.getBaseEntity(true),
                emp_id,
                qr_code,
                "1"
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
                emp_id,
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
     * @param qr_code
     * @param emp_id
     */
    public void keepAssetRequest(String qr_code, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<AssetDetailBean> call = RetrofitHolder.getService().keepAssetRequest(
                KeyKeepApplication.getBaseEntity(true),
                emp_id,
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
     * approved api for requested asset to transfer
     * @param req_id
     * @param emp_id
     */
    public void approveAssetRequest(int req_id, String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<BaseResponse> call = RetrofitHolder.getService().approveAssetRequest(
                KeyKeepApplication.getBaseEntity(true),
                emp_id,
                req_id
        );

        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                BaseResponse bean  = response.body();
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

        Call<BaseResponse> call = RetrofitHolder.getService().cancelAssetRequest(
                KeyKeepApplication.getBaseEntity(true),
                emp_id,
                req_id
        );

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                BaseResponse bean  = response.body();
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
