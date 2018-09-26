package com.keykeeper.app.views.fragment.setting;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.keykeeper.app.R;
import com.keykeeper.app.databinding.SettingFragmentBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.BaseResponse;
import com.keykeeper.app.model.bean.LoginResponseBean;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseFragment;

/**
 * Created by akshaydashore on 14/9/18
 */
public class SettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, DialogClickListener {

    SettingFragmentBinding binding;
    private SettingViewModel viewModel;
    private Context context;
    private LoginResponseBean.Result userBean;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.setting_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, validatorObserver);
        viewModel.response_validator.observe(this, response_observer);
        userBean = Utils.getUserDetail();
        if (userBean != null && Utils.validateInt(userBean.getEnableNotification()) == 1) {
            binding.notificationSwitch.setChecked(true);
        }
        binding.notificationSwitch.setOnCheckedChangeListener(SettingFragment.this);
        return binding.getRoot();
    }


    Observer validatorObserver = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

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


    Observer<BaseResponse> response_observer = new Observer<BaseResponse>() {

        @Override
        public void onChanged(@Nullable BaseResponse bean) {

            binding.notificationSwitch.setOnCheckedChangeListener(null);
            if (bean.getCode() == AppUtils.STATUS_SUCCESS) {
                Utils.showAlert(context, "", bean.getMessage(), "ok", "", AppUtils.dialog_ok_click, SettingFragment.this);
            } else {
                Utils.showAlert(context, "", bean.getMessage(), "ok", "", AppUtils.dialog_ok_click, SettingFragment.this);
            }
            if (binding.notificationSwitch.isChecked()) {
                userBean.setEnableNotification(1);
            } else {
                userBean.setEnableNotification(0);
            }
            Utils.setUserDetail(userBean);

            binding.notificationSwitch.setOnCheckedChangeListener(SettingFragment.this);

        }
    };

    @Override
    public void initializeViews(View rootView) {

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            Utils.showProgressDialog(context, getString(R.string.loading));
            viewModel.enableNotifications("1");
        } else {

            Utils.showProgressDialog(context, getString(R.string.loading));
            viewModel.enableNotifications("2");

        }
    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}
