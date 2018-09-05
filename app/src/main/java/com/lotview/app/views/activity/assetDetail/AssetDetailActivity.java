package com.lotview.app.views.activity.assetDetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.lotview.app.R;
import com.lotview.app.databinding.ActivityAssetDetailLayoutBinding;
import com.lotview.app.interfaces.DialogClickListener;
import com.lotview.app.model.bean.AssetDetailBean;
import com.lotview.app.model.bean.BaseResponse;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.qrcodescanner.QrCodeActivity;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.base.BaseActivity;
import com.lotview.app.views.custom_view.CustomActionBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ankurrawal on 23/8/18.
 */

public class AssetDetailActivity extends BaseActivity implements DialogClickListener {

    public static int ASSET_STATUS = 1;
    private Context context;
    ActivityAssetDetailLayoutBinding binding;
    AssetDetailViewModel viewModel;
    private String mEmp_id;
    private String asset_emp_id;
    private String qr_code;
    private int req_id = -1;
    boolean IS_FROM_SCANNER = false;
    private String assetRequestedByName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setCustomActionBar();
        initializeViews();
    }


    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.asset_detail), true, false, this);
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_asset_detail_layout);
        viewModel = ViewModelProviders.of(this).get(AssetDetailViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.asset_request_validator.observe(this, asset_request_observer);
        viewModel.asset_req_approved_validator.observe(this, asset_req_approved_observer);
        viewModel.asset_req_cancel_validator.observe(this, asset_req_cancel_observer);

        binding.scanButton.setOnClickListener(AssetDetailActivity.this);

        /**
         * call web service to get data using qr code
         */
        mEmp_id = AppSharedPrefs.getEmployeeID();

        qr_code = getIntent().getStringExtra(AppUtils.SCANED_QR_CODE);
        Utils.showProgressDialog(context, getString(R.string.loading));
        ASSET_STATUS = getIntent().getIntExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
        assetRequestedByName = getIntent().getStringExtra(AppUtils.ASSET_REQUESTED_BY_EMP_NAME);
        viewModel.getAssetDetail(qr_code, mEmp_id);
        if (ASSET_STATUS == AppUtils.STATUS_SCANED_CODE) {
            IS_FROM_SCANNER = true;
        }


    }


    private void validateAfterScan(String qr) {

        switch (ASSET_STATUS) {

            case AppUtils.STATUS_ALL_ASSET_LIST:
                binding.scanButton.setVisibility(View.VISIBLE);
                binding.scanButton.setText(R.string.confirm_request);
                break;

            case AppUtils.STATUS_TRANSFER_ASSET_LIST:
                binding.scanButton.setVisibility(View.VISIBLE);
                binding.scanButton.setText(R.string.confirm_transfer);
                break;

            default:
                this.qr_code = qr;

        }

    }

    private void validateSubmitView() {

        switch (ASSET_STATUS) {

            case AppUtils.STATUS_ALL_ASSET_LIST:
                binding.scanButton.setVisibility(View.VISIBLE);
                binding.scanButton.setText(R.string.request_key);
                break;


            case AppUtils.STATUS_SCANED_CODE:
                binding.scanButton.setVisibility(View.VISIBLE);
                binding.scanButton.setText(R.string.confirm_request);
                break;


            case AppUtils.STATUS_TRANSFER_ASSET_LIST:
                binding.scanButton.setVisibility(View.VISIBLE);
                binding.scanButton.setText(R.string.transfer_key_to_company);
                break;


            case AppUtils.STATUS_ASSET_SEND_REQUEST1:
                req_id = getIntent().getIntExtra(AppUtils.ASSET_REQUEST_ID, -1);
                binding.pendingStatusContainer.setVisibility(View.VISIBLE);
                binding.cancelTv.setOnClickListener(this);
                binding.acceptTv.setOnClickListener(this);
                break;

            case AppUtils.STATUS_ASSET_SEND_REQUEST:
                req_id = getIntent().getIntExtra(AppUtils.ASSET_REQUEST_ID, -1);
                binding.pendingStatusContainer.setVisibility(View.VISIBLE);
                binding.acceptTv.setVisibility(View.GONE);
                binding.cancelTv.setOnClickListener(this);
                break;
        }
    }


    /**
     * Asset detail observer
     */
    Observer<AssetDetailBean> response_observer = new Observer<AssetDetailBean>() {
        @Override
        public void onChanged(@Nullable AssetDetailBean bean) {

            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                if (IS_FROM_SCANNER) {
                    IS_FROM_SCANNER = false;
                    Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, AssetDetailActivity.this);
                }

                return;
            }
            setDataFromBean(bean);
            validateSubmitView();
        }
    };


    Observer<AssetDetailBean> asset_request_observer = new Observer<AssetDetailBean>() {
        @Override
        public void onChanged(@Nullable AssetDetailBean bean) {

            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, AssetDetailActivity.this);
                return;
            }
            Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, AssetDetailActivity.this);
            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                return;
            }
        }

    };


    Observer<BaseResponse> asset_req_approved_observer = new Observer<BaseResponse>() {

        @Override
        public void onChanged(@Nullable BaseResponse bean) {

            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                return;
            } else {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, AssetDetailActivity.this);
            }

        }
    };


    Observer<BaseResponse> asset_req_cancel_observer = new Observer<BaseResponse>() {
        @Override
        public void onChanged(@Nullable BaseResponse bean) {

            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, AssetDetailActivity.this);
                return;
            } else {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, AssetDetailActivity.this);
            }
        }
    };


    Observer<Integer> observer = new Observer<Integer>() {

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

    /**
     * Set data on view components from web service bean
     *
     * @param bean
     */
    private void setDataFromBean(AssetDetailBean bean) {
        AssetDetailBean.Result resultBean = bean.getResult();

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


        if (resultBean.getAssetAssginedStatus() == 0) {
            binding.availability.setText("Available");
        } else if (resultBean.getAssetAssginedStatus() == 1) {
            binding.availability.setText("Assigned");
        }
        binding.make.setText(resultBean.getDescription());
        binding.scanButton.setOnClickListener(AssetDetailActivity.this);
        asset_emp_id = Utils.validateInt(resultBean.getEmployeeId()) + "";
        qr_code = resultBean.getQrCodeNumber();

        if (Utils.validateStringValue(resultBean.getEmployeeName()).equals("")) {
            binding.employeeContainer.setVisibility(View.GONE);
        }

        if (!Utils.validateStringValue(assetRequestedByName).equals("")) {
            binding.requestContainer.setVisibility(View.VISIBLE);
            binding.requestByEmployeeName.setText(assetRequestedByName);
        } else {
            binding.requestContainer.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.scan_button:
                callSubmit();
                break;

            case R.id.left_iv:
                finish();
                break;

            case R.id.accept_tv:
                Utils.showProgressDialog(context, getString(R.string.loading));
                viewModel.approveAssetRequest(req_id, mEmp_id);
                break;

            case R.id.cancel_tv:
                Utils.showProgressDialog(context, getString(R.string.loading));
                viewModel.cancelAssetRequest(req_id, mEmp_id);
                break;

        }
    }


    /**
     * Put action onclick according to status code
     */
    private void callSubmit() {

        switch (ASSET_STATUS) {

            case AppUtils.STATUS_ALL_ASSET_LIST:
                if (Utils.checkPermissions(this, AppUtils.STORAGE_CAMERA_PERMISSIONS)) {
                    startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA);
                    }
                }
                break;

            case AppUtils.STATUS_TRANSFER_ASSET_LIST:
                if (Utils.checkPermissions(this, AppUtils.STORAGE_CAMERA_PERMISSIONS)) {
                    startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA);
                    }
                }
                break;

            case AppUtils.STATUS_SCANED_CODE:
                beforeSendRequestValidation();
                break;

        }
    }

    /**
     * validate request for request,handover and transfer
     */
    private void beforeSendRequestValidation() {
        Utils.showProgressDialog(context, getString(R.string.loading));

        /**
         * Asset keep request
         */
        if (asset_emp_id == null || asset_emp_id.equals("0")) {
            viewModel.keepAssetRequest(qr_code, mEmp_id);


            /**
             * Asset handover
             */
        } else if (asset_emp_id.equals(mEmp_id)) {
            viewModel.sendHandoverRequest(qr_code, mEmp_id);

            /**
             * Asset transfer
             */
        } else {
            viewModel.sendTransferRequest(qr_code, mEmp_id);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * Get asset result from web service after scan Qr code
         */
        if (requestCode == AppUtils.REQUEST_CODE_QR_SCAN) {

            if (resultCode != RESULT_OK || data == null) {
                Utils.showSnackBar(binding, getString(R.string.unable_to_scan_qr));
                return;
            }
            //Getting the passed result
            String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String qr = jsonObject.getString("qr_code_number");

                if (!qr_code.equals("") && !qr.equals(qr_code)) {
                    Utils.showAlert(context, "", getString(R.string.sorry_qr_code_does_not_match), "ok", "" +
                            "", AppUtils.dialog_ok_click, this);
                    return;
                }
                validateAfterScan(qr);
                ASSET_STATUS = AppUtils.STATUS_SCANED_CODE;

            } catch (JSONException e) {
                e.printStackTrace();
                Utils.showSnackBar(binding, getString(R.string.unable_to_scan_qr));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtils.REQUEST_CODE_CAMERA || Utils.onRequestPermissionsResult(permissions, grantResults)) {
            startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
        } else {
            Utils.showSnackBar(binding, getString(R.string.allow_camera_permission));
        }
    }

}
