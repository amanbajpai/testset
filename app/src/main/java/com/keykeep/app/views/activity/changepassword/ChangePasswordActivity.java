package com.keykeep.app.views.activity.changepassword;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.keykeep.app.R;
import com.keykeep.app.databinding.ActivityChangePasswordBinding;
import com.keykeep.app.model.bean.ChangePasswordBean;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.base.BaseActivity;

/**
 * Created by akshaydashore on 28/8/18
 */

public class ChangePasswordActivity extends BaseActivity {


    private ActivityChangePasswordBinding binding;
    private ChangePasswordViewModel viewModel;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    public void initializeViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        binding.tvSubmit.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);

        viewModel.response_validator.observe(this, response_observer);

    }

    /**
     * handle web service response
     */
    Observer<ChangePasswordBean> response_observer = new Observer<ChangePasswordBean>() {
        @Override
        public void onChanged(@Nullable ChangePasswordBean bean) {

            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), "ok", "", AppUtils.dialogOkClick, viewModel);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                Utils.showAlert(context, "", bean.getMessage(), "ok", "", AppUtils.dialogOkClick, viewModel);
                finish();
            } else if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", bean.getMessage(), "ok", "", AppUtils.dialogOkClick, viewModel);
                return;
            }

        }
    };


    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {

            switch (value) {

                case AppUtils.empty_old_password:
                    Utils.showToast(context, getString(R.string.enter_old_password));
                    break;

                case AppUtils.empty_password:
                    Utils.showToast(context, getString(R.string.enter_password));
                    break;

                case AppUtils.empty_confirm_password:
                    Utils.showToast(context, getString(R.string.enter_confirm_password));
                    break;

                case AppUtils.match_confirm_password:
                    Utils.showToast(context, getString(R.string.confirm_password_match));
                    break;
                case AppUtils.SERVER_ERROR:
                    Utils.showToast(context, getString(R.string.server_error));
                    break;

            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_submit:
                if (viewModel.isValidate(binding)) {
                    viewModel.doChangePassword(binding, context);
                }
                break;
        }
    }
}
