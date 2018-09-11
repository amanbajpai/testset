package com.lotview.app.firebase.firebase;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lotview.app.R;
import com.lotview.app.application.KeyKeepApplication;
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


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Utils.showLog(TAG, "Message==" + remoteMessage.getData());
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
            String PushSound = "";
            String PushIcon = "";
            String pushTittle = "";
            String PushBody = "";
            String PushAdditionalDataJson = "";
            int PushType = 0;


            //  PushIcon = jsonObject.optString("icon");
            pushDatabean.setIcon(jsonObject.optString("icon"));
            // PushSound = jsonObject.optString("sound");
            pushDatabean.setSound(jsonObject.optString("sound"));
            //   pushTittle = jsonObject.optString("title");
            pushDatabean.setTitle(jsonObject.optString("title"));
            //  PushBody = jsonObject.optString("body");
            pushDatabean.setBody(jsonObject.optString("body"));
            //   PushType = jsonObject.optInt("push_type");
            pushDatabean.setPushType(Integer.valueOf(jsonObject.optString("push_type")));

            PushAdditionalDataJson = jsonObject.optString("additional_data");

            JSONObject object = new JSONObject(PushAdditionalDataJson);
            int PushAssetId = object.optInt("asset_id");
            int PushEmployeeId = object.optInt("employee_id");
            int PushCompanyId = object.optInt("company_id");

            PushAdditionalData pushAdditionalDataBean = new PushAdditionalData();
            pushAdditionalDataBean.setAssetId(PushAssetId);
            pushAdditionalDataBean.setEmployeeId(PushEmployeeId);
            pushAdditionalDataBean.setCompanyId(PushCompanyId);

            pushDatabean.setAdditionalData(pushAdditionalDataBean);

            if (AppSharedPrefs.getInstance(getApplicationContext()).isLogin() && checkIfNotificationEnabled()) {
//                addNotification(PushType, PushBody, pushTittle, push_data);
                addNotification(pushDatabean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addNotification(PushData pushData) {
//        switch (pushData.getPushType()) {
//            case 2: // asset request approve
//                intent = new Intent(this, HomeActivity.class);
//
//                break;
//            case 4: // asset transfer request receiver
//
//                break;
//            case 5: // asset transfer approve
//                break;
//            case 6: // asset transfer decline sender/receiver
//                break;
//            case 8: // asset submit approve sender
//                break;
//
//        }

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Keys.NOTIFICATION_DATA, pushData);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.notification_icon);
            builder.setColor(getResources().getColor(R.color.app_blue));
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }
        builder.setContentTitle(pushData.getTitle());
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(pushData.getBody()));
        // Set the intent that will fire when the user taps the notification
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        notificationManager.notify(KeyKeepApplication.NOTIFICATION_ID, builder.build());

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