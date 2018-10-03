package com.keykeeper.app.application;

import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.facebook.stetho.Stetho;
import com.keykeeper.app.BuildConfig;
import com.keykeeper.app.job.LocationSyncUploadJob;
import com.keykeeper.app.job.LocationUploadJobCreator;
import com.keykeeper.app.model.bean.BaseRequestEntity;
import com.keykeeper.app.model.location.DaoMaster;
import com.keykeeper.app.model.location.DaoSession;
import com.keykeeper.app.netcom.Keys;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.Utils;

import org.greenrobot.greendao.database.Database;

import java.lang.reflect.Method;
import java.util.Set;

import io.fabric.sdk.android.Fabric;


/**
 * Created by ankurrawal on 22/8/18.
 */

public class KeyKeepApplication extends MultiDexApplication {
    private static boolean activityVisible;

    static KeyKeepApplication instance;
    public static int mLastJobId;
    public static JobManager mJobManager;

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
            // enableStricMode();
            instantiateFabric();

            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lotview_db");
            Database db = helper.getWritableDb();

            daoSession = new DaoMaster(db).newSession();

            JobManager.create(this).addJobCreator(new LocationUploadJobCreator());


            /**
             * init retrofit client to call network services
             */
            RetrofitHolder retrofitHolder = new RetrofitHolder(instance);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        if (BuildConfig.DEBUG) {
        Stetho.initializeWithDefaults(this);
//        }

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void instantiateFabric() {
        try {
            //  if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
            //  }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopBatchService() {
        // Stop Job here
        if (KeyKeepApplication.mJobManager != null)
            KeyKeepApplication.mJobManager.cancel(KeyKeepApplication.mLastJobId);
        // stopService(new Intent(this, BatchTaskService.class));
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

            if (BuildConfig.DEBUG) {
                StrictMode.setThreadPolicy(
                        new StrictMode.ThreadPolicy.Builder()
                                .detectAll()
                                .penaltyLog()
                                .penaltyDeath()
                                .build());

                StrictMode.setVmPolicy(
                        new StrictMode.VmPolicy.Builder()
                                .detectAll()
                                .penaltyLog()
                                .penaltyDeath()
                                .build());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static BaseRequestEntity getBaseEntity(boolean includeToken) {
        BaseRequestEntity baseRequestEntity = new BaseRequestEntity();

        baseRequestEntity.setApi_key(Keys.API_KEY);
        baseRequestEntity.setDevice_id(Utils.getDeviceID());
        baseRequestEntity.setDevice_type(Keys.TYPE_ANDROID);

        if (AppSharedPrefs.getInstance(instance).getEmployeeID() != null && AppSharedPrefs.getInstance(instance).getEmployeeID().trim().length() > 0) {
            baseRequestEntity.setEmployee_id(Integer.valueOf(AppSharedPrefs.getInstance(instance).getEmployeeID()));
        } else {
            baseRequestEntity.setEmployee_id(0);
        }

        if (AppSharedPrefs.getInstance(instance).getCompanyID() != null && AppSharedPrefs.getInstance(instance).getCompanyID().trim().length() > 0) {
            baseRequestEntity.setCompany_id(Integer.valueOf(AppSharedPrefs.getInstance(instance).getCompanyID()));
        } else {
            baseRequestEntity.setCompany_id(0);
        }

        //put firebase app token here from preferences
        if (AppSharedPrefs.getInstance(instance).getPushDeviceToken() != null && AppSharedPrefs.getInstance(instance).getPushDeviceToken().trim().length() > 0) {
            baseRequestEntity.setDevice_token(AppSharedPrefs.getInstance(instance).getPushDeviceToken());
        } else {
            baseRequestEntity.setDevice_token("");
        }
        if (includeToken) {
            baseRequestEntity.setToken_type(Keys.TOKEN_TYPE);
            baseRequestEntity.setAccess_token(AppSharedPrefs.getInstance(instance).getAccessToken());
        }
        return baseRequestEntity;
    }

//    public boolean checkJobFinished() {
//        boolean isFinished = false;
//        Set<Job> allJobsForTag = JobManager.instance().getAllJobsForTag(LocationSyncUploadJob.TAG);
//        for (Job job : allJobsForTag) {
//            if (!job.isFinished()) {
//                isFinished = false;
//                Utils.showLog(TAG, "checkJobFinished: " + false);
//                return false;
//
//            }
//        }
//        Utils.showLog(TAG, "checkJobFinished: " + true);
//        return true;
//    }


}
