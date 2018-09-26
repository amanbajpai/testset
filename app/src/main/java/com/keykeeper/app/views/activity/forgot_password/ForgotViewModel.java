package com.keykeeper.app.views.activity.forgot_password;

import android.arch.lifecycle.MutableLiveData;

import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.databinding.ActivityForgotPasswordBinding;
import com.keykeeper.app.model.bean.ForgotPasswordResponseBean;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 27/8/18
 */
public class ForgotViewModel extends BaseViewModel {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<ForgotPasswordResponseBean> response_validator = new MutableLiveData<>();


    public boolean checkEmail(String text) {

        if (Utils.isStringsEmpty(text)) {
            validator.setValue(AppUtils.empty_id);
            return false;
        } else if (!Utils.isValideEmail(text)) {
            validator.setValue(AppUtils.invalid_mail);
            return false;
        }
        return true;
    }

    public void forgotPassword(ActivityForgotPasswordBinding binding) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            Utils.hideProgressDialog();
            return;
        }

        String email = binding.etMail.getText().toString();

        Call<ForgotPasswordResponseBean> call = RetrofitHolder.getService().forgotPassword(KeyKeepApplication.getInstance().getBaseEntity(false), email);

        call.enqueue(new Callback<ForgotPasswordResponseBean>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponseBean> call, Response<ForgotPasswordResponseBean> response) {
                Utils.hideProgressDialog();
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponseBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }

}
