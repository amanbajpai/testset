package com.lotview.app.views.activity.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.Gson;
import com.lotview.app.R;
import com.lotview.app.databinding.LoginActivityBinding;
import com.lotview.app.model.bean.LoginResponseBean;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.forgot_password.ForgotPasswordActivity;
import com.lotview.app.views.activity.home.HomeActivity;
import com.lotview.app.views.base.BaseActivity;

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
        viewModel.validator.observe(this, validatorObserver);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.validator.observe(this, validatorObserver);

        if (AppSharedPrefs.getInstance(context).isLogin() && AppSharedPrefs.getInstance(context).getRememberMe()) {
            LoginResponseBean.Result bean = Utils.getUserDetail();
            binding.etMail.setText(bean.getEmail());
            binding.etPassword.setText(AppSharedPrefs.getInstance(context).getPassword());
            binding.rememberCheckbox.setChecked(true);
        }

    }


    Observer validatorObserver = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.empty_id:
                    Utils.showSnackBar(binding, getString(R.string.enter_employeeid));
                    break;

                case AppUtils.empty_password:
                    Utils.showSnackBar(binding, getString(R.string.enter_password));
                    break;

                case AppUtils.invalid_mail:
                    Utils.showSnackBar(binding, getString(R.string.enter_valid_employeeid));
                    break;
                case AppUtils.NO_INTERNET:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.internet_connection));
                    break;

                case AppUtils.SERVER_ERROR:
                    Utils.showSnackBar(binding, getString(R.string.server_error));
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
            String empId = loginBean.getResult().getEmployeeId() + "";
            String empName = loginBean.getResult().getFirstname() + "";
            boolean isRemember = binding.rememberCheckbox.isChecked();

            AppSharedPrefs.getInstance(context).setUserDetail(user_detail);
            AppSharedPrefs.getInstance(context).setEmployeeID(empId);
            AppSharedPrefs.getInstance(context).setEmployeeName(empName);
            AppSharedPrefs.getInstance(context).setAccessToken(loginBean.getAccessToken());
            AppSharedPrefs.getInstance(context).setRememberMe(isRemember);
            AppSharedPrefs.getInstance(context).setLogin(true);
            AppSharedPrefs.getInstance(context).setPassword(binding.etPassword.getText().toString());

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                Utils.showProgressDialog(context, getString(R.string.loading));
                viewModel.doLogin(binding);
                break;

            case R.id.tv_forgot_password:
                startActivity(new Intent(context, ForgotPasswordActivity.class));
                break;
        }
    }


}
