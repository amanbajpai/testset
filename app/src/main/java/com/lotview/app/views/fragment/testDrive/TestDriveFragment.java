package com.lotview.app.views.fragment.testDrive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lotview.app.R;
import com.lotview.app.databinding.FragmentTestDriveBinding;
import com.lotview.app.qrcodescanner.QrCodeActivity;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.assetDetail.AssetDetailActivity;
import com.lotview.app.views.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by akshaydashore on 15/9/18
 */

public class TestDriveFragment extends BaseFragment {


    private Context context;
    FragmentTestDriveBinding binding;

    @Override
    public void initializeViews(View rootView) {

    }

    @Override
    public void onClick(View view) {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        openQrCode();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void openQrCode() {
        if (Utils.checkPermissions(getActivity(), AppUtils.STORAGE_CAMERA_PERMISSIONS)) {
            startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
        } else {
            requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtils.REQUEST_CODE_CAMERA && Utils.onRequestPermissionsResult(permissions, grantResults)) {
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

}
