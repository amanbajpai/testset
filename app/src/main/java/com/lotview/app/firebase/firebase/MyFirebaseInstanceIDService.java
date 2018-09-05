package com.lotview.app.firebase.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.Utils;

/**
 * Created by ankurrawal
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            Utils.showLog(TAG, "Refreshed token: " + refreshedToken);
            try {
                AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).setPushDeviceToken(refreshedToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}