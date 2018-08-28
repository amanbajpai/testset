package com.keykeep.app.views.activity.login;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.keykeep.app.R;
import com.keykeep.app.databinding.LoginActivityBinding;
import com.keykeep.app.interfaces.DialogClickListener;
import com.keykeep.app.model.bean.LoginBean;
import com.keykeep.app.netcom.Keys;
import com.keykeep.app.netcom.retrofit.RetrofitHolder;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.LogUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.base.BaseViewMadel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 24/8/18
 */
public class LoginViewModel extends BaseViewMadel implements DialogClickListener {


    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<LoginBean> responce_validator = new MutableLiveData<>();


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

    public boolean checkPassword(String text) {

        if (Utils.isStringsEmpty(text)) {
            validator.setValue(AppUtils.empty_password);
            return false;
        }
        return true;
    }

    @Override
    public void onDialogClick(int which, int requestCode) {
    }

    public void doLogin(LoginActivityBinding binding) {

        String email = binding.etMail.getText().toString();
        String password = binding.etPassword.getText().toString();

        Call<LoginBean> call = RetrofitHolder.getService().doLogin(email, password
                , Utils.getApiKey()
                , Utils.getDeviceID()
                , Utils.getToken()
                , Utils.getDeviceType());


        call.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                responce_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }

}
