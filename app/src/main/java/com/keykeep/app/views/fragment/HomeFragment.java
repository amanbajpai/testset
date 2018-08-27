package com.keykeep.app.views.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.keykeep.app.R;
import com.keykeep.app.qrcodescanner.QrCodeActivity;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.activity.AssetListActivity;
import com.keykeep.app.views.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    View rootView;
    private Context context;
    private RelativeLayout asset_rl, scan_rl, history_rl, hand_over_rl, takeout_rl, chat_rl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        initializeViews(rootView);
        return rootView;
    }

    @Override
    public void initializeViews(View rootView) {
        context = getActivity();

        asset_rl = rootView.findViewById(R.id.asset_rl);
        scan_rl = rootView.findViewById(R.id.scan_rl);
        history_rl = rootView.findViewById(R.id.history_rl);
        hand_over_rl = rootView.findViewById(R.id.hand_over_rl);
        takeout_rl = rootView.findViewById(R.id.takeout_rl);
        chat_rl = rootView.findViewById(R.id.chat_rl);

        asset_rl.setOnClickListener(this);
        scan_rl.setOnClickListener(this);
        history_rl.setOnClickListener(this);
        hand_over_rl.setOnClickListener(this);
        takeout_rl.setOnClickListener(this);
        chat_rl.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.asset_rl:
                startActivity(new Intent(context, AssetListActivity.class));
                break;

            case R.id.scan_rl:
                if (Utils.checkPermissions(getActivity(), AppUtils.CAMERA_PERMISSION)) {
                    startActivityForResult(new Intent(context, QrCodeActivity.class), AppUtils.REQUEST_CODE_QR_SCAN);
                } else {
                    requestPermissions(AppUtils.STORAGE_CAMERA_PERMISSIONS, AppUtils.REQUEST_CODE_CAMERA);
                }
                break;

            case R.id.history_rl:
                break;

            case R.id.hand_over_rl:
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
            Utils.showToast(context, "Please allow Camera permission to scan qr code");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra(AppUtils.SCAN_FAIL);
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == AppUtils.REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra(AppUtils.SCAN_SUCCESS);
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }

    }


}