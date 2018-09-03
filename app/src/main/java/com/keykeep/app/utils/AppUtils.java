package com.keykeep.app.utils;

import android.Manifest;

/**
 * Created by ankurrawal on 22/8/18.
 */
public interface AppUtils {

    String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] STORAGE_CAMERA_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    String SCANED_QR_CODE = "scan_qr_code";
    String SCAN_SUCCESS = "scan_success";
    String SCAN_FAIL = "scan_fail";
    String ASSET_STATUS_CODE = "asset_status_code";
    String ASSET_REQUEST_ID = "asset_req_id";

    /**
     * login keys
     */
    int empty_id = 101;
    int invalid_mail = 102;
    int empty_password = 103;
    int empty_old_password = 104;
    int empty_confirm_password = 105;
    int match_confirm_password = 106;

    /**
     * Intent request code
     */
    int REQUEST_CODE_QR_SCAN = 201;
    int REQUEST_CODE_CAMERA = 202;
    int SERVER_ERROR = 203;
    int NO_INTERNET = 204;
    int STATUS_SCAN_CODE = 205;
    int STATUS_ASSET_LIST = 206;
    int STATUS_ASSET_SEND_REQUEST = 207;
    int STATUS_ASSET_RECEIVE_REQUEST = 208;
    /**
     * dialog keys
     */
    int dialog_ok_click = 301;
    int dialog_request_succes = 301;
    int dialog_ok_to_finish = 302;
    int dialog_yes_no_to_finish_app = 303;
    int dialog_logout_app = 304;


    /**
     * api status code
     */
    String STATUS_FAIL = "0";
    String STATUS_SUCCESS = "1";


    int STATUS_TRANSFER_ASSET_LIST = 209;
     /*Check AssetList Status*/

    String IS_ASSET_LIST_AVAILABLE = "isAssetListAvailable";
    String ASSET_AVAILABLE_STATUS = "assetAvailableStatus";


}
