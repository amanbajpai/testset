package com.keykeeper.app.views.activity.testdrive;

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
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.keykeeper.app.databinding.ActivityTestDriveLayoutBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.CheckIfAnyTestDriveResponseBean;
import com.keykeeper.app.model.bean.EmployeeOwnedAssetsListResponse;
import com.keykeeper.app.model.bean.TestDriveResponseBean;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.home.HomeActivity;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

import java.util.ArrayList;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;

/**
 * Created by ankurrawal on 18/9/18.
 */
public class TestDriveStuckActivity extends BaseActivity implements DialogClickListener {

    private ActivityTestDriveLayoutBinding binding;
    private TestDriveStuckViewModel viewModel;
    private boolean isDriveStart;
    private Context context;
    private CheckIfAnyTestDriveResponseBean.Result checkIfAnyTestDriveResponseBean = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        isDriveStart = AppSharedPrefs.isTestDriveRunning();
        initializeViews();
    }

    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        if (isDriveStart) {
            customActionBar.setActionbar(getString(R.string.test_drive), false, false, false, false, this);
        } else {
            customActionBar.setActionbar(getString(R.string.test_drive), true, false, false, false, this);
        }
    }

    @Override
    public void initializeViews() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_drive_layout);
        viewModel = ViewModelProviders.of(this).get(TestDriveStuckViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.response_testdrive_stop.observe(this, responseTestDriveStop);
        viewModel.response_check_ifany_testdrive.observe(this, responseIfTestDriveRunning);
        viewModel.response_assets_owned.observe(this, responseAssetsOwnedCurrently);

        binding.tvTestDriveStop.setOnClickListener(this);

        viewModel.doCheckIfTestDriveIsRuning(AppSharedPrefs.getTestDriveId());
        viewModel.getCurrentAssetsOwned();
        registerReceiver(receiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        if (Utils.isGpsEnable(context)) {
            binding.gpsEnableTv.setVisibility(View.GONE);
        } else {
            binding.gpsEnableTv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_test_drive_stop:
                validateStartService();
                break;
        }

    }

    private void validateStartService() {

        if (Connectivity.isConnected()) {
            if (isDriveStart) {
                 setCustomActionBar();
                    Utils.showProgressDialog(context, getString(R.string.loading));
                    viewModel.doStopTestDrive(AppSharedPrefs.getEmployeeID(), Integer.valueOf(checkIfAnyTestDriveResponseBean.getAsset_id()), AppSharedPrefs.getLatitude(), AppSharedPrefs.getLongitude(), Utils.getCurrentTimeStampDate(), Utils.getCurrentUTCTimeStampDate(), AppSharedPrefs.getInstance(context).getTestDriveId());
            }
        } else {
            Utils.showAlert(context, "", getString(R.string.internet_connection), "ok", "", AppUtils.dialog_ok_click, TestDriveStuckActivity.this);
        }
    }

    /**
     * Test Drive Stop observer
     */
    Observer<CheckIfAnyTestDriveResponseBean> responseIfTestDriveRunning = new Observer<CheckIfAnyTestDriveResponseBean>() {
        @Override
        public void onChanged(@Nullable CheckIfAnyTestDriveResponseBean bean) {
            Utils.hideProgressDialog();
            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, TestDriveStuckActivity.this);
                return;
            } else if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                isDriveStart = true;
                checkIfAnyTestDriveResponseBean = bean.getResult();
                AppSharedPrefs.setTestDriveAssetId(checkIfAnyTestDriveResponseBean.getAsset_id());
                setViewForRunningTestDrive();
                AppSharedPrefs.setTestDriveRunning(true);
                Utils.startLocationStorage(context);
            } else {
                Utils.stopLocationStorage(context);
                AppSharedPrefs.setTestDriveRunning(false);
                AppSharedPrefs.getInstance(context).setTestDriveID("");
                startActivity(new Intent(context, HomeActivity.class));
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


    private void setViewForRunningTestDrive() {
        binding.tvKeyName.setText(checkIfAnyTestDriveResponseBean.getAsset_name());
        binding.tvStartedDatetime.setText("Start time: " + checkIfAnyTestDriveResponseBean.getStart_date_time());
    }


    /**
     * Test Drive Stop observer
     */
    Observer<TestDriveResponseBean> responseTestDriveStop = new Observer<TestDriveResponseBean>() {
        @Override
        public void onChanged(@Nullable TestDriveResponseBean bean) {
            Utils.hideProgressDialog();
            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, TestDriveStuckActivity.this);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                AppSharedPrefs.getInstance(context).setTestDriveRunning(false);
                isDriveStart = false;
                AppSharedPrefs.getInstance(context).setTestDriveID("");
                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                viewModel.getCurrentAssetsOwned();
            }
        }
    };


    @Override
    public void onBackPressed() {
        if (isDriveStart) {
            return;
        } else {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.doCheckIfTestDriveIsRuning(AppSharedPrefs.getInstance(context).getTestDriveId());
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
                    Utils.startLocationStorage(context);
                } else {
                    Utils.stopLocationStorage(context);
                }
            }
        }
    };

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
        }
    }




    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (Utils.isGpsEnable(context)) {
                binding.gpsEnableTv.setVisibility(View.GONE);
            } else {
                binding.gpsEnableTv.setVisibility(View.VISIBLE);
            }
        }
    };


}
