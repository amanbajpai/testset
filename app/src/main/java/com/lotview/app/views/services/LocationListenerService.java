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
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.lotview.app.R;
import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.LocationTrackBeanList;
import com.lotview.app.model.bean.TrackLocationBaseResponse;
import com.lotview.app.model.location.LocationTrackBean;
import com.lotview.app.model.location.LocationTrackBeanDao;
import com.lotview.app.netcom.Keys;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.home.HomeActivity;

import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    int trackLocationGap = 120000;

    Handler trackLocationFrequentlyHandler = new Handler();
    Runnable trackLocationFrequentlyRunnable = new Runnable() {
        @Override
        public void run() {

            TrackEmployeeAssets();
        }
    };

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

            trackLocationFrequentlyHandler.postDelayed(trackLocationFrequentlyRunnable, trackLocationGap);
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
                    .setContentText("Lotview is syncing in background.")
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
                AppSharedPrefs.setLongitude(lng);
                speed = location.getSpeed();
                AppSharedPrefs.setSpeed(location.getSpeed() + "");
                getLocationBean(LocationListenerService.this);
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
        locationTrackBean.setEmployeeLatitue(latitude);
        locationTrackBean.setEmployeeLongitude(longitude);
        locationTrackBean.setEmployeeSpeed(speed);
        locationTrackBean.setEmployeeTimeStampLocal(Utils.getCurrentTimeStampDate());
        locationTrackBean.setEmployeeTimeStampLocalUTC(Utils.getCurrentUTC());
        locationTrackBean.setEmployee_key_ids(AppSharedPrefs.getInstance(this).getOwnedKeyIds());
        // If Logging for Testdrive then need to send 1 otherwise 0.
        if (AppSharedPrefs.isTestDriveRunning()) {
            locationTrackBean.setAsset_employee_test_drive_id(1);
        } else {
            locationTrackBean.setAsset_employee_test_drive_id(0);
        }
        // If Logging for Testdrive then need to send "TestDrive Id received in response of start Testdrive", otherwise 0.
        if (!TextUtils.isEmpty(AppSharedPrefs.getTestDriveId()) && AppSharedPrefs.getTestDriveId().length() > 0) {
            locationTrackBean.setTestDriveAssetId(Integer.valueOf(AppSharedPrefs.getTestDriveId()));
        } else {
            locationTrackBean.setTestDriveAssetId(0);
        }

        KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao().insert(locationTrackBean);

        return locationTrackBean;
    }


    private void TrackEmployeeAssets() {
        ArrayList<LocationTrackBean> trackBeanArrayList = (ArrayList<LocationTrackBean>) KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao().queryBuilder().where(LocationTrackBeanDao.Properties.EmployeeDataIsSync.eq(0)).list();

        LocationTrackBeanList locationTrackBeanList = new LocationTrackBeanList();
        locationTrackBeanList.setLocationTrackBeanArrayList(trackBeanArrayList);
        locationTrackBeanList.setApi_key(Keys.API_KEY);
        locationTrackBeanList.setDevice_id(Utils.getDeviceID());
        locationTrackBeanList.setDevice_type(Keys.TYPE_ANDROID);
        locationTrackBeanList.setEmployee_id(Integer.valueOf(AppSharedPrefs.getInstance(this).getEmployeeID()));
        locationTrackBeanList.setCompany_id(Integer.valueOf(AppSharedPrefs.getInstance(this).getCompanyID()));

        if (AppSharedPrefs.getInstance(this).getPushDeviceToken() != null && AppSharedPrefs.getInstance(this).getPushDeviceToken().trim().length() > 0) {
            locationTrackBeanList.setDevice_token(AppSharedPrefs.getInstance(this).getPushDeviceToken());
        } else {
            locationTrackBeanList.setDevice_token("aaaaaaa");
        }
        locationTrackBeanList.setToken_type(Keys.TOKEN_TYPE);
        locationTrackBeanList.setAccess_token(AppSharedPrefs.getInstance(this).getAccessToken());
        Call<TrackLocationBaseResponse> call = RetrofitHolder.getService().trackeEmployee(locationTrackBeanList);


        call.enqueue(new Callback<TrackLocationBaseResponse>() {
            @Override
            public void onResponse(Call<TrackLocationBaseResponse> call, Response<TrackLocationBaseResponse> response) {
                TrackLocationBaseResponse trackLocationBaseResponse = response.body();
                if (trackLocationBaseResponse.getSuccess()) {
                    if (trackLocationBaseResponse.getResultArray().size() > 0) {
                        trackLocationFrequentlyHandler.postDelayed(trackLocationFrequentlyRunnable, trackLocationGap);
                    } else {
                        trackLocationFrequentlyHandler.removeCallbacks(trackLocationFrequentlyRunnable);
                        stopSelf();
                    }

                    for (int i = 0; i < trackBeanArrayList.size(); i++) {
                        LocationTrackBean locationTrackBean = trackBeanArrayList.get(i);
                        locationTrackBean.setEmployeeDataIsSync(true);
                        KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao()
                                .update(locationTrackBean);
                    }
                } else {
                    trackLocationFrequentlyHandler.postDelayed(trackLocationFrequentlyRunnable, trackLocationGap);
                }
            }

            @Override
            public void onFailure(Call<TrackLocationBaseResponse> call, Throwable t) {

            }
        });
    }

}
