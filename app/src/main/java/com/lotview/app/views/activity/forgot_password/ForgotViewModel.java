package com.lotview.app.views.activity.forgot_password;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.databinding.ActivityForgotPasswordBinding;
import com.lotview.app.interfaces.DialogClickListener;
import com.lotview.app.model.bean.ForgotPasswordResponseBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 27/8/18
 */
public class ForgotViewModel extends BaseViewModel implements DialogClickListener {

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
            return;
        }

        String email = binding.etMail.getText().toString();

        Call<ForgotPasswordResponseBean> call = RetrofitHolder.getService().forgotPassword(KeyKeepApplication.getInstance().getBaseEntity(false), email);

        call.enqueue(new Callback<ForgotPasswordResponseBean>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponseBean> call, Response<ForgotPasswordResponseBean> response) {
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponseBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }


    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}
