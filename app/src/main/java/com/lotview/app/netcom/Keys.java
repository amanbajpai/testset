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


    /**
     * change password
     */
    String EMPLOYEE_ID = "employee_id";
    String OLD_PASSWORD = "old_password";
    String NEW_PASSWORD = "new_password";
    String CONFIRM_NEW_PASSWORD = "confirm_new_password";
    String IS_MY_ASSETS = "is_my_assets";


    /**
     * qr code
     */
    String QR_CODE_NUMBER = "qr_code_number";


    /**
     * approved request
     */
    String REQ_ID = "request_id";
    String SUBMIT_USER_TYPE =  "submit_user_type";
}
