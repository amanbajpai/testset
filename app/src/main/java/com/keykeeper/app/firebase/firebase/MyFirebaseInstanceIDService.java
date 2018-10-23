package com.keykeeper.app.firebase.firebase;

import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.Utils;

/**
 * Created by ankurrawal
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    String refreshedToken = "";

    //FOR TESTING
//    int trackLocationInterval = 30000;
//
//
//    static Handler handler = new Handler();
//    private Runnable periodicUpdate = new Runnable() {
//        @Override
//        public void run() {
//            handler.postDelayed(periodicUpdate, trackLocationInterval - SystemClock.elapsedRealtime() % 1000);
//            Log.e("FirebaseMessaging", "In the service");
//        }
//    };
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        handler.post(periodicUpdate);
//    }

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            Utils.showLog(TAG, "PushToken: " + refreshedToken);
            if (!TextUtils.isEmpty(refreshedToken)) {
                try {
                    AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).setPushDeviceToken(refreshedToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                refreshedToken = FirebaseInstanceId.getInstance().getToken();
            }

            try {
                AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).setPushDeviceToken(refreshedToken);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}