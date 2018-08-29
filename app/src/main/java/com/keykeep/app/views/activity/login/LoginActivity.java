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
import com.keykeep.app.model.bean.LoginResponseBean;
import com.keykeep.app.preferences.Pref;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Connectivity;
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
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        binding.tvLogin.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);

        if (Pref.getBoolean(context, Pref.IS_LOGIN) && Pref.getBoolean(context, Pref.IS_LOGIN)) {
            LoginResponseBean.Result bean = Utils.getUserDetail(context);
            binding.etMail.setText(bean.getEmail());
            binding.etPassword.setText(Pref.getPassword(context));
            binding.rememberCheckbox.setChecked(true);
        }

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
                case AppUtils.NO_INTERNET:
                    Utils.showToast(context, getString(R.string.internet_connection));
                    break;
            }
        }
    };

    Observer<LoginResponseBean> response_observer = new Observer<LoginResponseBean>() {

        @Override
        public void onChanged(@Nullable LoginResponseBean loginBean) {

            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            if (loginBean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", loginBean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }
            Gson gson = new Gson();

            String user_detail = gson.toJson(loginBean.getResult());
            Pref.setUserDetail(context, user_detail);
            String empId = loginBean.getResult().getEmployeeId() + "";
            Pref.setEmployeeID(context, empId);
            Pref.setAccessToken(context, loginBean.getAccessToken());
            boolean isRemember = binding.rememberCheckbox.isChecked();
            Pref.setBoolean(context, isRemember, Pref.REMEMBER_ME);
            Pref.setBoolean(context, true, Pref.IS_LOGIN);
            Pref.setPassword(context, binding.etPassword.getText().toString());

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                viewModel.doLogin(binding);
                break;

            case R.id.tv_forgot_password:
                startActivity(new Intent(context, ForgotPasswordActivity.class));
                break;
        }
    }


}
