package com.keykeeper.app.views.activity.transfer;

import android.arch.lifecycle.MutableLiveData;

import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.databinding.ActivityTransferAssetBinding;
import com.keykeeper.app.model.bean.AssetsListResponseBean;
import com.keykeeper.app.model.bean.BaseResponse;
import com.keykeeper.app.model.bean.LoginResponseBean;
import com.keykeeper.app.model.bean.ReturnKeyBean;
import com.keykeeper.app.model.bean.ReturnKeyBeanList;
import com.keykeeper.app.netcom.Keys;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseViewModel;

import java.util.ArrayList;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 31/8/18
 */
public class TransferViewModel extends BaseViewModel {


    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<AssetsListResponseBean> response_validator = new MutableLiveData<>();
    public final MutableLiveData<BaseResponse> mutikeySubmit_response_validator = new MutableLiveData<>();

    public void getMyAssets(ActivityTransferAssetBinding binding) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String employeeId = AppSharedPrefs.getEmployeeID();

        Call<AssetsListResponseBean> call = RetrofitHolder.getService().getAssetsList(KeyKeepApplication.getInstance().getBaseEntity(false), "1", "");

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

    public void returnMultipleKey(AssetsListResponseBean assetsListResponseBean) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        /**
         * get box qr code from login response
         */
        LoginResponseBean.Result userDetail = Utils.getUserDetail();
        String box_number = userDetail.getQrCodeNumber();

        ArrayList<ReturnKeyBean> list = new ArrayList<>();

        for (int i = 0; i < assetsListResponseBean.getResult().size(); i++) {

            AssetsListResponseBean.Result bean = assetsListResponseBean.getResult().get(i);

            if (bean.isSelected) {

                ReturnKeyBean returnKeyBean = new ReturnKeyBean();
                returnKeyBean.setSubmitUserType("1");
                returnKeyBean.setQrCodeNumber(bean.getQrCodeNumber());
                returnKeyBean.setKeyboxQrCodeNumber(box_number);
                list.add(returnKeyBean);
            }

        }

        if (list.size() <= 0) {
            validator.setValue(AppUtils.NO_ITEM_SELECT);
            return;
        }

        ReturnKeyBeanList returnKeyBeanList = new ReturnKeyBeanList();
        returnKeyBeanList.setReturnKeyBeans(list);

        /**
         * adding common param
         */
        returnKeyBeanList.setApi_key(Keys.API_KEY);
        returnKeyBeanList.setDevice_id(Utils.getDeviceID());
        returnKeyBeanList.setDevice_type(Keys.TYPE_ANDROID);
        returnKeyBeanList.setEmployee_id(Utils.validateStringToInt(AppSharedPrefs.getEmployeeID()));
        returnKeyBeanList.setCompany_id(Utils.validateStringToInt(AppSharedPrefs.getCompanyID()));

        if (AppSharedPrefs.getPushDeviceToken() != null && AppSharedPrefs.getPushDeviceToken().trim().length() > 0) {
            returnKeyBeanList.setDevice_token(AppSharedPrefs.getPushDeviceToken());
        } else {
            returnKeyBeanList.setDevice_token("");
        }

        returnKeyBeanList.setToken_type(Keys.TOKEN_TYPE);
        returnKeyBeanList.setAccess_token(AppSharedPrefs.getAccessToken());


        Call<BaseResponse> call = RetrofitHolder.getService().returnMultipleKey(returnKeyBeanList);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utils.hideProgressDialog();
                mutikeySubmit_response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });


    }


}
