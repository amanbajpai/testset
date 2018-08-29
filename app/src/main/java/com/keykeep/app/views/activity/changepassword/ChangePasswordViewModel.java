package com.keykeep.app.views.activity.changepassword;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.text.TextUtils;

import com.keykeep.app.application.KeyKeepApplication;
import com.keykeep.app.databinding.ActivityChangePasswordBinding;
import com.keykeep.app.interfaces.DialogClickListener;
import com.keykeep.app.model.bean.ChangePasswordBean;
import com.keykeep.app.netcom.retrofit.RetrofitHolder;
import com.keykeep.app.preferences.Pref;
import com.keykeep.app.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akshaydashore on 28/8/18
 */

public class ChangePasswordViewModel extends ViewModel implements DialogClickListener {

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
        }
        return true;
    }

    public void doChangePassword(ActivityChangePasswordBinding binding, Context context) {

        String oldpassword = binding.etOldPass.getText().toString();
        String password = binding.etPassword.getText().toString();
        String c_password = binding.etConfirmPass.getText().toString();
        String emp_id = Pref.getEmployeeID(context);

        Call<ChangePasswordBean> call = RetrofitHolder.getService().doChangePassword(
                KeyKeepApplication.getInstance().getBaseEntity(false)
                , oldpassword
                , password
                , c_password
                , emp_id);

        call.enqueue(new Callback<ChangePasswordBean>() {
            @Override
            public void onResponse(Call<ChangePasswordBean> call, Response<ChangePasswordBean> response) {
                response_validator.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ChangePasswordBean> call, Throwable t) {
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }


    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}
