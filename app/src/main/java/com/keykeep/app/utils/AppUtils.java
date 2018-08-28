package com.keykeep.app.utils;

import android.Manifest;
import android.content.pm.PackageManager;

/**
 * Created by ankurrawal on 22/8/18.
 */

public interface AppUtils {

    String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] STORAGE_CAMERA_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    String SCAN_SUCCESS = "scan_success";
    String SCAN_FAIL = "scan_fail";

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

    /**
     * dialog keys
     */
    public static int dialogOkClick = 301;


    /**
     * api status code
     */
    String STATUS_FAIL = "0";
    String STATUS_SUCCESS = "1";


}
