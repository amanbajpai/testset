package com.keykeep.app.netcom.retrofit;

import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.model.bean.BaseRequestEntity;
import com.keykeep.app.model.bean.ChangePasswordBean;
import com.keykeep.app.model.bean.ForgotPasswordResponseBean;
import com.keykeep.app.model.bean.LoginBean;
import com.keykeep.app.netcom.Keys;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ankurrawal on 22/8/18.
 */

public interface KeyKeepAPI {

    @POST(Config.LOGIN_URL)
    public Call<LoginBean> doLogin(@Body BaseRequestEntity baseRequestEntity, @Query(Keys.EMAIL) String email,
                                   @Query(Keys.PASSWORD) String password);

    @POST(Config.FORGOT_PASSWORD_URL)
    public Call<ForgotPasswordResponseBean> forgotPassword(@Body BaseRequestEntity baseRequestEntity, @Query(Keys.EMAIL) String email);

    @POST(Config.ASSET_LIST_URL)
    public Call<AssetsListResponseBean> getAssetsList(@Body BaseRequestEntity baseRequestEntity, @Query(Keys.EMPLOYEE_ID) String email, @Query(Keys.IS_MY_ASSETS) String isMyAsset);

     @POST(Config.CHANGE_PASSWORD_URL)
    Call<ChangePasswordBean> doChangePassword(
            @Body BaseRequestEntity baseRequestEntity,
            @Query(Keys.OLDPASWSWORD) String oldPassword,
            @Query(Keys.NEW_PASSWORD) String newPassword,
            @Query(Keys.CONFIRM_NEW_PASSWORD) String confirmPassword,
            @Query(Keys.EMPLOYEE_ID) String employeeId
    );
}
