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

public class LocationListenerService extends Service {
    private static final String TAG = "TimerService";
    public static final int SERVICE_NOTIFICATION_ID = 101;
    private static SmartLocation.LocationControl location_control;
    private float speed;
    int trackLocationInterval = 30000;


    Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, trackLocationInterval - SystemClock.elapsedRealtime() % 1000);
            // whatever you want to do below
            TrackEmployeeAssets();
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
        handler.removeCallbacks(periodicUpdate);
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
            handler.post(periodicUpdate);
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

                            getLocationBean(location);
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

    }

    public static void stopLocationUpdate() {
        if (location_control != null) {
            location_control.stop();
        }
        // handler.removeCallbacks(periodicUpdate);
    }

    public LocationTrackBean getLocationBean(Location location) {

        LocationTrackBean locationTrackBean = new LocationTrackBean();
        locationTrackBean.setEmployeeLatitue(location.getLatitude());
        locationTrackBean.setEmployeeLongitude(location.getLongitude());
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

        if (!Connectivity.isConnected()) {
            return;
        }

        ArrayList<LocationTrackBean> trackBeanArrayList = (ArrayList<LocationTrackBean>) KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao().queryBuilder().where(LocationTrackBeanDao.Properties.EmployeeDataIsSync.eq(0)).limit(50).list();

        /**
         * added for remove crash and manage handler
         */
        if (Utils.validateStringToInt(AppSharedPrefs.getInstance(this).getEmployeeID()) == 0) {
            return;
        }
        if (trackBeanArrayList.size() == 0) {
            return;
        }


        long startPoint = trackBeanArrayList.get(0).getEmpTrackId();
        long endPoint = trackBeanArrayList.get(trackBeanArrayList.size() - 1).getEmpTrackId();


        if (Connectivity.isConnected() && trackBeanArrayList != null && trackBeanArrayList.size() > 0) {

//            setForegroundNotification();
//            HashMap<Long, LocationTrackBean> trackBeanHashMap = getMapFromList(trackBeanArrayList);

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
            locationTrackBeanList.setEmp_current_speed(AppSharedPrefs.getSpeed());

            Call<TrackLocationBaseResponse> call = RetrofitHolder.getService().trackEmployee(locationTrackBeanList);

            call.enqueue(new Callback<TrackLocationBaseResponse>() {

                @Override
                public void onResponse(Call<TrackLocationBaseResponse> call, Response<TrackLocationBaseResponse> response) {
                    TrackLocationBaseResponse trackLocationBaseResponse = response.body();
                    try {
                        if (trackLocationBaseResponse.getSuccess()) {

                            for (int i = 0; i < trackBeanArrayList.size(); i++) {
                                LocationTrackBean locationTrackBean = trackBeanArrayList.get(i);
                                if (locationTrackBean.getEmpTrackId() >= startPoint &&
                                        locationTrackBean.getEmpTrackId() <= endPoint) {

                                    locationTrackBean.setEmployeeDataIsSync(true);
                                    KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao()
                                            .update(locationTrackBean);
                                }
                            }

                            if (trackLocationBaseResponse.getResultArray() != null && trackLocationBaseResponse.getResultArray().size() > 0) {
                                handler.removeCallbacks(periodicUpdate);
                                handler.postDelayed(periodicUpdate, trackLocationInterval);
                            } else {
                                handler.removeCallbacks(periodicUpdate);
                                stopSelf();
                            }
                        } else {
                            handler.removeCallbacks(periodicUpdate);
                            handler.postDelayed(periodicUpdate, trackLocationInterval);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
            handler.removeCallbacks(periodicUpdate);
            handler.postDelayed(periodicUpdate, trackLocationInterval);
        }

    }


}
