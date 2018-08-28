package com.keykeep.app.netcom.retrofit;

import android.os.Build;

import com.keykeep.app.BuildConfig;
import com.keykeep.app.model.bean.LoginBean;
import com.keykeep.app.netcom.Keys;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ankurrawal on 22/8/18.
 */

public interface KeyKeepAPI {


    @POST(Config.LOGIN_URL)
    public Call<LoginBean> doLogin(@Field(Keys.EMAIL) String email,
                                   @Field(Keys.PASSWORD) String password
    );

}
