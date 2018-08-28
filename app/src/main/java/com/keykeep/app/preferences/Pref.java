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
    public static final String USER_PASSWORD = "user_password";


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


    public static void setAccessToken(Context context, String value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(ACCESS_TOKEN, value);
        editor.apply();
    }

    public static String getAccessToken(Context context){
        return getSharedPrefs(context).getString(ACCESS_TOKEN, "");
    }

    public static String getUserName(Context context){
        return getSharedPrefs(context).getString(USER_NAME, null);
    }

    public static void setUserDetail(Context context, String user_detail) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(User_DETAIL, user_detail);
        editor.apply();
    }

    public static String getUserDetail(Context context){
        return getSharedPrefs(context).getString(User_DETAIL, null);
    }

    public static void setPassword(Context context, String password) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(USER_PASSWORD, password);
        editor.apply();
    }

    public static String getUserPassword(Context context){
        return getSharedPrefs(context).getString(USER_PASSWORD, null);
    }



}
