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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private boolean isToStartLocationUpdate = false;
    private float speed;
    int trackLocationGap = 30000;


    Handler trackLocationFrequentlyHandler = new Handler();
    Runnable trackLocationFrequentlyRunnable = new Runnable() {
        @Override
        public void run() {

            TrackEmployeeAssets();
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
                }
            }
        });

    }

    public static void stopLocationUpdate() {
        if (location_control != null) {
            location_control.stop();
        }
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

        ArrayList<LocationTrackBean> trackBeanArrayList = (ArrayList<LocationTrackBean>) KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao().queryBuilder().where(LocationTrackBeanDao.Properties.EmployeeDataIsSync.eq(0)).limit(50).list();


        /**
         * added for remove crash and manage handler
         */
        if (Utils.validateStringToInt(AppSharedPrefs.getInstance(this).getEmployeeID()) == 0) {
            return;
        }

        if (Connectivity.isConnected() && trackBeanArrayList != null && trackBeanArrayList.size() > 0) {

//            setForegroundNotification();

            HashMap<Long, LocationTrackBean> trackBeanHashMap = getMapFromList(trackBeanArrayList);

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

                        for (int i = 0; i < trackBeanArrayList.size(); i++) {
                            LocationTrackBean locationTrackBean = trackBeanArrayList.get(i);
                            locationTrackBean.setEmployeeDataIsSync(true);
                            KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao()
                                    .update(locationTrackBean);
                        }


                        // Create a Iterator to EntrySet of HashMap
                        Iterator<Map.Entry<Long, LocationTrackBean>> entryIt = trackBeanHashMap.entrySet().iterator();

                        // Iterate over all the elements
                        while (entryIt.hasNext()) {
                            Map.Entry<Long, LocationTrackBean> entry = entryIt.next();
                            // Check if Value associated with Key is 10
                            if (trackBeanArrayList.contains(entry.getValue())) {
                                // Update the element
                                LocationTrackBean locationTrackBean = entry.getValue();
                                locationTrackBean.setEmployeeDataIsSync(true);
                                KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao()
                                        .update(locationTrackBean);
                            }
                        }


                        if (trackLocationBaseResponse.getResultArray() != null && trackLocationBaseResponse.getResultArray().size() > 0) {
                            trackLocationFrequentlyHandler.postDelayed(trackLocationFrequentlyRunnable, trackLocationGap);
                        } else {
                            trackLocationFrequentlyHandler.removeCallbacks(trackLocationFrequentlyRunnable);
                            stopSelf();
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

    private HashMap<Long, LocationTrackBean> getMapFromList(ArrayList<LocationTrackBean> locationTrackBeanList) {
        HashMap<Long, LocationTrackBean> hashMap = new HashMap<>();

        for (int i = 0; i < locationTrackBeanList.size(); i++) {
            LocationTrackBean locationTrackBean = locationTrackBeanList.get(i);
            hashMap.put(locationTrackBean.getEmpTrackId(), locationTrackBean);
        }
        return hashMap;
    }

    {

    }

}
