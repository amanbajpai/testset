package com.keykeep.app.views.activity.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.Gson;
import com.keykeep.app.R;
import com.keykeep.app.databinding.LoginActivityBinding;
import com.keykeep.app.model.bean.LoginBean;
import com.keykeep.app.preferences.Pref;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.activity.forgot_password.ForgotPasswordActivity;
import com.keykeep.app.views.activity.home.HomeActivity;
import com.keykeep.app.views.base.BaseActivity;

/**
 * Created by akshaydashore on 22/8/18
 */
public class LoginActivity extends BaseActivity {


    private LoginActivityBinding binding;
    LoginViewModel viewModel;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initializeViews();
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        binding.tvLogin.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
    }


    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.empty_id:
                    Utils.showToast(context, getString(R.string.enter_employeeid));
                    break;

                case AppUtils.empty_password:
                    Utils.showToast(context, getString(R.string.enter_password));
                    break;

                case AppUtils.invalid_mail:
                    Utils.showToast(context, getString(R.string.enter_valid_employeeid));
                    break;

            }
        }
    };

    Observer<LoginBean> response_observer = new Observer<LoginBean>() {

        @Override
        public void onChanged(@Nullable LoginBean loginBean) {

            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), "ok", "", AppUtils.dialogOkClick, viewModel);
                return;
            }
            if (loginBean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", loginBean.getMessage(), "ok", "", AppUtils.dialogOkClick, viewModel);
                return;
            }
            Gson gson = new Gson();
            String user_detail = gson.toJson(loginBean.getResult());
            Pref.setUserDetail(context, user_detail);
            Pref.setAccessToken(context, loginBean.getAccessToken());
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (viewModel.checkEmail(binding.etMail.getText().toString())
                        && viewModel.checkPassword(binding.etPassword.getText().toString())) {
                    viewModel.doLogin(binding);
                }
                break;

            case R.id.tv_forgot_password:
                startActivity(new Intent(context, ForgotPasswordActivity.class));
                break;
        }
    }


}
