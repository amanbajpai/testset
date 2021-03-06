package com.keykeeper.app.views.fragment.testDrive;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.keykeeper.app.R;
import com.keykeeper.app.databinding.TestDriveAssetDetailBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.AssetDetailBean;
import com.keykeeper.app.model.bean.TestDriveResponseBean;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.testdrive.TestDriveStuckActivity;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;


/**
 * Created by akshaydashore on 15/9/18
 */
public class TestDriveAssetDetailActivity extends BaseActivity implements DialogClickListener {

    public static int ASSET_STATUS = 1;
    private Context context;
    TestDriveAssetDetailBinding binding;
    TestDriveAssetViewModel viewModel;
    private String mEmp_id;
    private String qr_code;
    boolean IS_FROM_SCANNER = false;
    private String assetRequestedByName;
    private String tag_number;
    private boolean isDriveStart;
    private int assetId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getLocation(null);
        initializeViews();
    }


    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        if (isDriveStart) {
            customActionBar.setActionbar(getString(R.string.test_drive), false, false, false, false, false, this);
        } else {
            customActionBar.setActionbar(getString(R.string.test_drive), true, false, false, false, false, this);
        }
    }


    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.test_drive_asset_detail);
        viewModel = ViewModelProviders.of(this).get(TestDriveAssetViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_validator.observe(this, response_observer);
        viewModel.response_testdrive_start.observe(this, responseTestDriveStart);


        /**
         * call web service to get data using qr code
         */
        mEmp_id = AppSharedPrefs.getInstance(this).getEmployeeID();

        qr_code = getIntent().getStringExtra(AppUtils.SCANED_QR_CODE);
        Utils.showProgressDialog(context, getString(R.string.loading));
        ASSET_STATUS = getIntent().getIntExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
        assetRequestedByName = getIntent().getStringExtra(AppUtils.ASSET_REQUESTED_BY_EMP_NAME);
        viewModel.getAssetDetail(qr_code, mEmp_id);
        isDriveStart = AppSharedPrefs.isTestDriveRunning();
        setCustomActionBar();
//        if (isDriveStart) {
//            binding.driveStartButton.setText(R.string.drive_started);
//        }

    }


    /**
     * Asset detail observer
     */
    Observer<AssetDetailBean> response_observer = new Observer<AssetDetailBean>() {
        @Override
        public void onChanged(@Nullable AssetDetailBean bean) {

            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, TestDriveAssetDetailActivity.this);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, TestDriveAssetDetailActivity.this);
                return;
            }
            if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                setDataFromBean(bean);
            }

        }
    };


    /**
     * Test Drive Start observer
     */
    Observer<TestDriveResponseBean> responseTestDriveStart = new Observer<TestDriveResponseBean>() {
        @Override
        public void onChanged(@Nullable TestDriveResponseBean bean) {
            Utils.hideProgressDialog();
            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, TestDriveAssetDetailActivity.this);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, TestDriveAssetDetailActivity.this);
                return;
            }
            if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                AppSharedPrefs.getInstance(context).setTestDriveRunning(true);
                isDriveStart = true;
                AppSharedPrefs.getInstance(context).setTestDriveID(bean.getResult().getAssetEmployeeTestDriveId());
//                binding.driveStartButton.setText(R.string.drive_started);
                startActivity(new Intent(context, TestDriveStuckActivity.class));
                finish();
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
        assetId = resultBean.getAssetId();
        AppSharedPrefs.setAssetNameforRunningTestDrive(resultBean.getAssetName());
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

        String eid = Utils.validateInteger(resultBean.getEmployeeId());

        if (eid.equals(mEmp_id) || eid.equals("0")) {

            binding.availability.setVisibility(View.GONE);
            binding.availabilityHeader.setVisibility(View.GONE);
        }

        if (resultBean.getAssetType() == Integer.valueOf(AppUtils.ASSET_CUSTOMER)) {
            binding.assetNameHeaderTv.setText(R.string.tag_number);
        }


        if (Utils.validateStringToValue(resultBean.getVin()).equals("")) {
            binding.codeNumber.setVisibility(View.GONE);
            binding.codeNumberHeader.setVisibility(View.GONE);
        }

        binding.make.setText(resultBean.getDescription());
        binding.driveStartButton.setOnClickListener(TestDriveAssetDetailActivity.this);
        qr_code = resultBean.getQrCodeNumber();
        tag_number = resultBean.getTagNumber();

        if (Utils.validateStringValue(resultBean.getEmployeeName()).equals("")) {
            binding.employeeContainer.setVisibility(View.GONE);
        }

        if (!Utils.validateStringValue(assetRequestedByName).equals("")) {
            binding.requestContainer.setVisibility(View.VISIBLE);
            binding.requestByEmployeeName.setText(assetRequestedByName);
        } else {
            binding.requestContainer.setVisibility(View.GONE);
        }

        if (Utils.validateIntValue(bean.getResult().getAssetType()) != AppUtils.ASSET_CUSTOMER) {
            binding.customerDetailLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.left_iv:
                finish();
                break;

            case R.id.drive_start_button:
                validateTestDrive();
                break;
        }

    }


    @Override
    public void onDialogClick(int which, int requestCode) {

        switch (requestCode) {
            case AppUtils.dialog_ok_to_finish:
                finish();
                break;

            case AppUtils.dialog_gps_enable: {

                switch (which) {

                    case DialogInterface.BUTTON_POSITIVE:
                        displayLocationSettingsRequest();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        setCustomActionBar();
                        AppSharedPrefs.getInstance(context).setQrCode(qr_code);
                        Utils.showProgressDialog(context, getString(R.string.loading));
                        viewModel.doStartTestDrive(mEmp_id, assetId, AppSharedPrefs.getLatitude(), AppSharedPrefs.getLongitude(), Utils.getCurrentTimeStampDate(), Utils.getCurrentUTCTimeStampDate(), qr_code);

                        break;
                }
            }
            break;
        }

    }

    public void validateTestDrive() {

        if (isDriveStart) {

            setCustomActionBar();

            AppSharedPrefs.getInstance(context).setQrCode("");

        } else {

            if (Utils.isGpsEnable(context)) {
                setCustomActionBar();
                AppSharedPrefs.getInstance(context).setQrCode(qr_code);
                Utils.showProgressDialog(context, getString(R.string.loading));
                viewModel.doStartTestDrive(mEmp_id, assetId, AppSharedPrefs.getLatitude(), AppSharedPrefs.getLongitude(), Utils.getCurrentTimeStampDate(), Utils.getCurrentUTCTimeStampDate(), qr_code);
            } else {
                Utils.showAlert(context, "", getString(R.string.gps_enable_test_drive), "Ok", "Cancel", AppUtils.dialog_gps_enable, this);
            }

        }

    }


    private void displayLocationSettingsRequest() {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("tag", "All location settings are satisfied.");
                        if (Utils.checkPermissions(TestDriveAssetDetailActivity.this, AppUtils.LOCATION_PERMISSIONS)) {

                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(AppUtils.LOCATION_PERMISSIONS, AppUtils.REQUEST_CODE_LOCATION);
                            }
                        }
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("tag", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(TestDriveAssetDetailActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("tag", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("tag", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
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

}
