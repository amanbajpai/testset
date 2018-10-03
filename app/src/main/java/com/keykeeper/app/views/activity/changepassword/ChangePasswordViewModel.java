package com.keykeeper.app.views.activity.changepassword;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.text.TextUtils;

import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.databinding.ActivityChangePasswordBinding;
import com.keykeeper.app.model.bean.ChangePasswordBean;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 28/8/18
 */

public class ChangePasswordViewModel extends BaseViewModel {

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();
    public final MutableLiveData<ChangePasswordBean> response_validator = new MutableLiveData<>();

    public boolean isValidate(ActivityChangePasswordBinding binding) {

        if (TextUtils.isEmpty(binding.etOldPass.getText().toString())) {
            validator.setValue(AppUtils.empty_old_password);
            return false;
        } else if (TextUtils.isEmpty(binding.etPassword.getText().toString())) {
            validator.setValue(AppUtils.empty_password);
            return false;
        } else if (TextUtils.isEmpty(binding.etConfirmPass.getText().toString())) {
            validator.setValue(AppUtils.empty_confirm_password);
            return false;
        }else if (binding.etConfirmPass.getText().toString().equals(binding.etConfirmPass.getText().toString())) {
            validator.setValue(AppUtils.match_confirm_password);
            return false;
        }
        return true;
    }

    public void doChangePassword(ActivityChangePasswordBinding binding, Context context) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        String oldpassword = binding.etOldPass.getText().toString();
        String password = binding.etPassword.getText().toString();
        String c_password = binding.etConfirmPass.getText().toString();
        String emp_id = AppSharedPrefs.getEmployeeID();

        Call<ChangePasswordBean> call = RetrofitHolder.getService().doChangePassword(
                KeyKeepApplication.getInstance().getBaseEntity(true)
                , oldpassword
                , password
                , c_password);

        call.enqueue(new Callback<ChangePasswordBean>() {
            @Override
            public void onResponse(Call<ChangePasswordBean> call, Response<ChangePasswordBean> response) {
                Utils.hideProgressDialog();
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ChangePasswordBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }



}
