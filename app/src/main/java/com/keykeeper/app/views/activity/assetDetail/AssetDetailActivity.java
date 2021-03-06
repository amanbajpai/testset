package com.keykeeper.app.views.activity.assetDetail;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.keykeeper.app.databinding.ActivityAssetDetailLayoutBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.AssetDetailBean;
import com.keykeeper.app.model.bean.BaseResponse;
import com.keykeeper.app.model.bean.LoginResponseBean;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.qrcodescanner.ScannerActivity;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.keyMap.KeyOnMapActivity;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

import org.json.JSONException;
import org.json.JSONObject;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;

/**
 * Created by akshaydashore on 23/8/18
 */
public class AssetDetailActivity extends BaseActivity implements DialogClickListener {

    private String TAG = getClass().getName();
    private boolean HAS_SCANNED = false;
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
    private String tag_number;
    private boolean isBoxScanned;

    private String box_number;
    private String asset_name;
    String asset_id = "0";
    private AssetDetailBean.Result resultBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setCustomActionBar();
        asset_id = getIntent().getStringExtra(AppUtils.ASSET_ID);
        initializeViews();
    }


    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.asset_detail), true, false, false, true, false, this);
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
        mEmp_id = AppSharedPrefs.getInstance(this).getEmployeeID();

        LoginResponseBean.Result bean = Utils.getUserDetail();
        box_number = bean.getQrCodeNumber();
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

    @Override
    protected void onResume() {
        super.onResume();
    }


    private String validateTextView(boolean isConfirm) {

        /**
         * Asset keep request
         */
        if (asset_emp_id == null || asset_emp_id.equals("0")) {
            if (isConfirm || IS_FROM_SCANNER)
                return getString(R.string.confirm_request);
            else
                return getString(R.string.request_key);

            /**
             * Asset handover
             */
        } else if (asset_emp_id.equals(mEmp_id)) {
           /* if (isConfirm || IS_FROM_SCANNER)
                return getString(R.string.confirm_transfer);
            else
                return getString(R.string.transfer_ownership);*/

            HAS_SCANNED = true;
            return getString(R.string.confirm_transfer);


            /**
             * Asset transfer
             */
        } else {
            if (isConfirm || IS_FROM_SCANNER)
                return getString(R.string.confirm_request);
            else
                return getString(R.string.request_key);

        }
    }


    private void validateSubmitView() {

        switch (ASSET_STATUS) {

            case AppUtils.STATUS_ASSET_SEND_REQUEST1:
                req_id = getIntent().getIntExtra(AppUtils.ASSET_REQUEST_ID, -1);
                binding.pendingStatusContainer.setVisibility(View.VISIBLE);
                binding.cancelTv.setOnClickListener(this);
                binding.acceptTv.setOnClickListener(this);
                break;

            case AppUtils.STATUS_ASSET_SEND_REQUEST:
                req_id = getIntent().getIntExtra(AppUtils.ASSET_REQUEST_ID, -1);
                binding.pendingStatusContainerRequest.setVisibility(View.VISIBLE);
                binding.requestCancelTv.setOnClickListener(this);
                break;

            default:
                String text = validateTextView(HAS_SCANNED);
                binding.scanButton.setVisibility(View.VISIBLE);
                binding.scanButton.setText(text);

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
                    Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_to_finish, AssetDetailActivity.this);
                }
                return;
            }
            if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                setDataFromBean(bean);
                validateSubmitView();
            }

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
        binding.scanButton.setOnClickListener(AssetDetailActivity.this);
        asset_emp_id = Utils.validateInt(resultBean.getEmployeeId()) + "";
        qr_code = resultBean.getQrCodeNumber();
        tag_number = resultBean.getTagNumber();
        asset_name = resultBean.getAssetName();

        if (Utils.validateStringValue(resultBean.getEmployeeName()).equals("")) {
            binding.employeeContainer.setVisibility(View.GONE);
        }

        if (!Utils.validateStringValue(assetRequestedByName).equals("")) {
            binding.requestContainer.setVisibility(View.VISIBLE);
            binding.requestByEmployeeName.setText(assetRequestedByName);
        } else {
            binding.requestContainer.setVisibility(View.GONE);
        }

        if (!Utils.validateIntValue(bean.getResult().getAssetType()).equalsIgnoreCase(AppUtils.ASSET_CUSTOMER)) {
            binding.customerDetailLayout.setVisibility(View.GONE);
        }

        if (Utils.validateStringValue(resultBean.getEmployeeName()).equals("") ||
                Utils.validateInteger(resultBean.getEmployeeId()).equals(mEmp_id)) {
            binding.assigneeContainer.setVisibility(View.GONE);
        } else {
            binding.assigneeContainer.setVisibility(View.VISIBLE);
            binding.assigneeTv.setText(bean.getResult().getEmployeeName());
        }

        asset_id = bean.getResult().getAssetId() + "";

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.scan_button:
                if (checkLocationDepedency())
                    callSubmit();
                break;

            case R.id.left_iv:
                finish();
                break;

            case R.id.map_iv:
                Intent i = new Intent(this, KeyOnMapActivity.class);
                i.putExtra(AppUtils.ASSET_ID, asset_id);
                i.putExtra(AppUtils.ASSET_BEAN, resultBean);
                startActivity(i);
                break;

            case R.id.accept_tv:
                Utils.showProgressDialog(context, getString(R.string.loading));
                viewModel.approveAssetRequest(req_id, mEmp_id);
                break;

            case R.id.cancel_tv:
                Utils.showProgressDialog(context, getString(R.string.loading));
                viewModel.cancelAssetRequest(req_id, mEmp_id);
                break;

            case R.id.request_cancel_tv:
                Utils.showProgressDialog(context, getString(R.string.loading));
                viewModel.cancelAssetRequest(req_id, mEmp_id);
                break;

        }
    }


