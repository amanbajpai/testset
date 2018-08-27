package com.keykeep.app.views.activity.forgot_password;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.keykeep.app.R;
import com.keykeep.app.databinding.ActivityForgotPasswordBinding;
import com.keykeep.app.netcom.Keys;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.activity.login.LoginViewModel;
import com.keykeep.app.views.base.BaseActivity;

/**
 * Created by akshaydashore on 27/8/18
 */
public class ForgotPasswordActivity extends BaseActivity {

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
        viewModel = ViewModelProviders.of(this).get(ForgotViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
    }

    Observer observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case Keys.empty_id:
                    Utils.showAlert(context, getString(R.string.error), getString(R.string.enter_employeeid), "ok", "", Keys.dialogOkClick, viewModel);
                    break;

                case Keys.empty_password:
                    Utils.showAlert(context, getString(R.string.error), getString(R.string.enter_password), "ok", "", Keys.dialogOkClick, viewModel);
                    break;

            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (viewModel.checkEmail(binding.etMail.getText().toString())) {
                    finish();
                }
                break;
        }
    }

}
