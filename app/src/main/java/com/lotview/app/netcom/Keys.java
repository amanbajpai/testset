package com.lotview.app.netcom;

/**
 * Created by ankurrawal on 22/8/18.
 */
public interface Keys {

    String API_KEY = "cdnsol";
    String TYPE_ANDROID = "android";
    String TOKEN_TYPE = "access";


    /**
     * common param
     */
    String TAG_API_KEY = "api_key";
    String TAG_DEVICE_TYPE = "device_type";
    String TAG_DEVICE_ID = "device_id";
    String TAG_DEVICE_TOKEN = "device_token";



    /*API keys Below*/

    /**
     * Login
     */
    String EMAIL = "email";
    String PASSWORD = "password";


    String EMPLOYEE_ID = "employee_id";
    String OLD_PASSWORD = "old_password";
    String NEW_PASSWORD = "new_password";
    String CONFIRM_NEW_PASSWORD = "confirm_new_password";
    String IS_MY_ASSETS = "is_my_assets";
    String TEXT_TO_SEARCH = "search_key";

    /**
     * qr code
     */
    String QR_CODE_NUMBER = "qr_code_number";

    /*Notification Type*/
    int NOTIFICATION_ASSET_REQUEST_APPROVE = 2;
    int NOTIFICATION_ASSET_TRANSFER_REQUEST = 4;
    int NOTIFICATION_ASSET_TRANSFER_APPROVE = 5;
    int NOTIFICATION_ASSET_TRANSFER_DECLINE = 6;
    int NOTIFICATION_ASSET_SUBMIT_APPROVE = 8;
    int NOTIFICATION_CHAT_PUSH = 12;

    /**
     * approved request
     */
    String REQ_ID = "request_id";
    String SUBMIT_USER_TYPE = "submit_user_type";
    String NOTIFICATION_DATA = "notification_data";

    /*Latitude  &  Longitude*/
    String EMPLOYEE_LATITUDE = "emp_lat";
    String EMPLOYEE_LONGITUDE = "emp_long";

    String CHANNEL_NAME = "channel_name";

    String LOCAL_DATE_TIME = "local_date_time";
    String UTC_DATE_TIME = "utc_date_time";
    String SPEED = "speed";
    String ASSET_EMPOLOYEE_TEST_DRIVE_ID = "asset_employee_test_drive_id";

    /**
     * setting
     */
    String STATUS = "status";

    /*TestDrive*/


    String ASSET_ID = "asset_id";
    String TEST_DRIVE_START_LATITUDE = "start_latitude";
    String TEST_DRIVE_START_LONGITUDE = "start_longitude";
    String TEST_DRIVE_START_DATETIME = "start_date_time";
    String TEST_DRIVE_END_DATETIME = "end_date_time";
    String TEST_DRIVE_END_LATITUDE = "end_latitude";
    String TEST_DRIVE_END_LONGITUDE = "end_longitude";
    String TEST_DRIVE_START_DATETIME_UTC = "start_date_time_utc";
    String TEST_DRIVE_END_DATETIME_UTC = "end_date_time_utc";
    String LAST_ASSET_TRANSACTION_LOG_ID = "last_asset_transaction_log_id";
    String LAST_NOTIFICATION_ID = "last_notification_id";


    String ASSET_NAME = "asset_name";

}
