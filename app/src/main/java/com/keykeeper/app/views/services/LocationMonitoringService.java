package com.keykeeper.app.views.services;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankurrawal on 9/10/18.
 */


public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Context context;
    int trackLocationInterval = 30000;
    public static final int SERVICE_NOTIFICATION_ID = 101;
    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    static GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();


    static Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, trackLocationInterval - SystemClock.elapsedRealtime() % 1000);
            TrackEmployeeAssets();
            Utils.showLog("run", "Postdelayed");
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        setForegroundNotification();
    }

    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(trackLocationInterval);
//        mLocationRequest.setFastestInterval(trackLocationInterval);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationRequest.setSmallestDisplacement(3);
//        mLocationRequest.setInterval(5000L);
//        mLocationRequest.setInterval(5000L);
        mLocationClient.connect();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.

        if (intent != null) {
            handler.post(periodicUpdate);
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d(TAG, "== location != null");

            handleUpdatedLocation(location);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }

    private void handleUpdatedLocation(Location location) {
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            String lat = location.getLatitude() + "";
            String lng = location.getLongitude() + "";
            String speed = location.getSpeed() + "";

            Log.e("Accuracy: ", "" + location.getAccuracy());

            if (location.getAccuracy() > 0 && location.getAccuracy() < 15) {

                if (location.getLatitude() != Utils.validateStringToDouble(AppSharedPrefs.getInstance(context).getLatitude())
                        || location.getLongitude() != Utils.validateStringToDouble(AppSharedPrefs.getInstance(context).getLongitude())) {

                    Log.e(lat + " onLocationUpdated: ", lng + " <<" + " Speed: " + speed);

                    getLocationBean(location);
                }
            }
//            AppSharedPrefs.setLatitude(lat);
//            AppSharedPrefs.setLongitude(lng);
//            AppSharedPrefs.setSpeed(location.getSpeed() + "");
            try {
                AppSharedPrefs.setIsLocationFromMock(location.isFromMockProvider());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setForegroundNotification() {

        //PendingIntent pendingIntent = PendingIntent.getActivity(KeyKeepApplication.getInstance(), 101, new Intent(KeyKeepApplication.getInstance(), HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
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
                //.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setColor(getResources().getColor(R.color.app_blue))
                .setShowWhen(true)
                .setOnlyAlertOnce(true)
                .setColor(Color.BLUE)
                .setLocalOnly(true)
                .build();
        startForeground(SERVICE_NOTIFICATION_ID, notification);

    }

    public static void stopLocationUpdate() {

        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
        // handler.removeCallbacks(periodicUpdate);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(periodicUpdate);
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
        stopSelf();
        super.onDestroy();
    }

    public LocationTrackBean getLocationBean(Location location) {

        LocationTrackBean locationTrackBean = new LocationTrackBean();
        locationTrackBean.setEmployeeLatitue(location.getLatitude());
        locationTrackBean.setEmployeeLongitude(location.getLongitude());
        locationTrackBean.setEmployeeSpeed(location.getSpeed());
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


    /**
     * TrackEmployeeAssets check for the records still in database and not uploaded to server
     */
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
            if (mLocationClient.isConnected() || mLocationClient.isConnecting()) {
                return;
            } else {
                mLocationClient.connect();
                return;
            }

        }


        long startPoint = trackBeanArrayList.get(0).getEmpTrackId();
        long endPoint = trackBeanArrayList.get(trackBeanArrayList.size() - 1).getEmpTrackId();

        if (Connectivity.isConnected() && trackBeanArrayList != null && trackBeanArrayList.size() > 0) {

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
                                // Store owned key ids here
                                storeOwnedKeyIdsPreferences(trackLocationBaseResponse);
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

    private void storeOwnedKeyIdsPreferences(TrackLocationBaseResponse
                                                     trackLocationBaseResponse) {
        String ownedKeys = null;
        if (trackLocationBaseResponse != null && trackLocationBaseResponse.getResultArray().size() > 0) {
            for (int i = 0; i < trackLocationBaseResponse.getResultArray().size(); i++) {
                if (!TextUtils.isEmpty(String.valueOf(trackLocationBaseResponse.getResultArray().get(i).getAsset_id()))) {
                    if (ownedKeys != null) {
                        ownedKeys = ownedKeys + "," + trackLocationBaseResponse.getResultArray().get(i).getAsset_id();
                    } else {
                        ownedKeys = String.valueOf(trackLocationBaseResponse.getResultArray().get(i).getAsset_id());
                    }
                }
            }
            AppSharedPrefs.getInstance(context).setOwnedKeyIds(ownedKeys);
        }
    }

}