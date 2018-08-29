package com.keykeep.app.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by akshaydashore on 28/8/18
 */

public class Pref {

    private static final String SHARED_PREFS_NAME = "key_keep_pref";
    private static final String USER_NAME = "user_name";
    private static final String ACCESS_TOKEN = "Access_token";
    private static final String User_DETAIL = "user_detail";
    private static final String EMPLOYEE_ID = "employee_id";
    private static final String LOGIN_PASSWORD = "login_password";

    public static final String REMEMBER_ME = "remember_me";
    public static final String IS_LOGIN = "is_login";


    /**
     * This method returns instance of shared preferences.
     * @param context
     * @return
     */
    private static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }


    /**
     * This method returns instance of shared preferences editor.
     * @param context
     * @return
     */
    private static SharedPreferences.Editor getEditor(Context context){
        return getSharedPrefs(context).edit();
    }


    /**
     * access token
     * @param context
     * @param value
     */
    public static void setAccessToken(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(ACCESS_TOKEN, value);
        editor.apply();
    }
    public static String getAccessToken(Context context){
        return getSharedPrefs(context).getString(ACCESS_TOKEN, "");
    }


    /**
     * user name
     * @param context
     * @param value
     */
    public static void setUserName(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(USER_NAME, value);
        editor.apply();
    }
    public static String getUserName(Context context){
        return getSharedPrefs(context).getString(USER_NAME, null);
    }

    /**
     * user detail
     * @param context
     * @param user_detail
     */
    public static void setUserDetail(Context context, String user_detail) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(User_DETAIL, user_detail);
        editor.apply();
    }
    public static String getUserDetail(Context context){
        return getSharedPrefs(context).getString(User_DETAIL, null);
    }


    /**
     * employee id
     * @param context
     * @param value
     */
    public static void setEmployeeID(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(EMPLOYEE_ID, value);
        editor.apply();
    }
    public static String getEmployeeID(Context context){
        return getSharedPrefs(context).getString(EMPLOYEE_ID, "");
    }

    /**
     * password
     * @param context
     * @param value
     */
    public static void setPassword(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(LOGIN_PASSWORD, value);
        editor.apply();
    }
    public static String getPassword(Context context){
        return getSharedPrefs(context).getString(LOGIN_PASSWORD, "");
    }


    public static void setBoolean(Context context, boolean value,String key) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context,String key){
        return getSharedPrefs(context).getBoolean(key, false);
    }



}
