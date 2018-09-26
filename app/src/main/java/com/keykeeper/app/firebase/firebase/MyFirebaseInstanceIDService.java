package com.keykeeper.app.firebase.firebase;

import android.text.TextUtils;

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