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
import android.support.v4.app.NotificationCompat;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankurrawal on 13/9/18.
 */

public class LocationListenerService extends Service {
    private static final String TAG = "TimerService";
    public static final int SERVICE_NOTIFICATION_ID = 101;
    private static SmartLocation.LocationControl location_control;
    private boolean isToStartLocationUpdate = false;
    private double latitude;
    private double longitude;
    private float speed;
    int trackLocationGap = 120000;
    LocationParams.Builder builder = null;


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

            NotificationChannel channel = new NotificationChannel(Keys.CHANNEL_NAME_BACKGROUND, "Fetching", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null, null);
            KeyKeepApplication.getInstance().getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(KeyKeepApplication.getInstance(), Keys.CHANNEL_NAME_BACKGROUND)
//                    .setContentTitle(KeyKeepApplication.getInstance().getString(R.string.app_name))
                    .setContentText("Keykeeper is syncing.")
                    .setAutoCancel(true)
                    .setChannelId(Keys.CHANNEL_NAME_BACKGROUND)
                    .setSound(null)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.notification_icon)
                    .setColor(getResources().getColor(R.color.app_blue))
                    .setShowWhen(true)
                    .setOnlyAlertOnce(true)
                    .setColor(Color.BLUE)
                    .setLocalOnly(true)
                    .build();
            startForeground(SERVICE_NOTIFICATION_ID, notification);
        }
    }


    private void getLocation() {

        builder = new LocationParams.Builder();
        builder.setAccuracy(LocationAccuracy.HIGH);
        builder.setDistance(5); // in Meteres
        builder.setInterval(10000L); // 10 seconds

//        builder.setDistance(0); // in Meteres
//        builder.setInterval(100); // 2 min

        LocationParams params = builder.build();

        location_control = SmartLocation.with(this).location().config(params);

        location_control.start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {


                if (location.getLatitude() != 0 && location.getLongitude() != 0) {
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


        if (AppSharedPrefs.isTestDriveRunning()) {
             /*Put Test Drive ID if testdrive is ON otherwise 0 for tracking*/
            locationTrackBean.setAsset_employee_test_drive_id(Integer.valueOf(AppSharedPrefs.getTestDriveId()));
             /*0 for tracking and if tesdrive is on Id of asset for which testdrive is on*/
            locationTrackBean.setTestDriveAssetId(Integer.valueOf(AppSharedPrefs.getTestDriveAssetId()));
        } else {
            locationTrackBean.setAsset_employee_test_drive_id(0);
            locationTrackBean.setTestDriveAssetId(0);
        }

        KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao().insert(locationTrackBean);

        return locationTrackBean;
    }


    private void TrackEmployeeAssets() {
        ArrayList<LocationTrackBean> trackBeanArrayList = (ArrayList<LocationTrackBean>) KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao().queryBuilder().where(LocationTrackBeanDao.Properties.EmployeeDataIsSync.eq(0)).list();

        /**
         * added for remove crash and manage handler
         */
        if (Utils.validateStringToInt(AppSharedPrefs.getInstance(this).getEmployeeID()) == 0) {
            return;
        }


        if (Connectivity.isConnected() && trackBeanArrayList != null && trackBeanArrayList.size() > 0) {

            setForegroundNotification();

            LocationTrackBeanList locationTrackBeanList = new LocationTrackBeanList();
            locationTrackBeanList.setLocationTrackBeanArrayList(trackBeanArrayList);

            locationTrackBeanList.setApi_key(Keys.API_KEY);
            locationTrackBeanList.setDevice_id(Utils.getDeviceID());
            locationTrackBeanList.setDevice_type(Keys.TYPE_ANDROID);
            locationTrackBeanList.setEmployee_id(Utils.validateStringToInt(AppSharedPrefs.getInstance(this).getEmployeeID()));
            locationTrackBeanList.setCompany_id(Utils.validateStringToInt(AppSharedPrefs.getInstance(this).getCompanyID()));

            if (AppSharedPrefs.getInstance(this).getPushDeviceToken() != null && AppSharedPrefs.getInstance(this).getPushDeviceToken().trim().length() > 0) {
                locationTrackBeanList.setDevice_token(AppSharedPrefs.getInstance(this).getPushDeviceToken());
            } else {
                locationTrackBeanList.setDevice_token("");
            }
            locationTrackBeanList.setToken_type(Keys.TOKEN_TYPE);
            locationTrackBeanList.setAccess_token(AppSharedPrefs.getInstance(this).getAccessToken());
            locationTrackBeanList.setEmp_current_lat(AppSharedPrefs.getLatitude());
            locationTrackBeanList.setEmp_current_long(AppSharedPrefs.getLongitude());

            Call<TrackLocationBaseResponse> call = RetrofitHolder.getService().trackEmployee(locationTrackBeanList);


            call.enqueue(new Callback<TrackLocationBaseResponse>() {
                @Override
                public void onResponse(Call<TrackLocationBaseResponse> call, Response<TrackLocationBaseResponse> response) {
                    TrackLocationBaseResponse trackLocationBaseResponse = response.body();
                    if (trackLocationBaseResponse.getSuccess()) {
                        if (trackLocationBaseResponse.getResultArray() != null && trackLocationBaseResponse.getResultArray().size() > 0) {
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
        } else {
            // Hide notification here
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.deleteNotificationChannel(Keys.CHANNEL_NAME_BACKGROUND);
                manager.cancel(SERVICE_NOTIFICATION_ID);
            }
            trackLocationFrequentlyHandler.postDelayed(trackLocationFrequentlyRunnable, trackLocationGap);
        }

    }

}
