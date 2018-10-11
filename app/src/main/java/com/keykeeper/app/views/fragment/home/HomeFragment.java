package com.keykeeper.app.views.fragment.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.keykeeper.app.databinding.HomeFragmentLayoutBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.EmployeeOwnedAssetsListResponse;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.qrcodescanner.ScannerActivity;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.AssetListActivity;
import com.keykeeper.app.views.activity.assetDetail.AssetDetailActivity;
import com.keykeeper.app.views.activity.chat.ChatActivity;
import com.keykeeper.app.views.activity.history.HistoryActivity;
import com.keykeeper.app.views.activity.transfer.TransferActivity;
import com.keykeeper.app.views.base.BaseFragment;
import com.keykeeper.app.views.fragment.testDrive.TestDriveAssetDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;

public class HomeFragment extends BaseFragment implements DialogClickListener {

    private Context context;
    private HomeFragmentLayoutBinding binding;
    HomeFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_layout, container, false);
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
        initializeViews(binding.getRoot());
        return binding.getRoot();

    }

    @Override
    public void initializeViews(View rootView) {
        context = getActivity();
        binding.assetRl.setOnClickListener(this);
        binding.scanRl.setOnClickListener(this);
        binding.historyRl.setOnClickListener(this);
        binding.handOverRl.setOnClickListener(this);
        binding.takeoutRl.setOnClickListener(this);
        binding.chatRl.setOnClickListener(this);

        viewModel.validator.observe(this, observer);
        viewModel.response_allassets_owned.observe(this, responseAssetsOwnedCurrently);
        binding.enableGpsLl.setOnClickListener(this);
        if (!Utils.isGpsEnable(context)){
            binding.enableGpsLl.setVisibility(View.VISIBLE);
        }else {
            binding.enableGpsLl.setVisibility(View.GONE);
        }
        if (!Utils.isLocationInHighMode(context)){
            Utils.showAlert(context,"",getString(R.string.enable_high_accuract),"ok","",AppUtils.REQUEST_CODE_LOCATION_HIGH_ACCURACY,this);
        }
    }



    @Override
    public void onClick(View v) {

        if (!Connectivity.isConnected()) {
            Utils.showSnackBar(binding, getString(R.string.internet_connection));
            return;
        }

        switch (v.getId()) {
            case R.id.asset_rl:
                startActivity(new Intent(context, AssetListActivity.class));
                break;

            case R.id.scan_rl:
                if (Utils.checkPermissions(getActivity(), AppUtils.STORAGE_CAMERA_PERMISSIONS)) {
                    //  startActivityForResult(new Intent(context, ScannerActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
                    Intent i = new Intent(getActivity(), ScannerActivity.class);
                    i.putExtra("title", getString(R.string.txt_qr_code_screen_title_from_home));
                    startActivityForResult(i, AppUtils.REQUEST_CODE_QR_SCAN);

                } else {
                    requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA);
                }
                break;

            case R.id.history_rl:
                startActivity(new Intent(context, HistoryActivity.class));
                break;

            case R.id.hand_over_rl:
                startActivity(new Intent(context, TransferActivity.class));
                break;

            case R.id.takeout_rl:
                if (Utils.checkPermissions(getActivity(), AppUtils.STORAGE_CAMERA_PERMISSIONS)) {
                    // startActivityForResult(new Intent(context, ScannerActivity.class), AppUtils.REQUEST_CODE_QR_SCAN_FOR_DRIVE);

                    Intent i = new Intent(getActivity(), ScannerActivity.class);
                    i.putExtra("title", getString(R.string.txt_qr_code_screen_title_from_home));
                    startActivityForResult(i, AppUtils.REQUEST_CODE_QR_SCAN_FOR_DRIVE);

                } else {
                    requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA_FOR_DRIVE);
                }
                break;

            case R.id.chat_rl:
                if (!TextUtils.isEmpty(AppSharedPrefs.getInstance(context).getChatUrl())) {
                    startActivity(new Intent(context, ChatActivity.class));
                }
                break;

            case R.id.enable_gps_ll:
                displayLocationSettingsRequest();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtils.REQUEST_CODE_CAMERA) {
            if (Utils.onRequestPermissionsResult(permissions, grantResults)) {
                // startActivityForResult(new Intent(context, ScannerActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);

                Intent i = new Intent(getActivity(), ScannerActivity.class);
                i.putExtra("title", getString(R.string.txt_qr_code_screen_title_from_home));
                startActivityForResult(i, AppUtils.REQUEST_CODE_QR_SCAN);

            } else {
                Utils.showSnackBar(binding, getString(R.string.allow_camera_permission));
            }
        } else if (requestCode == AppUtils.REQUEST_CODE_CAMERA_FOR_DRIVE) {
            if (Utils.onRequestPermissionsResult(permissions, grantResults)) {
                //startActivityForResult(new Intent(context, ScannerActivity.class), AppUtils.REQUEST_CODE_QR_SCAN_FOR_DRIVE);

                Intent i = new Intent(getActivity(), ScannerActivity.class);
                i.putExtra("title", getString(R.string.txt_qr_code_screen_title_from_home));
                startActivityForResult(i, AppUtils.REQUEST_CODE_QR_SCAN_FOR_DRIVE);

            } else {
                Utils.showSnackBar(binding, getString(R.string.allow_camera_permission));
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * Get asset result from web service after scan Qr code
         */
        if (requestCode == AppUtils.REQUEST_CODE_QR_SCAN) {

            if (resultCode != getActivity().RESULT_OK) {
                Utils.showSnackBar(binding, getString(R.string.unable_to_scan_qr));
                return;
            }
            if (data == null)
                return;
            //Getting the passed result
            if (data.getBooleanExtra(AppUtils.IS_MANUAL_QR, false)) {
                String result = data.getStringExtra(AppUtils.QR_NUMBER_MANUAL_SCAN_SUCCESS);
                String qr_tag_number = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                Intent intent = new Intent(context, AssetDetailActivity.class);
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.SCAN_SUCCESS);
                intent.putExtra(AppUtils.SCANED_QR_CODE, qr_tag_number);
                startActivity(intent);

            } else {
                String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String mResult = jsonObject.getString("qr_code_number");
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
                    intent.putExtra(AppUtils.SCANED_QR_CODE, mResult);
                    startActivity(intent);
                } catch (JSONException e) {

                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
                    intent.putExtra(AppUtils.SCANED_QR_CODE, result);
                    startActivity(intent);
                }
            }

            /**
             * call for test drive scanner
             */
        } else if (requestCode == AppUtils.REQUEST_CODE_QR_SCAN_FOR_DRIVE) {

            if (resultCode != getActivity().RESULT_OK) {
                Utils.showSnackBar(binding, getString(R.string.unable_to_scan_qr));
                return;
            }
            if (data == null)
                return;
            //Getting the passed result
            if (data.getBooleanExtra(AppUtils.IS_MANUAL_QR, false)) {
                String result = data.getStringExtra(AppUtils.QR_NUMBER_MANUAL_SCAN_SUCCESS);
                String qr_tag_number = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                Intent intent = new Intent(context, TestDriveAssetDetailActivity.class);
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.SCAN_SUCCESS);
                intent.putExtra(AppUtils.SCANED_QR_CODE, qr_tag_number);
                startActivity(intent);

            } else {
                String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String mResult = jsonObject.getString("qr_code_number");
                    Intent intent = new Intent(context, TestDriveAssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
                    intent.putExtra(AppUtils.SCANED_QR_CODE, mResult);
                    startActivity(intent);
                } catch (JSONException e) {
                    Intent intent = new Intent(context, TestDriveAssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
                    intent.putExtra(AppUtils.SCANED_QR_CODE, result);
                    startActivity(intent);
                }
            }

        }

    }


    @Override
    public void onDialogClick(int which, int requestCode) {

        switch (requestCode){

            case AppUtils.REQUEST_CODE_LOCATION_HIGH_ACCURACY:{
                     switch (which){
                         case DialogInterface.BUTTON_POSITIVE:
                              Utils.sendToLocationSetting(context);
                             break;
                     }
            }
                   break;
        }
    }

    Observer<EmployeeOwnedAssetsListResponse> responseAssetsOwnedCurrently = new Observer<EmployeeOwnedAssetsListResponse>() {

        @Override
        public void onChanged(@Nullable EmployeeOwnedAssetsListResponse employeeOwnedAssetsListResponse) {

            Utils.hideProgressDialog();
            if (employeeOwnedAssetsListResponse != null && employeeOwnedAssetsListResponse.getResults() != null && employeeOwnedAssetsListResponse.getResults().size() > 0) {
                ArrayList<EmployeeOwnedAssetsListResponse.Result> resultArrayList = employeeOwnedAssetsListResponse.getResults();
                if (resultArrayList.size() > 0) {
                    storeOwnedKeyIdsPreferences(employeeOwnedAssetsListResponse);
                    Utils.startLocationStorage(context,true);
                } else {
                    storeOwnedKeyIdsPreferences(employeeOwnedAssetsListResponse);
                    Utils.stopLocationStorage(context);
                }
            } else {
                Utils.stopLocationStorage(context);
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
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.server_error));
                    break;
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        // Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getCurrentAssetsOwned();
        context.registerReceiver(receiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }


    private void storeOwnedKeyIdsPreferences(EmployeeOwnedAssetsListResponse employeeOwnedAssetsListResponse) {
        String ownedKeys = null;
        if (employeeOwnedAssetsListResponse != null && employeeOwnedAssetsListResponse.getResults().size() > 0) {
            for (int i = 0; i < employeeOwnedAssetsListResponse.getResults().size(); i++) {
                if (!TextUtils.isEmpty(employeeOwnedAssetsListResponse.getResults().get(i).getAsset_id())) {
                    if (ownedKeys != null) {
                        ownedKeys = ownedKeys + "," + employeeOwnedAssetsListResponse.getResults().get(i).getAsset_id();
                    } else {
                        ownedKeys = employeeOwnedAssetsListResponse.getResults().get(i).getAsset_id();
                    }
                }
            }
            AppSharedPrefs.getInstance(context).setOwnedKeyIds(ownedKeys);
        } else {
            AppSharedPrefs.getInstance(context).setOwnedKeyIds("");
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (Utils.isGpsEnable(context)) {
                binding.enableGpsLl.setVisibility(View.GONE);
            } else {
                binding.enableGpsLl.setVisibility(View.VISIBLE);
            }
        }
    };


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
                        if (Utils.checkPermissions(getActivity(), AppUtils.LOCATION_PERMISSIONS)) {

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
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
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