//    OnLocationUpdatedListener onLocationUpdatedListener = new OnLocationUpdatedListener() {
//        @Override
//        public void onLocationUpdated(Location location) {
//            if (location.getLatitude() != 0 && location.getLongitude() != 0) {
//                // Utils.hideProgressDialog();
//                String lat = location.getLatitude() + "";
//                String lng = location.getLongitude() + "";
//                Log.e(" onLocationUpdated: ", lat + " " + lng);
//                AppSharedPrefs.setLatitude(lat);
//                AppSharedPrefs.setLongitude(lng);
//                AppSharedPrefs.setSpeed(location.getSpeed() + "");
//                Utils.hideProgressDialog();
//                callSubmit();
//
//            } else {
//                fetchLocation(onLocationUpdatedListener);
//            }
//        }
//    };



    LocationChangeListener locationChangeListener  = new LocationChangeListener() {
        @Override
        public void onLocationChange(Location location) {
            if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                // Utils.hideProgressDialog();
                String lat = location.getLatitude() + "";
                String lng = location.getLongitude() + "";
                Log.e(TAG+" onLocationUpdated: ", lat + " " + lng);
                AppSharedPrefs.setLatitude(lat);
                AppSharedPrefs.setLongitude(lng);
                AppSharedPrefs.setSpeed(location.getSpeed() + "");
                Utils.hideProgressDialog();
                callSubmit();

            } else {
                getLocation(locationChangeListener);
            }
        }
    };



    public boolean checkLocationDepedency() {

        if (Utils.checkPermissions(AssetDetailActivity.this, AppUtils.LOCATION_PERMISSIONS)) {
            if (Utils.isGpsEnable(context)) {
                String lat = AppSharedPrefs.getLatitude();
                String lng = AppSharedPrefs.getLatitude();
                if (lat.equalsIgnoreCase("0") || lng.equalsIgnoreCase("0")) {
                    Utils.showProgressDialog(context, getString(R.string.fetching_location));
//                    fetchLocation(onLocationUpdatedListener);
                    getLocation(locationChangeListener);
                    return false;
                }
                return true;
            } else {
                displayLocationSettingsRequest();
                return false;
            }
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(AppUtils.LOCATION_PERMISSIONS, AppUtils.REQUEST_CODE_LOCATION);
            }
            return false;
        }
    }


