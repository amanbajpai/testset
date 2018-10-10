package com.keykeeper.app.receiver;

/**
 * Created by ankurrawal on 4/10/18.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.services.LocationMonitoringService;
import com.keykeeper.app.views.services.TestLocationListenerService;


public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            try {
                if (AppSharedPrefs.getInstance(context).isToTrackLocation()) {
                    if (!Utils.isMyServiceRunning(context, TestLocationListenerService.class)) {
                        Intent pushIntent = new Intent(context, TestLocationListenerService.class);
                        context.startService(pushIntent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}