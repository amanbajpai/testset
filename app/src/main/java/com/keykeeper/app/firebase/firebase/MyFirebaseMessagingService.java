package com.keykeeper.app.firebase.firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.keykeeper.app.R;
import com.keykeeper.app.model.notification.PushAdditionalData;
import com.keykeeper.app.model.notification.PushData;
import com.keykeeper.app.netcom.Keys;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.home.HomeActivity;
import com.keykeeper.app.views.services.LocationMonitoringService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;

/*
 * Created by ankurrawal
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Map<String, String> params;
    public static int value = 1;
    String CHANNEL_ID = "push_cannel_id";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            if (remoteMessage.getData().size() > 0) {
                Utils.showLog(TAG, "Data payload==" + remoteMessage.getData());
            } else if (remoteMessage.getNotification() != null) {
                Utils.showLog(TAG, "Notification payload==" + remoteMessage.getNotification().getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PushData pushDatabean = new PushData();
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            String PushAdditionalDataJson = "";

            pushDatabean.setIcon(jsonObject.optString("icon"));
            pushDatabean.setSound(jsonObject.optString("sound"));
            pushDatabean.setTitle(jsonObject.optString("title"));
            pushDatabean.setBody(jsonObject.optString("body"));
            pushDatabean.setPushType(Integer.valueOf(jsonObject.optString("push_type")));

            try {
                PushAdditionalDataJson = jsonObject.optString("additional_data");
                JSONObject object = new JSONObject(PushAdditionalDataJson);

                int pushAssetId = object.optInt("asset_id");
                int pushEmployeeId = object.optInt("employee_id");
                int pushCompanyId = object.optInt("company_id");
                String chat_user_url = object.optString("chat_user_url");

                PushAdditionalData pushAdditionalDataBean = new PushAdditionalData();
                pushAdditionalDataBean.setAssetId(pushAssetId);
                pushAdditionalDataBean.setEmployeeId(pushEmployeeId);
                pushAdditionalDataBean.setCompanyId(pushCompanyId);
                pushAdditionalDataBean.setChatUserUrl(chat_user_url);

                pushDatabean.setAdditionalData(pushAdditionalDataBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (AppSharedPrefs.getInstance(getApplicationContext()).isLogin() && checkIfNotificationEnabled()) {
                showNotification(this, pushDatabean);
            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showNotification(Context context, PushData pushData) {


        /**
         * handle push for location service
         */
        if (pushData.getPushType() == 15) {
            if (Utils.isGpsEnable(context) && Utils.isLocationInHighMode(context)) {
                if (!Utils.isMyServiceRunning(context, LocationMonitoringService.class)) {
                    Utils.startLocationStorage(context);

                    return;
                }
            }
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Keys.NOTIFICATION_DATA, pushData);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        int count = AppSharedPrefs.getNotificationCount();
        count++;
        AppSharedPrefs.setNotificationCount(count);

        if (count > 0) {
            ShortcutBadger.applyCount(context, count);
        } else {
            ShortcutBadger.removeCount(context);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.ic_notification_icon);
            mBuilder.setColor(getResources().getColor(R.color.app_blue));
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        mBuilder.setContentTitle(pushData.getTitle());
        mBuilder.setContentText(pushData.getBody());
        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(pushData.getBody()));
        mBuilder.setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (!AppSharedPrefs.isTestDriveRunning()) {
            mBuilder.setContentIntent(resultPendingIntent);
        }

        Random random = new Random();
        int notificationId = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(notificationId, mBuilder.build());

    }


    @Override
    public void handleIntent(Intent intent) {
        try {
            if (AppSharedPrefs.getInstance(getApplicationContext()).isLogin()) {
                if (intent.getExtras() != null) {
                    RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                    for (String key : intent.getExtras().keySet()) {
                        builder.addData(key, intent.getExtras().get(key).toString());
                    }

                    onMessageReceived(builder.build());
                } else {
                    super.handleIntent(intent);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkIfNotificationEnabled() {
        boolean isNotificationEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled();
        return isNotificationEnabled;
    }

}