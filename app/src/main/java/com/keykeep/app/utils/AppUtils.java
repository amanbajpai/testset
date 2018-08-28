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

    String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] STORAGE_CAMERA_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};






    /**
     * login keys
     */
    public static int empty_id =101;
    public static int invalid_mail =102;
    public static int empty_password =103;

    int REQUEST_CODE_QR_SCAN = 104;
    int REQUEST_CODE_CAMERA = 105;
    int SERVER_ERROR = 106;


    /**
     * dialog keys
     */
    public static int dialogOkClick=501;

}
