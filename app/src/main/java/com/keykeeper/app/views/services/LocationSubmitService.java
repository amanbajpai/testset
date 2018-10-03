package com.keykeeper.app.views.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankurrawal on 13/9/18.
 */

public class LocationSubmitService extends Service {
    private static final String TAG = "TimerService";
    public static final int SERVICE_NOTIFICATION_ID = 101;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        setForegroundNotification();
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
            TrackEmployeeAssets();
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


    private void TrackEmployeeAssets() {

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
                            if (locationTrackBean.getEmpTrackId() >= startPoint &&
                                    locationTrackBean.getEmpTrackId() <= endPoint) {

                                locationTrackBean.setEmployeeDataIsSync(true);
                                KeyKeepApplication.getInstance().getDaoSession().getLocationTrackBeanDao()
                                        .update(locationTrackBean);
                            }
                        }

                        if (trackLocationBaseResponse.getResultArray() != null && trackLocationBaseResponse.getResultArray().size() > 0) {
                            // Continue Job scheduler
                        } else {
                            // and close the job scheduler
                            // stopSelf();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TrackLocationBaseResponse> call, Throwable t) {
                    // Continue Job scheduler
                }
            });
        } else {
            // Hide notification here
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.deleteNotificationChannel(Keys.CHANNEL_NAME_BACKGROUND);
                manager.cancel(SERVICE_NOTIFICATION_ID);
            }
        }

    }
}
