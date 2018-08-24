package com.keykeep.app.utils;

import android.Manifest;
import android.content.pm.PackageManager;

/**
 * Created by ankurrawal on 22/8/18.
 */

public interface AppUtils {

    String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    String SCAN_SUCCESS = "scan_success";
    String SCAN_FAIL = "scan_fail";




    int REQUEST_CODE_QR_SCAN = 1010;
    int REQUEST_CODE_CAMERA = 1011;


}
