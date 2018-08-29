package com.keykeep.app.views.activity.assetDetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.keykeep.app.R;
import com.keykeep.app.databinding.ActivityAssetDetailLayoutBinding;
import com.keykeep.app.interfaces.DialogClickListener;
import com.keykeep.app.model.bean.AssetDetailBean;
import com.keykeep.app.preferences.Pref;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.activity.login.LoginViewModel;
import com.keykeep.app.views.base.BaseActivity;
import com.keykeep.app.views.fragment.home.HomeFragment;

/**
 * Created by ankurrawal on 23/8/18.
 */

public class AssetDetailActivity extends BaseActivity implements DialogClickListener {

    private Context context;
    ActivityAssetDetailLayoutBinding binding;
    AssetDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initializeViews();
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_asset_detail_layout);
        viewModel = ViewModelProviders.of(this).get(AssetDetailViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);

        String emp_id = Pref.getEmployeeID(context);
        String qrcode = getIntent().getStringExtra(AppUtils.SCANED_QR_CODE);
        viewModel.getAssetDetail(qrcode, emp_id);
    }


    Observer<AssetDetailBean> response_observer = new Observer<AssetDetailBean>() {
        @Override
        public void onChanged(@Nullable AssetDetailBean bean) {

            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                return;
            }

            AssetDetailBean.Result resultBean = bean.getResult();

            binding.assetName.setText(resultBean.getAssetName());
            binding.assetType.setText(resultBean.getAssetType()+"");
            binding.versionNumber.setText(resultBean.getVersionNumber());
            binding.codeNumber.setText(resultBean.getVin());
            binding.modelNo.setText(resultBean.getModelNumber());
            binding.colorName.setText(resultBean.getColor());

            binding.customerName.setText(resultBean.getCustomerName());
            binding.customerAddress.setText(resultBean.getCustomerAddress());
            binding.customerNumber.setText(resultBean.getCustomerMobileNumber());

            binding.employeeName.setText(Utils.validateValue(resultBean.getEmployeeName()));

        }
    };

    Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.NO_INTERNET:
                    Utils.showToast(context, getString(R.string.internet_connection));
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }

}
