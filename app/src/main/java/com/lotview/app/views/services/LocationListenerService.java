package com.lotview.app.views.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lotview.app.R;
import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.location.LocationTrackBean;
import com.lotview.app.netcom.Keys;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.home.HomeActivity;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

/**
 * Created by ankurrawal on 13/9/18.
 */

public class LocationListenerService extends Service {
    private static final String TAG = "TimerService";
    private static SmartLocation.LocationControl location_control;
    private boolean isToStartLocationUpdate = false;
    private double latitude;
    private double longitude;
    private float speed;

    public LocationListenerService(boolean needToStartLocation) {
        isToStartLocationUpdate = needToStartLocation;
    }

    public LocationListenerService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // if (isToStartLocationUpdate) {
        setForegroundNotification();
        // }
    }

    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            //  if (isToStartLocationUpdate) {
            getLocation();
            //  }

        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void setForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent pendingIntent = PendingIntent.getActivity(KeyKeepApplication.getInstance(), 101, new Intent(KeyKeepApplication.getInstance(), HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationChannel channel = new NotificationChannel(Keys.CHANNEL_NAME, "Fetching", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null, null);
            KeyKeepApplication.getInstance().getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(KeyKeepApplication.getInstance(), Keys.CHANNEL_NAME)
                    .setContentTitle(KeyKeepApplication.getInstance().getString(R.string.app_name))
//                .setContentText(getNotificationText())
                    .setAutoCancel(true)
                    .setChannelId(Keys.CHANNEL_NAME)
                    .setSound(null)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setShowWhen(true)
                    .setOnlyAlertOnce(true)
                    .setColor(Color.BLUE)
                    .setLocalOnly(true)
                    .build();
            startForeground(101, notification);
            Log.e(TAG, "onStartCommand: ");
        }
    }


    private void getLocation() {

        LocationParams.Builder builder = new LocationParams.Builder();
        builder.setAccuracy(LocationAccuracy.HIGH);
        builder.setDistance(5); // in Meteres
        builder.setInterval(1000);
        LocationParams params = builder.build();


        location_control = SmartLocation.with(this).location().config(params);

        location_control.start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                String lat = location.getLatitude() + "";
                String lng = location.getLongitude() + "";
                Log.e(lat + "onLocationUpdated: ", lng + "<<");
                AppSharedPrefs.setLatitude(lat);
                AppSharedPrefs.setLatitude(lng);
                AppSharedPrefs.setSpeed(location.getSpeed() + "");

            }
        });
    }

    public static void stopLocationUpdate() {
        if (location_control != null) {
            location_control.stop();
        }
    }

    public LocationTrackBean getLocationBean(Context context) {

        LocationTrackBean locationTrackBean = new LocationTrackBean();
        locationTrackBean.setEmployeeId(Integer.valueOf(AppSharedPrefs.getInstance(context).getEmployeeID()));
        locationTrackBean.setEmployeeLatitue(latitude);
        locationTrackBean.setEmployeeLongitude(longitude);
        locationTrackBean.setEmployeeSpeed(speed);
        locationTrackBean.setEmployeeTimeStampLocal(Utils.getCurrentTimeStampDate());
        locationTrackBean.setEmployeeTimeStampLocalUTC(Utils.getCurrentUTC());
        locationTrackBean.setEmployee_key_ids(AppSharedPrefs.getInstance(this).getOwnedKeyIds());

        KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao().insertOrReplaceInTx(locationTrackBean);

        return locationTrackBean;
    }
}
