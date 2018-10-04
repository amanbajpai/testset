package com.keykeeper.app.job;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.services.LocationSubmitService;

/**
 * Created by ankurrawal on 3/10/18.
 */

public class LocationSyncUploadJob extends Job {

    public static final String TAG = "LocationSyncUploadJob";

    @Override
    @NonNull
    protected Result onRunJob(@NonNull final Params params) {

        try {
//            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), HomeActivity.class), 0);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel(TAG, "Job Demo", NotificationManager.IMPORTANCE_LOW);
//                channel.setDescription("Job demo job");
//                getContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
//            }
//
//            Notification notification = new NotificationCompat.Builder(getContext(), TAG)
//                    .setContentTitle("ID " + params.getId())
//                    .setContentText("Job ran, exact " + params.isExact() + " , periodic " + params.isPeriodic() + ", transient " + params.isTransient())
//                    .setAutoCancel(true)
//                    .setChannelId(TAG)
//                    .setSound(null)
//                    .setContentIntent(pendingIntent)
//                    .setSmallIcon(R.mipmap.notification_icon)
//                    .setShowWhen(true)
//                    .setColor(Color.GREEN)
//                    .setLocalOnly(true)
//                    .build();
//
//            NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), notification);
            Utils.showLog(TAG, "onRunJob");
            Intent autoUpload = new Intent(KeyKeepApplication.getInstance(), LocationSubmitService.class);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                KeyKeepApplication.getInstance().startForegroundService(autoUpload);
            } else {
                KeyKeepApplication.getInstance().startService(autoUpload);
            }
            return Result.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILURE;
        }
    }

}