package com.keykeep.app.netcom.retrofit;

import android.os.Build;

import com.keykeep.app.BuildConfig;
import com.keykeep.app.model.bean.LoginBean;
import com.keykeep.app.netcom.Keys;
import com.keykeep.app.utils.Utils;

import java.security.Key;

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
    Call<LoginBean> doLogin(@Query(Keys.EMAIL) String email,
                                   @Query(Keys.PASSWORD) String password,
                                   @Query(Keys.TAG_API_KEY) String apiKey,
                                   @Query(Keys.TAG_DEVICE_ID) String device_id,
                                   @Query(Keys.TAG_DEVICE_TOKEN) String token,
                                   @Query(Keys.TAG_DEVICE_TYPE) String device_type
    );

}
