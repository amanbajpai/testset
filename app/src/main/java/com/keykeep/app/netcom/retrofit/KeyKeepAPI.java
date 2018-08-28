package com.keykeep.app.netcom.retrofit;

import com.keykeep.app.model.bean.BaseRequestEntity;
import com.keykeep.app.model.bean.ForgotPasswordResponseBean;
import com.keykeep.app.model.bean.LoginBean;
import com.keykeep.app.netcom.Keys;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ankurrawal on 22/8/18.
 */

public interface KeyKeepAPI {

    @POST(Config.LOGIN_URL)
    public Call<LoginBean> doLogin(@Body BaseRequestEntity baseRequestEntity, @Query(Keys.EMAIL) String email,
                                   @Query(Keys.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Config.FORGOT_PASSWORD_URL)
    public Call<ForgotPasswordResponseBean> forgotPassword(@Field(Keys.EMAIL) String email);


}
