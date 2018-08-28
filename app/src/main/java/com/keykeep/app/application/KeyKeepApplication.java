package com.keykeep.app.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.keykeep.app.BuildConfig;
import com.keykeep.app.model.bean.BaseRequestEntity;
import com.keykeep.app.netcom.Keys;
import com.keykeep.app.netcom.retrofit.RetrofitHolder;
import com.keykeep.app.preferences.Pref;
import com.keykeep.app.utils.Utils;

import java.lang.reflect.Method;

import retrofit2.Retrofit;


/**
 * Created by ankurrawal on 22/8/18.
 */

public class KeyKeepApplication extends MultiDexApplication {
    private static boolean activityVisible;

    static KeyKeepApplication instance;

    private final String TAG = "DiningInApplication";
    public static int NOTIFICATION_ID = 1501;
    private Retrofit retrofit;

    public static KeyKeepApplication getInstance() {
        if (instance == null)
            instance = new KeyKeepApplication();

        return instance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            instance = this;
            enableStricMode();
            instantiateFabric();


            /**
             * init retrofit client to call network services
             */
            RetrofitHolder retrofitHolder = new RetrofitHolder(instance);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void instantiateFabric() {
        try {
            if (!BuildConfig.DEBUG) {
//                Fabric.with(this, new Crashlytics());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void enableStricMode() {
        try {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    class LifeCycle implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Intent intent = activity.getIntent();
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }


        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    public static BaseRequestEntity getBaseEntity(boolean includeToken) {
        BaseRequestEntity baseRequestEntity = new BaseRequestEntity();
        baseRequestEntity.setApi_key(Keys.API_KEY);
        baseRequestEntity.setDevice_id(Utils.getDeviceID());
        baseRequestEntity.setDevice_token("dfsfsdfsdfsdf"); //put firebase app token here from preferences
        baseRequestEntity.setDevice_type(Keys.TYPE_ANDROID);
        if (includeToken) {
            baseRequestEntity.setToken_type(Keys.TOKEN_TYPE);
            baseRequestEntity.setAccess_token(Pref.getAccessToken(instance));
        }
        return baseRequestEntity;
    }

}
