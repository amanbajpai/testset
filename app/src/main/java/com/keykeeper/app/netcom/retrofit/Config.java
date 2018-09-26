package com.keykeeper.app.netcom.retrofit;

import com.keykeeper.app.BuildConfig;

/**
 * Created by akshaydashore on 28/8/18
 */

public interface Config {

    public static final String BASE_URL = BuildConfig.BASE_URL;
    // public static final String BASE_URL = "http://192.168.0.54/keykeep/assetapi/public/v1/";

    /**
     * Login
     */
    String LOGIN_URL = "employee/login";
    String FORGOT_PASSWORD_URL = "employee/forgotpassword";
    String ASSET_LIST_URL = "employee/assetslist";
    String CHANGE_PASSWORD_URL = "employee/changepassword";
    String ASSET_DETAIL = "employee/assetdetail";
    //    String KEEP_ASSET_REQUEST ="employee/assetkeeprequest"; // Old Api when reques t need to be approved from the admin side.
    String KEEP_ASSET_REQUEST = "employee/assetdirectapprove"; // Updated API for the same when request is directly approved by admin
    String ASSET_HANDOVER_REQUEST = "employee/assethandoverrequest";
    String ASSET_TRANSFER_REQUEST = "employee/assettransferrequest";

    String ASSET_PENDING_SEND_REQUEST = "employee/pendingsendrequest";
    String ASSET_PENDING_RECIEVE_REQUEST = "employee/pendingreceiverequest";

    String CANCEL_ASSET_REQ_URL = "employee/assetrequestdecline";
    String APPROVE_ASSET_REQ_URL = "employee/assetrequestapprove";
    String NOTIFICATIONS_REQ_URL = "employee/notificationlist";
    String LOGOUT_REQ_URL = "employee/logout";
    String NOTIFICATION_ARCHICVE = "employee/notificationarchive";
    String NOTIFICATION_ENABLE_URL = "employee/notificationenable";
    String START_TEST_DRIVE_URL = "employee/employeetestdrivestart";
    String END_TEST_DRIVE_URL = "employee/employeetestdriveend";

    String EMPLOYEE_TRACKER_URL = "employee/employeeassettrack";
    String EMPLOYEE_ASSET_URL = "employee/myassetslist";
    String ASSET_CURRENT_LOCATION_URL = "employee/assetcurrentlocation";
    String CHECK_RUNNING_TESTDRIVE_URL = "employee/checkrunningtestdrive";
    String HISTORY_LIST_URL = "employee/assethistory";


}

