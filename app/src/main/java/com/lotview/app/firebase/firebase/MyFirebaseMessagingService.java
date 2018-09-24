package com.lotview.app.firebase.firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lotview.app.R;
import com.lotview.app.model.notification.PushAdditionalData;
import com.lotview.app.model.notification.PushData;
import com.lotview.app.netcom.Keys;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.home.HomeActivity;

import org.json.JSONObject;

import java.util.Map;

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

            PushAdditionalDataJson = jsonObject.optString("additional_data");
            JSONObject object = new JSONObject(PushAdditionalDataJson);
            int pushAssetId = object.optInt("asset_id");
            int pushEmployeeId = object.optInt("employee_id");
            int pushCompanyId = object.optInt("company_id");

            PushAdditionalData pushAdditionalDataBean = new PushAdditionalData();
            pushAdditionalDataBean.setAssetId(pushAssetId);
            pushAdditionalDataBean.setEmployeeId(pushEmployeeId);
            pushAdditionalDataBean.setCompanyId(pushCompanyId);

            pushDatabean.setAdditionalData(pushAdditionalDataBean);

            if (AppSharedPrefs.getInstance(getApplicationContext()).isLogin() && checkIfNotificationEnabled()) {
                showNotification(this, pushDatabean);
            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showNotification(Context context, PushData pushData) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Keys.NOTIFICATION_DATA, pushData);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.mipmap.notification_icon);
            mBuilder.setColor(getResources().getColor(R.color.app_blue));
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        mBuilder.setContentTitle(pushData.getTitle());
        mBuilder.setContentText(pushData.getBody());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

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