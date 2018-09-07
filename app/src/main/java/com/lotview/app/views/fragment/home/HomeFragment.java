package com.lotview.app.views.fragment.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lotview.app.R;
import com.lotview.app.databinding.HomeFragmentLayoutBinding;
import com.lotview.app.interfaces.DialogClickListener;
import com.lotview.app.qrcodescanner.QrCodeActivity;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.AssetListActivity;
import com.lotview.app.views.activity.assetDetail.AssetDetailActivity;
import com.lotview.app.views.activity.transfer.TransferActivity;
import com.lotview.app.views.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

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
//        viewModel.validator.observe(this, observer);
//        viewModel.respose_validator.observe(this, response_observer);

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
                break;

            case R.id.hand_over_rl:
                startActivity(new Intent(context, TransferActivity.class));
                break;

            case R.id.takeout_rl:
                break;

            case R.id.chat_rl:
                break;
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

                Intent intent = new Intent(context, AssetDetailActivity.class);
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
                intent.putExtra(AppUtils.SCANED_QR_CODE, result);
                startActivity(intent);

            } else {
                String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    result = jsonObject.getString("qr_code_number");
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_SCANED_CODE);
                    intent.putExtra(AppUtils.SCANED_QR_CODE, result);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showSnackBar(binding, getString(R.string.unable_to_scan_qr));
                }
            }

        }

    }


    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}