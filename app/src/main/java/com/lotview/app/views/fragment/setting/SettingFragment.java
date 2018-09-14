package com.lotview.app.views.fragment.setting;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.lotview.app.R;
import com.lotview.app.databinding.SettingFragmentBinding;
import com.lotview.app.interfaces.DialogClickListener;
import com.lotview.app.model.bean.BaseResponse;
import com.lotview.app.model.bean.NotificationsResponseBean;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.adapter.NotificationsListAdapter;
import com.lotview.app.views.base.BaseFragment;

/**
 * Created by akshaydashore on 14/9/18
 */
public class SettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener ,DialogClickListener {

    SettingFragmentBinding binding;
    private SettingViewModel viewModel;
    private Context context;


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
            binding.guideViewSwitch.setOnCheckedChangeListener(null);

            if (bean.getCode() == AppUtils.STATUS_SUCCESS) {
                Utils.showAlert(context,"",bean.getMessage(),"ok","cancel",AppUtils.dialog_ok_click,SettingFragment.this);
            }else {
                Utils.showAlert(context,"",bean.getMessage(),"ok","cancel",AppUtils.dialog_ok_click,SettingFragment.this);
            }

            binding.guideViewSwitch.setOnCheckedChangeListener(SettingFragment.this);
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