//    public void fetchLocation(OnLocationUpdatedListener listener) {
//        // Utils.showProgressDialog(context, "Fetching location...");
//        LocationParams.Builder builder = new LocationParams.Builder();
//        builder.setAccuracy(LocationAccuracy.HIGH);
//        builder.setDistance(5); // in Meteres
//        builder.setInterval(1000);
//        LocationParams params = builder.build();
//
//        SmartLocation.LocationControl location_control = SmartLocation.with(context).location().config(params);
//        location_control.start(listener);
//
//    }

    /**
     * Put action onclick according to status code
     */
    private void callSubmit() {

        /*Check for Location dependency here*/


        if (IS_FROM_SCANNER || HAS_SCANNED) {

            /**
             * Method calling for check handover key box scanner
             */
            if (!hasBoxVerify()) {
                //    startActivityForResult(new Intent(context, ScannerActivity.class), AppUtils.REQUEST_QR_SCAN_FOR_BOX_VERIFY);

                Intent i = new Intent(this, ScannerActivity.class);
                i.putExtra("title", getString(R.string.txt_qr_code_screen_title_from_transfer_ownership));
                startActivityForResult(i, AppUtils.REQUEST_QR_SCAN_FOR_BOX_VERIFY);

                return;
            }
            beforeSendRequestValidation();

        } else {
            if (Utils.checkPermissions(this, AppUtils.STORAGE_CAMERA_PERMISSIONS)) {
                //startActivityForResult(new Intent(context, ScannerActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);

                Intent i = new Intent(this, ScannerActivity.class);
                i.putExtra("title", getString(R.string.txt_qr_code_screen_title_from_keydetail));
                startActivityForResult(i, AppUtils.REQUEST_CODE_QR_SCAN);

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA);
                }
            }
        }
    }

    private boolean hasBoxVerify() {
        //Check handover condition
        if (asset_emp_id.equals(mEmp_id)) {
            return isBoxScanned;
        }
        return true;
    }


    /**
     * validate request for request,handover and transfer
     */
    private void beforeSendRequestValidation() {

        if (!Utils.isGpsEnable(context)) {
            displayLocationSettingsRequest();
            return;
        }

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
            viewModel.sendHandoverRequest(qr_code, mEmp_id, box_number);

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
            if (data == null)
                return;
            //Getting the passed result
            if (data.getBooleanExtra(AppUtils.IS_MANUAL_QR, false)) {
                String result = data.getStringExtra(AppUtils.QR_NUMBER_MANUAL_SCAN_SUCCESS);
                String qr_tag_number = data.getStringExtra(AppUtils.SCAN_SUCCESS);

                if (!tag_number.equals("") && !qr_tag_number.equals(tag_number)) {
                    Utils.showAlert(context, "", getString(R.string.sorry_qr_code_does_not_match), "ok", "" +
                            "", AppUtils.dialog_ok_click, this);
                    return;
                }
                qr_code = qr_tag_number;
                HAS_SCANNED = true;
                validateSubmitView();

            } else {
                String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String qr = jsonObject.getString("qr_code_number");

                    if (!qr_code.equals("") && !qr.equals(qr_code)) {
                        Utils.showAlert(context, "", getString(R.string.sorry_qr_code_does_not_match), "ok", "" +
                                "", AppUtils.dialog_ok_click, this);
                        return;
                    }
                    HAS_SCANNED = true;
                    validateSubmitView();

                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Utils.showSnackBar(binding, getString(R.string.invalid_qr_code));
                    if (!asset_name.equals("") && !result.equals(asset_name)) {
                        Utils.showAlert(context, "", getString(R.string.sorry_qr_code_does_not_match), "ok", "" +
                                "", AppUtils.dialog_ok_click, this);
                        return;
                    }
                    HAS_SCANNED = true;
                    validateSubmitView();
                }
            }

        } else if (requestCode == AppUtils.REQUEST_QR_SCAN_FOR_BOX_VERIFY) {
            if (resultCode != RESULT_OK || data == null) {
                Utils.showSnackBar(binding, getString(R.string.unable_to_scan_qr));
                return;
            }
            if (data == null)
                return;


            //Getting the passed result
            if (data.getBooleanExtra(AppUtils.IS_MANUAL_QR, false)) {
                String qr_tag_number = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                if (!qr_tag_number.equals("") && qr_tag_number.equals(box_number)) {
                    isBoxScanned = true;
                } else {
                    isBoxScanned = false;
                    Utils.showAlert(context, "", getString(R.string.sorry_qr_code_does_not_match), "ok", "" +
                            "", AppUtils.dialog_ok_click, this);
                    return;
                }

            } else {
                String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String qr = jsonObject.getString("qr_code_number");
                    if (!qr.equals("") && qr.equals(box_number)) {
                        isBoxScanned = true;
                    } else {
                        isBoxScanned = false;
                        Utils.showAlert(context, "", getString(R.string.sorry_qr_code_does_not_match), "ok", "" +
                                "", AppUtils.dialog_ok_click, this);
                        return;
                    }

                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Utils.showSnackBar(binding, getString(R.string.invalid_qr_code));
                    if (!result.equals("") && result.equals(box_number)) {
                        isBoxScanned = true;
                    } else {
                        isBoxScanned = false;
                        Utils.showAlert(context, "", getString(R.string.sorry_qr_code_does_not_match), "ok", "" +
                                "", AppUtils.dialog_ok_click, this);
                        return;
                    }
                }
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i(AssetDetailActivity.class.getCanonicalName(), "User agreed to make required location settings changes.");
                    getLocation(locationChangeListener);
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(BaseActivity.class.getCanonicalName(), "User chose not to make required location settings changes.");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            displayLocationSettingsRequest();
                        }
                    }, 1000);

                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtils.REQUEST_CODE_CAMERA) {
            if (Utils.onRequestPermissionsResult(permissions, grantResults)) {
                Intent i = new Intent(this, ScannerActivity.class);
                i.putExtra("title", getString(R.string.txt_qr_code_screen_title_from_keydetail));
                startActivityForResult(i, AppUtils.REQUEST_CODE_QR_SCAN);
            } else {
                Utils.showSnackBar(binding, getString(R.string.allow_camera_permission));
            }

        } else if (requestCode == AppUtils.REQUEST_CODE_LOCATION) {
            if (Utils.onRequestPermissionsResult(permissions, grantResults)) {
                if (Utils.isGpsEnable(context)) {
                    getLocation(locationChangeListener);
                } else {
                    displayLocationSettingsRequest();
                }
            } else {
                Utils.showSnackBar(binding, getString(R.string.allow_location_permission));
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
                        if (Utils.checkPermissions(AssetDetailActivity.this, AppUtils.LOCATION_PERMISSIONS)) {
                            beforeSendRequestValidation();
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
                            status.startResolutionForResult(AssetDetailActivity.this, REQUEST_CHECK_SETTINGS);
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


}
