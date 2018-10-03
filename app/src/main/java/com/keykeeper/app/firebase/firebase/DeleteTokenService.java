package com.keykeeper.app.firebase.firebase;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.LogUtils;


/**
 * Created by ankurrawal
 */

public class DeleteTokenService extends IntentService {

    public static final String TAG = DeleteTokenService.class.getSimpleName();

    public DeleteTokenService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            // Check for current token
            String originalToken = getTokenFromPrefs();
            LogUtils.d(TAG, "Token before deletion: " + originalToken);
            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();
            // Clear current saved token
            saveTokenToPrefs("");
            // Check for success of empty token
            String tokenCheck = getTokenFromPrefs();
            LogUtils.d(TAG, "Token deleted. Proof: " + tokenCheck);
            // Now manually call onTokenRefresh()
            LogUtils.d(TAG, "Getting new token");
            FirebaseInstanceId.getInstance().getToken();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void saveTokenToPrefs(String _token) {

        AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).setPushDeviceToken(_token);

    }

    private String getTokenFromPrefs() {

        String deviceToken = AppSharedPrefs.getInstance(KeyKeepApplication.getInstance()).getPushDeviceToken();
        return deviceToken;
    }
}