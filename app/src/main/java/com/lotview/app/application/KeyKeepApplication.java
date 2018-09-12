package com.lotview.app.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.lotview.app.BuildConfig;
import com.lotview.app.model.bean.BaseRequestEntity;
import com.lotview.app.model.location.DaoMaster;
import com.lotview.app.model.location.DaoSession;
import com.lotview.app.netcom.Keys;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.Utils;

import org.greenrobot.greendao.database.Database;

import java.lang.reflect.Method;

import io.fabric.sdk.android.Fabric;


/**
 * Created by ankurrawal on 22/8/18.
 */

public class KeyKeepApplication extends MultiDexApplication {
    private static boolean activityVisible;

    static KeyKeepApplication instance;

    private DaoSession daoSession;

    private final String TAG = "KeyKeepApplication";
    public static int NOTIFICATION_ID = 1501;

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
        Fabric.with(this, new Crashlytics());
        try {

            instance = this;
            enableStricMode();
            instantiateFabric();

            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dining_inn_guest-db");
            Database db = helper.getWritableDb();

            daoSession = new DaoMaster(db).newSession();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void instantiateFabric() {
        try {
            if (!BuildConfig.DEBUG) {
                Fabric.with(this, new Crashlytics());
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
        baseRequestEntity.setDevice_type(Keys.TYPE_ANDROID);

        //put firebase app token here from preferences
        if (AppSharedPrefs.getInstance(instance).getPushDeviceToken() != null && AppSharedPrefs.getInstance(instance).getPushDeviceToken().trim().length() > 0) {
            baseRequestEntity.setDevice_token(AppSharedPrefs.getInstance(instance).getPushDeviceToken());
        } else {
            baseRequestEntity.setDevice_token("aaaaaaa");
        }
        if (includeToken) {
            baseRequestEntity.setToken_type(Keys.TOKEN_TYPE);
            baseRequestEntity.setAccess_token(AppSharedPrefs.getInstance(instance).getAccessToken());
        }
        return baseRequestEntity;
    }

}
