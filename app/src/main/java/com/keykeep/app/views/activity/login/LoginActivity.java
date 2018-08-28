package com.keykeep.app.views.activity.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.keykeep.app.R;
import com.keykeep.app.databinding.LoginActivityBinding;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.model.bean.LoginBean;
import com.keykeep.app.views.activity.forgot_password.ForgotPasswordActivity;
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
        viewModel.responce_validator.observe(this, responce_observer);
    }


    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.empty_id:
                    Utils.showAlert(context, getString(R.string.error), getString(R.string.enter_employeeid), "ok", "", AppUtils.dialogOkClick, viewModel);
                    break;

                case AppUtils.empty_password:
                    Utils.showAlert(context, getString(R.string.error), getString(R.string.enter_password), "ok", "", AppUtils.dialogOkClick, viewModel);
                    break;

                case AppUtils.invalid_mail:
                    Utils.showAlert(context, getString(R.string.error), getString(R.string.enter_valid_employeeid), "ok", "", AppUtils.dialogOkClick, viewModel);
                    break;

            }
        }
    };

    Observer<LoginBean> responce_observer = new Observer<LoginBean>() {

        @Override
        public void onChanged(@Nullable LoginBean loginBean) {

            if (loginBean == null){
                Utils.showAlert(context, getString(R.string.error), getString(R.string.enter_employeeid), "ok", "", AppUtils.dialogOkClick, viewModel);
            }

        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (viewModel.checkEmail(binding.etMail.getText().toString())
                        && viewModel.checkPassword(binding.etPassword.getText().toString())) {
                    viewModel.doLogin(binding);
//                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                    finish();
                }
                break;

            case R.id.tv_forgot_password:
                startActivity(new Intent(context, ForgotPasswordActivity.class));
                break;
        }
    }


}
