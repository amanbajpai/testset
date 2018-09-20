package com.lotview.app.views.fragment.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lotview.app.R;
import com.lotview.app.interfaces.DialogClickListener;
import com.lotview.app.model.bean.EmployeeOwnedAssetsListResponse;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.qrcodescanner.QrCodeActivity;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.AssetListActivity;
import com.lotview.app.views.activity.chat.ChatActivity;
import com.lotview.app.views.activity.history.HistoryActivity;
import com.lotview.app.views.activity.transfer.TransferActivity;
import com.lotview.app.views.base.BaseFragment;
import com.lotview.app.views.fragment.testDrive.TestDriveAssetDetailFragment;
import com.lotview.app.views.services.LocationListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements DialogClickListener {

    private Context context;
    private com.lotview.app.databinding.HomeFragmentLayoutBinding binding;
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
                    startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
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
                    startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN_FOR_DRIVE);
                } else {
                    requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA_FOR_DRIVE);
                }
                break;

            case R.id.chat_rl:
                if (!TextUtils.isEmpty(AppSharedPrefs.getInstance(context).getChatUrl())) {
                    startActivity(new Intent(context, ChatActivity.class));
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtils.REQUEST_CODE_CAMERA) {
            if (Utils.onRequestPermissionsResult(permissions, grantResults)) {
                startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
            } else {
                Utils.showSnackBar(binding, getString(R.string.allow_camera_permission));
            }
        } else if (requestCode == AppUtils.REQUEST_CODE_CAMERA_FOR_DRIVE) {
            if (Utils.onRequestPermissionsResult(permissions, grantResults)) {
                startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN_FOR_DRIVE);
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
                Intent intent = new Intent(context, TestDriveAssetDetailFragment.class);
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.SCAN_SUCCESS);
                intent.putExtra(AppUtils.SCANED_QR_CODE, qr_tag_number);
                startActivity(intent);

            } else {
                String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    result = jsonObject.getString("qr_code_number");
                    Intent intent = new Intent(context, TestDriveAssetDetailFragment.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
                    intent.putExtra(AppUtils.SCANED_QR_CODE, result);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showSnackBar(binding, getString(R.string.invalid_qr_code));
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
                Intent intent = new Intent(context, TestDriveAssetDetailFragment.class);
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.SCAN_SUCCESS);
                intent.putExtra(AppUtils.SCANED_QR_CODE, qr_tag_number);
                startActivity(intent);

            } else {
                String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    result = jsonObject.getString("qr_code_number");
                    Intent intent = new Intent(context, TestDriveAssetDetailFragment.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
                    intent.putExtra(AppUtils.SCANED_QR_CODE, result);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showSnackBar(binding, getString(R.string.invalid_qr_code));
                }
            }

        }

    }


    @Override
    public void onDialogClick(int which, int requestCode) {

    }

    Observer<EmployeeOwnedAssetsListResponse> responseAssetsOwnedCurrently = new Observer<EmployeeOwnedAssetsListResponse>() {

        @Override
        public void onChanged(@Nullable EmployeeOwnedAssetsListResponse employeeOwnedAssetsListResponse) {

            Utils.hideProgressDialog();
            if (employeeOwnedAssetsListResponse != null && employeeOwnedAssetsListResponse.getResults() != null && employeeOwnedAssetsListResponse.getResults().size() > 0) {
                ArrayList<EmployeeOwnedAssetsListResponse.Result> resultArrayList = employeeOwnedAssetsListResponse.getResults();
                if (resultArrayList.size() > 0) {
                    startLocationStorage();
                    AppSharedPrefs.getInstance(context).setTestDriveRunning(true);
                } else {
                    AppSharedPrefs.getInstance(context).setTestDriveRunning(false);
                }
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


    private void startLocationStorage() {
        Intent serviceIntent = new Intent(context, LocationListenerService.class);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(serviceIntent);
            //((HomeActivity)context).finish();
        } else {
            getActivity().startService(serviceIntent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.showProgressDialog(context, getString(R.string.loading));
        viewModel.getCurrentAssetsOwned();
    }
}