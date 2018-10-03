package com.keykeeper.app.job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by ankurrawal on 3/10/18.
 */

public class LocationUploadJobCreator implements JobCreator {
    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        switch (tag) {
            case LocationSyncUploadJob.TAG:
                return new LocationSyncUploadJob();
            default:
                return null;
        }
    }
}