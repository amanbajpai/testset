package com.lotview.app.firebase.firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.LogUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.assetDetail.AssetDetailActivity;

import org.json.JSONObject;

import java.util.Map;

/*
 * Created by ankurrawal
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Map<String, String> params;
    public static int value = 1;
    private int pushType;
    PushData push_data = null;
    PushAdditionalData push_additional_data = null;


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

            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            String PushSound = "";
            String PushIcon = "";
            String pushTittle = "";
            String PushBody = "";
            String PushAdditionalData = "";
            int PushType = 0;


            PushIcon = jsonObject.optString("icon");
            PushSound = jsonObject.optString("sound");
            pushTittle = jsonObject.optString("title");
            PushBody = jsonObject.optString("body");
            PushType = jsonObject.optInt("push_type");
            PushAdditionalData = jsonObject.optString("additional_data");

            JSONObject object = new JSONObject(PushAdditionalData);
//            Gson gson = new Gson();
//            push_additional_data = gson.fromJson(object.toString(), PushAdditionalData.class);
            int PushAssetId = object.optInt("asset_id");
            int PushEmployeeId = object.optInt("employee_id");
            int PushCompanyId = object.optInt("company_id");

            switch (pushType) {

//                    case 1: // Booking Data
//                        pushData = jsonObject.optString("push_data");
//                        break;
//                    case 2: // Event Data
//                        pushData = jsonObject.optString("push_data");
////                        JSONObject jsonPushData_for_event = new JSONObject(pushData_forevent);
////                        pushData = jsonPushData_for_event.optString("event_data");
//                        break;
//                    case 3: // Normal data
//                        //no data to add
//                        break;
//                    case 4: // payment
//                        pushData = jsonObject.optString("push_data");
//                        break;
//
//                    case 5: // review
//                        pushData = jsonObject.optString("push_data");
//                        break;
            }

            if (AppSharedPrefs.getInstance(getApplicationContext()).isLogin() && checkIfNotificationEnabled()) {

                addNotification(pushType, PushBody, pushTittle, push_data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void addNotification(int pushType, String Message, String Tittle, PushData pushData) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.ic_launcher);
//            builder.setColor(getResources().getColor(R.color.black));
//            builder.setColor(getResources().getColor(R.color.notification_bg_color));
            builder.setColor(getResources().getColor(R.color.app_blue));
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }

        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentTitle(Tittle);
        builder.setContentText(Tittle + "\n" + Message);
        PendingIntent contentIntent = null;
        Intent intent = null;
        switch (pushType) {
            case 1: // Booking Data
                intent = new Intent(this, AssetDetailActivity.class);
//                intent.putExtra(ConstantsLib.NOTIFICATION_DATA, pushData);
                break;

        }

        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(KeyKeepApplication.NOTIFICATION_ID, builder.build());

        contentIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);

        manager.notify(KeyKeepApplication.NOTIFICATION_ID, builder.build());
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