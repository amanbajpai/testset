package com.keykeeper.app.views.activity.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.Gson;
import com.keykeeper.app.R;
import com.keykeeper.app.databinding.LoginActivityBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.LoginResponseBean;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.forgot_password.ForgotPasswordActivity;
import com.keykeeper.app.views.activity.home.HomeActivity;
import com.keykeeper.app.views.activity.testdrive.TestDriveStuckActivity;
import com.keykeeper.app.views.base.BaseActivity;

/**
 * Created by akshaydashore on 22/8/18
 */
public class LoginActivity extends BaseActivity implements DialogClickListener {

    LoginViewModel viewModel;
    private Context context;
    private LoginActivityBinding binding;

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

    Observer<LoginResponseBean> login_response_observer = new Observer<LoginResponseBean>() {

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
            String comId = loginBean.getResult().getCompanyId() + "";
            String empFirstName = loginBean.getResult().getFirstname() + "";
            String empLastName = loginBean.getResult().getLastname() + "";
            boolean isRemember = true;

            AppSharedPrefs.getInstance(context).setUserDetail(user_detail);
            AppSharedPrefs.getInstance(context).setEmployeeID(empId);
            AppSharedPrefs.getInstance(context).setCompanyID(comId);
            AppSharedPrefs.getInstance(context).setEmployeeName(empFirstName + " " + empLastName);
            AppSharedPrefs.getInstance(context).setAccessToken(loginBean.getAccessToken());
            AppSharedPrefs.getInstance(context).setRememberMe(isRemember);
            AppSharedPrefs.getInstance(context).setLogin(true);
            AppSharedPrefs.getInstance(context).setPassword(binding.etPassword.getText().toString());

            String url = loginBean.getResult().getChatUrl();
            url = url.replaceAll(" ", "%20");
            AppSharedPrefs.getInstance(context).setChatUrl(url);

            if (loginBean.getResult().getRunningTestDriveResponse() != null) {
                if (loginBean.getResult().getRunningTestDriveResponse().getTest_drive_start_status() != null) {

                    AppSharedPrefs.getInstance(context).setTestDriveID(loginBean.getResult().getRunningTestDriveResponse().getAsset_employee_test_drive_id());
                    AppSharedPrefs.setTestDriveAssetId(loginBean.getResult().getRunningTestDriveResponse().getAsset_id());
                    startActivity(new Intent(LoginActivity.this, TestDriveStuckActivity.class));
                    finish();

                } else {
                    AppSharedPrefs.getInstance(context).setTestDriveID("0");
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }


            } else {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getLocation(null);
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
        viewModel.response_validator.observe(this, login_response_observer);
        viewModel.validator.observe(this, validatorObserver);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getLocation(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocatonUpdates();
    }


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


    @Override
    public void onDialogClick(int which, int requestCode) {

        switch (requestCode) {
            case AppUtils.dialog_ok_mock_location:
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finishAffinity();
                }
                break;
        }
    }
}
