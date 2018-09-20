package com.lotview.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by akshaydashore on 28/8/18
 */

public class AppSharedPrefs {

    private static final String SHARED_PREFS_NAME = "key_keep_pref";
    private static final String USER_NAME = "user_name";
    private static final String ACCESS_TOKEN = "Access_token";
    private static final String PUSH_DEVICE_TOKEN = "push_device_token";
    private static final String User_DETAIL = "user_detail";
    private static final String EMPLOYEE_ID = "employee_id";
    private static final String COMPANY_ID = "company_id";
    private static final String EMPLOYEE_NAME = "employee_name";
    private static final String LOGIN_PASSWORD = "login_password";

    public static final String REMEMBER_ME = "remember_me";
    public static final String IS_LOGIN = "is_login";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String SPEED = "speed";
    public static final String CHAT_URL = "chat_url";
    public static final String MY_OWN_KEY_IDS_URL = "my_key_ids";
    public static final String DRIVE_START = "Drive_Start";
    public static final String QR_CODE = "Qr_code";
    public static final String TEST_DRIVE_ID = "test_drive_id";
    public static final String ASSET_NAME_TEST_DRIVE = "asset_name_test_drive";

    private static Context mContext = null;
    public static AppSharedPrefs instance = null;
    static SharedPreferences sp;
    static SharedPreferences.Editor prefEditor = null;


    public static AppSharedPrefs getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new AppSharedPrefs();
        }
        sp = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        prefEditor = sp.edit();
        return instance;
    }

    public static void clearPref(){

        prefEditor.clear();
        prefEditor.commit();
        prefEditor.apply();
    }

    public static void setLogin(boolean login) {
        prefEditor.putBoolean(IS_LOGIN, login);
        prefEditor.commit();
    }

    public static boolean isLogin() {
        return sp.getBoolean(IS_LOGIN, false);
    }

    public static void setRememberMe(boolean remember) {
        prefEditor.putBoolean(REMEMBER_ME, remember);
        prefEditor.commit();
    }

    public static boolean getRememberMe() {
        return sp.getBoolean(REMEMBER_ME, false);
    }


    /**
     * access token
     *
     * @param value
     */
    public static void setAccessToken(String value) {
        prefEditor.putString(ACCESS_TOKEN, value);
        prefEditor.commit();
    }

    public static String getAccessToken() {
        return sp.getString(ACCESS_TOKEN, "");
    }


    /**
     * Push Device Token
     *
     * @param value
     */
    public static void setPushDeviceToken(String value) {
        prefEditor.putString(PUSH_DEVICE_TOKEN, value);
        prefEditor.commit();
    }

    public static String getPushDeviceToken() {
        return sp.getString(PUSH_DEVICE_TOKEN, "");
    }


    /**
     * user detail
     *
     * @param user_detail
     */
    public static void setUserDetail(String user_detail) {
        prefEditor.putString(User_DETAIL, user_detail);
        prefEditor.commit();
    }

    public static String getUserDetail() {
        return sp.getString(User_DETAIL, null);
    }


    /**
     * company id
     *
     * @param value
     */
    public static void setCompanyID(String value) {
        prefEditor.putString(COMPANY_ID, value);
        prefEditor.commit();
    }

    public static String getCompanyID() {
        return sp.getString(COMPANY_ID, "");
    }

    /**
     * employee id
     *
     * @param value
     */
    public static void setEmployeeID(String value) {
        prefEditor.putString(EMPLOYEE_ID, value);
        prefEditor.commit();
    }

    public static String getEmployeeID() {
        return sp.getString(EMPLOYEE_ID, "");
    }

    /**
     * employee id
     *
     * @param value
     */
    public static void setEmployeeName(String value) {
        prefEditor.putString(EMPLOYEE_NAME, value);
        prefEditor.commit();
    }

    public static String getEmployeeName() {
        return sp.getString(EMPLOYEE_NAME, "");
    }


    /**
     * password
     *
     * @param value
     */
    public static void setPassword(String value) {
        prefEditor.putString(LOGIN_PASSWORD, value);
        prefEditor.commit();
    }

    public static String getPassword() {
        return sp.getString(LOGIN_PASSWORD, "");
    }

    public static void setLatitude(String lat) {
        prefEditor.putString(LATITUDE, lat);
        prefEditor.commit();
    }

    public static String getLatitude() {
        return sp.getString(LATITUDE, "0");
    }


    public static void setLongitude(String lat) {
        prefEditor.putString(LONGITUDE, lat);
        prefEditor.commit();
    }

    public static String getLongitude() {
        return sp.getString(LONGITUDE, "0");
    }


    public static void setSpeed(String speed) {
        prefEditor.putString(SPEED, speed);
        prefEditor.commit();
    }

    public static String getSpeed() {
        return sp.getString(SPEED, "0");
    }


    public static void setOwnedKeyIds(String myKeys) {
        prefEditor.putString(MY_OWN_KEY_IDS_URL, myKeys);
        prefEditor.commit();
    }

    public static String getOwnedKeyIds() {
        return sp.getString(MY_OWN_KEY_IDS_URL, "");
    }

    /*Chat URL*/
    public static void setChatUrl(String chaturl) {
        prefEditor.putString(CHAT_URL, chaturl);
        prefEditor.commit();
    }

    public static String getChatUrl() {
        return sp.getString(CHAT_URL, "");
    }


    public static void setTestDriveRunning(boolean b) {
        prefEditor.putBoolean(DRIVE_START, b);
        prefEditor.commit();
    }

    public static boolean isTestDriveRunning() {
        return sp.getBoolean(DRIVE_START, false);
    }


    public static void setQrCode(String qr_code) {
        prefEditor.putString(QR_CODE, qr_code);
        prefEditor.commit();
    }

    public static String getQrCode() {
        return sp.getString(QR_CODE, "");
    }

    public static void setTestDriveID(String testDriveID) {
        prefEditor.putString(TEST_DRIVE_ID, testDriveID);
        prefEditor.commit();
    }

    public static String getTestDriveId() {
        return sp.getString(TEST_DRIVE_ID, "");
    }

    public static void setAssetNameforRunningTestDrive(String testDriveID) {
        prefEditor.putString(ASSET_NAME_TEST_DRIVE, testDriveID);
        prefEditor.commit();
    }

    public static String getAssetNameforRunningTestDrive() {
        return sp.getString(ASSET_NAME_TEST_DRIVE, "");
    }


}
