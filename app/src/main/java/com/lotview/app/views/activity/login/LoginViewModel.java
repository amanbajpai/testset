package com.lotview.app.views.activity.login;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.databinding.LoginActivityBinding;
import com.lotview.app.interfaces.DialogClickListener;
import com.lotview.app.model.bean.LoginResponseBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 24/8/18
 */
public class LoginViewModel extends BaseViewModel implements DialogClickListener {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<LoginResponseBean> response_validator = new MutableLiveData<>();

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

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        if (!checkEmail(binding.etMail.getText().toString())) {
            return;
        }

        if (!checkPassword(binding.etPassword.getText().toString())) {
            return;
        }



        String email = binding.etMail.getText().toString();
        String password = binding.etPassword.getText().toString();

        Call<LoginResponseBean> call = RetrofitHolder.getService().doLogin(KeyKeepApplication.getInstance().getBaseEntity(false), email, password);

        call.enqueue(new Callback<LoginResponseBean>() {
            @Override
            public void onResponse(Call<LoginResponseBean> call, Response<LoginResponseBean> response) {
                Utils.hideProgressDialog();
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<LoginResponseBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });
    }

}
