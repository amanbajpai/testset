package com.keykeep.app.views.activity.assetDetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.keykeep.app.R;
import com.keykeep.app.databinding.ActivityAssetDetailLayoutBinding;
import com.keykeep.app.interfaces.DialogClickListener;
import com.keykeep.app.model.bean.AssetDetailBean;
import com.keykeep.app.preferences.Pref;
import com.keykeep.app.qrcodescanner.QrCodeActivity;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.base.BaseActivity;
import com.keykeep.app.views.custom_view.CustomActionBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurrawal on 23/8/18.
 */

public class AssetDetailActivity extends BaseActivity implements DialogClickListener {

    public static final int STATUS_ADMIN = 1;
    public static final int STATUS_ = 2;
    private Context context;
    ActivityAssetDetailLayoutBinding binding;
    AssetDetailViewModel viewModel;
    private String emp_id;
    private AssetDetailBean.Result resultBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setCustomActionBar();
        initializeViews();
    }


    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar("Assets Detail", true, false, this);
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_asset_detail_layout);
        viewModel = ViewModelProviders.of(this).get(AssetDetailViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.asset_request_validator.observe(this, asset_request_observer);
        binding.scanButton.setOnClickListener(AssetDetailActivity.this);

        /**
         * call web service to get data using qr code
         */
        emp_id = Pref.getEmployeeID(context);
        String qrcode = getIntent().getStringExtra(AppUtils.SCANED_QR_CODE);
        Utils.showDialog(context, getString(R.string.please_wait));
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
            setDataFromBean(bean);

        }
    };


    Observer<AssetDetailBean> asset_request_observer = new Observer<AssetDetailBean>() {
        @Override
        public void onChanged(@Nullable AssetDetailBean bean) {

            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                return;
            }

            Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);

            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                return;
            }
            binding.scanButton.setVisibility(View.GONE);
            setDataFromBean(bean);

        }

    };

    private void setDataFromBean(AssetDetailBean bean) {
        resultBean = bean.getResult();

        binding.assetName.setText(resultBean.getAssetName());
        binding.assetType.setText(Utils.getAssetType(resultBean.getAssetType()));
        binding.versionNumber.setText(resultBean.getVersionNumber());
        binding.codeNumber.setText(resultBean.getVin());
        binding.modelNo.setText(resultBean.getModelNumber());
        binding.colorName.setText(resultBean.getColor());

        binding.customerName.setText(resultBean.getCustomerName());
        binding.customerAddress.setText(resultBean.getCustomerAddress());
        binding.customerNumber.setText(resultBean.getCustomerMobileNumber());

        binding.employeeName.setText(Utils.validateValue(resultBean.getEmployeeName()));
        binding.scanButton.setOnClickListener(AssetDetailActivity.this);
    }


    Observer<Integer> observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.NO_INTERNET:
                    Utils.showToast(context, getString(R.string.internet_connection));
                    break;

                case AppUtils.SERVER_ERROR:
                    Utils.showToast(context, getString(R.string.server_error));
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.scan_button:
                callSubmit();
//                Utils.showDialog(context,getString(R.string.please_wait));
//                viewModel.sendAssetRequest(resultBean.getQrCodeNumber(), emp_id);
                break;

            case R.id.left_iv:
                finish();
                break;
        }
    }

    private void callSubmit() {

        switch (1) {

            case AppUtils.STATUS_SCAN_CODE:
                if (Utils.checkPermissions(this, AppUtils.STORAGE_CAMERA_PERMISSIONS)) {
                    startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA);
                    }
                }
                break;

            case AppUtils.STATUS_ASSET_REQUEST:
                break;
        }


    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        /**
         * Get asset result from web service after scan Qr code
         */
        if (requestCode == AppUtils.REQUEST_CODE_QR_SCAN) {

            if (resultCode != RESULT_OK) {
                Utils.showToast(context, getString(R.string.unable_to_scan_qr));
                return;
            }
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
            try {
                JSONObject jsonObject = new JSONObject(result);
                result = jsonObject.getString("qr_code_number");
                Intent intent = new Intent(context, AssetDetailActivity.class);
                intent.putExtra(AppUtils.SCANED_QR_CODE, result);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
                Utils.showToast(context, getString(R.string.unable_to_scan_qr));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtils.REQUEST_CODE_CAMERA || Utils.onRequestPermissionsResult(permissions, grantResults)) {
            startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
        } else {
            Utils.showToast(context, getString(R.string.allow_camera_permission));
        }
    }


}
