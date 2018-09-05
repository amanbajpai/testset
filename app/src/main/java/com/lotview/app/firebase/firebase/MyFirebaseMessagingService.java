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
import com.google.gson.Gson;
import com.lotview.app.R;
import com.lotview.app.application.KeyKeepApplication;
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
            String pushMessage = "";
            String pushTittle = "";
            String pushBody = "";
//          String pushData = "";

            if (jsonObject.toString().contains("mp_message")) {
//                String message = jsonObject.has("mp_message") ? jsonObject.optString("mp_message") : "";
//                mixPanelNotification(message, getResources().getString(R.string.app_name));

            } else {

                pushMessage = jsonObject.optString("message");
                pushTittle = jsonObject.optString("title");
                pushBody = jsonObject.optString("body");
                pushType = jsonObject.optInt("push_type");

                JSONObject jsonObj = makejson(jsonObject);
                JSONObject object = jsonObj.getJSONObject("push_data");
                Gson gson = new Gson();
                push_data = gson.fromJson(object.toString(), PushData.class);

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
            }

            if (AppSharedPrefs.getInstance(getApplicationContext()).isLogin() && checkIfNotificationEnabled()) {

                addNotification(pushType, pushMessage, pushTittle, push_data);
            }


        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }


    private void addNotification(int pushType, String Message, String Tittle, PushData pushData) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.ic_launcher);
//            builder.setColor(getResources().getColor(R.color.black));
//            builder.setColor(getResources().getColor(R.color.notification_bg_color));
            builder.setColor(getResources().getColor(R.color.colorRed));
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

    private JSONObject makejson(JSONObject jsonObject) {
        JSONObject newObject = new JSONObject();
        try {
            String pushData = jsonObject.optString("push_data");
            JSONObject rootJson = new JSONObject(pushData);


            jsonObject.put("push_data", rootJson);

            LogUtils.e(TAG, "jsonObject==" + jsonObject.toString(4));

            return jsonObject;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

}