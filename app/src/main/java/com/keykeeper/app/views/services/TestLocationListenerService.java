package com.keykeeper.app.views.services;

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
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.keykeeper.app.R;
import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.model.bean.LocationTrackBeanList;
import com.keykeeper.app.model.bean.TrackLocationBaseResponse;
import com.keykeeper.app.model.location.LocationTrackBean;
import com.keykeeper.app.model.location.LocationTrackBeanDao;
import com.keykeeper.app.netcom.Keys;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.home.HomeActivity;

import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankurrawal on 13/9/18.
 */

public class TestLocationListenerService extends Service {

    private static final String TAG = "TestLocationListenerService";
    public static final int SERVICE_NOTIFICATION_ID = 101;
    private static SmartLocation.LocationControl location_control;
    private float speed;
    int trackLocationInterval = 30000;

    static Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, trackLocationInterval - SystemClock.elapsedRealtime() % 1000);
            getLocation();
            Utils.showLog("run", "Postdelayed");
        }
    };


    private Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // if (isToStartLocationUpdate) {
        setForegroundNotification();
        // }
    }

    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);

        } else {
            stopSelf();
        }
//        handler.removeCallbacks(periodicUpdate);
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            //  if (isToStartLocationUpdate) {
            getLocation();

//            trackLocationFrequentlyHandler.removeCallbacks(trackLocationFrequentlyRunnable);
//            trackLocationFrequentlyHandler.postDelayed(trackLocationFrequentlyRunnable, trackLocationInterval);
//            handler.post(periodicUpdate);
            //  }

//            KeyKeepApplication.getInstance().startLocationUploadPeriodicJob();
        }
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void setForegroundNotification() {

        PendingIntent pendingIntent = PendingIntent.getActivity(KeyKeepApplication.getInstance(), 101, new Intent(KeyKeepApplication.getInstance(), HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Keys.CHANNEL_NAME_BACKGROUND, "Fetching", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null, null);
            KeyKeepApplication.getInstance().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(KeyKeepApplication.getInstance(), Keys.CHANNEL_NAME_BACKGROUND)
                .setContentText("Keykeeper is syncing.")
                .setAutoCancel(true)
                .setChannelId(Keys.CHANNEL_NAME_BACKGROUND)
                .setSound(null)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setColor(getResources().getColor(R.color.app_blue))
                .setShowWhen(true)
                .setOnlyAlertOnce(true)
                .setColor(Color.BLUE)
                .setLocalOnly(true)
                .build();
        startForeground(SERVICE_NOTIFICATION_ID, notification);

    }


    private void getLocation() {

        LocationGooglePlayServicesProvider provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        LocationParams.Builder builder = null;
        builder = new LocationParams.Builder();

        builder.setAccuracy(LocationAccuracy.HIGH);
        builder.setDistance(10); // in Meteres
        builder.setInterval(5000L); // 5 seconds

//        For Testing use
//        builder.setDistance(0); // in Meteres
//        builder.setInterval(100); // 2 min

        LocationParams params = builder.build();

        location_control = SmartLocation.with(this).location(provider).config(params).continuous();
//        location_control.start(this);
        location_control.start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                    String lat = location.getLatitude() + "";
                    String lng = location.getLongitude() + "";
                    speed = location.getSpeed();

                    Log.e("Accuracy: ", "" + location.getAccuracy());

                    if (location.getAccuracy() > 0 && location.getAccuracy() < 20) {

                        if (location.getLatitude() != Utils.validateStringToDouble(AppSharedPrefs.getInstance(context).getLatitude())
                                || location.getLongitude() != Utils.validateStringToDouble(AppSharedPrefs.getInstance(context).getLongitude())) {

                            Log.e(lat + " onLocationUpdated: ", lng + " <<");

                        }
                    }
                    AppSharedPrefs.setLatitude(lat);
                    AppSharedPrefs.setLongitude(lng);
                    AppSharedPrefs.setSpeed(location.getSpeed() + "");
                    try {
                        AppSharedPrefs.setIsLocationFromMock(location.isFromMockProvider());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        handler.removeCallbacks(periodicUpdate);
        handler.postDelayed(periodicUpdate, trackLocationInterval - SystemClock.elapsedRealtime() % 1000);
    }

    public static void stopLocationUpdate() {
        if (location_control != null) {
            location_control.stop();
        }
    }




}
