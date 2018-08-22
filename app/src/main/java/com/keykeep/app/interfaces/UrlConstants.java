package com.keykeep.app.interfaces;


import com.keykeep.app.BuildConfig;

/**
 * Created by vishalchhodwani on 18/8/17.
 */
public interface UrlConstants {


    String BASE_URL = BuildConfig.BASE_URL;

    String LOGIN_URL = BASE_URL + "socialLogin";


    int LOGIN_URL_REQUEST_CODE = 1;

}
