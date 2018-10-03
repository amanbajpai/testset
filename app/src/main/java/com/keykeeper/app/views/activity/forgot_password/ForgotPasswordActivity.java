package com.keykeeper.app.views.activity.forgot_password;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.keykeeper.app.R;
import com.keykeeper.app.databinding.ActivityForgotPasswordBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.ForgotPasswordResponseBean;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

/**
 * Created by akshaydashore on 27/8/18
 */
public class ForgotPasswordActivity extends BaseActivity implements DialogClickListener {

    ActivityForgotPasswordBinding binding;
    ForgotViewModel viewModel;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        context = this;
    }


    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        binding.tvSubmit.setOnClickListener(this);
        binding.backTv.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(ForgotViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
    }

    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.empty_id:
                    Utils.showSnackBar(binding, getString(R.string.enter_employeeid));
                    break;

                case AppUtils.invalid_mail:
                    Utils.showSnackBar(binding, getString(R.string.enter_valid_employeeid));
                    break;


                case AppUtils.NO_INTERNET:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.internet_connection));
                    break;
            }
        }
    };


    Observer<ForgotPasswordResponseBean> response_observer = new Observer<ForgotPasswordResponseBean>() {

        @Override
        public void onChanged(@Nullable ForgotPasswordResponseBean loginBean) {

            if (loginBean == null) {
                Utils.showAlert(context, getString(R.string.error), getString(R.string.enter_employeeid), "ok", "", AppUtils.dialog_ok_click, ForgotPasswordActivity.this);
            }
            if (loginBean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", loginBean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, ForgotPasswordActivity.this);
                return;
            }
            if (loginBean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                Utils.showAlert(context, "", loginBean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, ForgotPasswordActivity.this);
                return;
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (viewModel.checkEmail(binding.etMail.getText().toString())) {
                    Utils.showProgressDialog(context, getString(R.string.loading));
                    viewModel.forgotPassword(binding);
                }
                break;
            case R.id.back_tv:
                finish();
                break;
        }
    }


    @Override
    public void onDialogClick(int which, int requestCode) {

        switch (requestCode) {
            case AppUtils.dialog_ok_to_finish:
                finish();
                break;
        }
    }

}
