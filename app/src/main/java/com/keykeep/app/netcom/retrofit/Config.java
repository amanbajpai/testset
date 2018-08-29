package com.keykeep.app.netcom.retrofit;

import com.keykeep.app.BuildConfig;

/**
 * Created by akshaydashore on 28/8/18
 */

public interface Config {

    public static final String BASE_URL = BuildConfig.BASE_URL;

    /**
     * Login
     */
    String LOGIN_URL = "login";
    String FORGOT_PASSWORD_URL = "forgotpassword";
    String ASSET_LIST_URL = "assetslist";
    String CHANGE_PASSWORD_URL = "changepassword";
    String ASSET_DETAIL = "assetdetail";
    String ASSET_REQUEST ="assetkeeprequest";

    String ASSET_PENDING_SEND_REQUEST="pendingsendrequest";
    String ASSET_PENDING_RECIEVE_REQUEST="pendingreceiverequest";

}

