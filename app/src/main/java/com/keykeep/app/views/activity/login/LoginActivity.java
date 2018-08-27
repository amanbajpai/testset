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
import com.keykeep.app.views.activity.HomeActivity;
import com.keykeep.app.views.base.BaseActivity;
import com.keykeep.app.views.custom_view.CustomActionBar;

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
        setContentView(R.layout.login_activity);
        setCustomActionBar();
        initializeViews();
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        binding.tvLogin.setOnClickListener(this);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

    }

    @Override
    public void setCustomActionBar() {

        super.setCustomActionBar();
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar("Login", false, false, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (viewModel.checkEmail(binding.etMail.getText().toString())) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
                break;
        }
    }


}
