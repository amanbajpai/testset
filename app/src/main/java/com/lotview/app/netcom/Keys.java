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



    /**
     * setting
     */
    String STATUS = "status";

}
