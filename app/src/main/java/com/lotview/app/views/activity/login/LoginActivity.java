package com.lotview.app.views.activity.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.lotview.app.R;
import com.lotview.app.databinding.LoginActivityBinding;
import com.lotview.app.model.bean.LoginResponseBean;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.forgot_password.ForgotPasswordActivity;
import com.lotview.app.views.activity.home.HomeActivity;
import com.lotview.app.views.activity.testdrive.TestDriveStuckActivity;
import com.lotview.app.views.base.BaseActivity;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;

/**
 * Created by akshaydashore on 22/8/18
 */
public class LoginActivity extends BaseActivity {

    LoginViewModel viewModel;
    private Context context;
    private LoginActivityBinding binding;
    Observer validatorObserver = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.empty_id:
                    Utils.showSnackBar(binding, getString(R.string.enter_employeeid));
                    break;

                case AppUtils.empty_password:
                    Utils.showSnackBar(binding, getString(R.string.enter_password));
                    break;

                case AppUtils.invalid_mail:
                    Utils.showSnackBar(binding, getString(R.string.enter_valid_employeeid));
                    break;

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

    Observer<LoginResponseBean> login_response_observer = new Observer<LoginResponseBean>() {

        @Override
        public void onChanged(@Nullable LoginResponseBean loginBean) {

            if (loginBean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            if (loginBean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", loginBean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, viewModel);
                return;
            }

            Gson gson = new Gson();

            String user_detail = gson.toJson(loginBean.getResult());
            String empId = loginBean.getResult().getEmployeeId() + "";
            String comId = loginBean.getResult().getCompanyId() + "";
            String empName = loginBean.getResult().getFirstname() + "";
            boolean isRemember = true;

            AppSharedPrefs.getInstance(context).setUserDetail(user_detail);
            AppSharedPrefs.getInstance(context).setEmployeeID(empId);
            AppSharedPrefs.getInstance(context).setCompanyID(comId);
            AppSharedPrefs.getInstance(context).setEmployeeName(empName);
            AppSharedPrefs.getInstance(context).setAccessToken(loginBean.getAccessToken());
            AppSharedPrefs.getInstance(context).setRememberMe(isRemember);
            AppSharedPrefs.getInstance(context).setLogin(true);
            AppSharedPrefs.getInstance(context).setPassword(binding.etPassword.getText().toString());

            String url = loginBean.getResult().getChatUrl();
            url = url.replaceAll(" ", "%20");
            AppSharedPrefs.getInstance(context).setChatUrl(url);

            if (loginBean.getResult().getRunningTestDriveResponse() != null) {
                if (loginBean.getResult().getRunningTestDriveResponse().getTest_drive_start_status() != null) {

                    AppSharedPrefs.getInstance(context).setTestDriveID(loginBean.getResult().getRunningTestDriveResponse().getAsset_employee_test_drive_id());
                    AppSharedPrefs.setTestDriveAssetId(loginBean.getResult().getRunningTestDriveResponse().getAsset_id());
                    startActivity(new Intent(LoginActivity.this, TestDriveStuckActivity.class));
                    finish();

                } else {
                    AppSharedPrefs.getInstance(context).setTestDriveID("0");
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }


            } else {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

        }
    };
    private SmartLocation.LocationControl location_control;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initializeViews();
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        binding.tvLogin.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        viewModel.validator.observe(this, validatorObserver);
        viewModel.response_validator.observe(this, login_response_observer);
        viewModel.validator.observe(this, validatorObserver);


        if (Utils.isGpsEnable(context)) {
            if (Utils.checkPermissions(this, AppUtils.LOCATION_PERMISSIONS)) {
                getLocation();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(AppUtils.LOCATION_PERMISSIONS, AppUtils.REQUEST_CODE_LOCATION);
                }
            }
        } else {
            displayLocationSettingsRequest();
        }
    }

    private void getLocation() {

        LocationParams.Builder builder = new LocationParams.Builder();
        builder.setAccuracy(LocationAccuracy.HIGH);
        builder.setDistance(5); // in Meteres
        builder.setInterval(1000);
        LocationParams params = builder.build();


        location_control = SmartLocation.with(context).location().config(params);

        location_control.start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {

                String lat = location.getLatitude() + "";
                String lng = location.getLongitude() + "";
                Log.e(lat + " onLocationUpdated: ", lng + "<<");
                AppSharedPrefs.setLatitude(lat);
                AppSharedPrefs.setLongitude(lng);
                AppSharedPrefs.setSpeed(location.getSpeed() + "");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                Utils.showProgressDialog(context, getString(R.string.loading));
                viewModel.doLogin(binding);
                break;

            case R.id.tv_forgot_password:
                startActivity(new Intent(context, ForgotPasswordActivity.class));
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppUtils.REQUEST_CODE_CAMERA || Utils.onRequestPermissionsResult(permissions, grantResults)) {
            getLocation();
        } else {
            Utils.showSnackBar(binding, getString(R.string.allow_camera_permission));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (location_control != null) {
            location_control.stop();
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
                        if (Utils.checkPermissions(LoginActivity.this, AppUtils.LOCATION_PERMISSIONS)) {
                            getLocation();
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
                            status.startResolutionForResult(LoginActivity.this, REQUEST_CHECK_SETTINGS);
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
