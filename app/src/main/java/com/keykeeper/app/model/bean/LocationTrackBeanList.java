package com.keykeeper.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.keykeeper.app.model.location.LocationTrackBean;

import java.util.ArrayList;

/**
 * Created by nazimakauser on 17/9/18.
 */

public class LocationTrackBeanList extends TrackLocationRequestEntity {

    @SerializedName("track_data")
    @Expose
    public ArrayList<LocationTrackBean> locationTrackBeanArrayList;



    public ArrayList<LocationTrackBean> getLocationTrackBeanArrayList() {
        return locationTrackBeanArrayList;
    }

    public void setLocationTrackBeanArrayList(ArrayList<LocationTrackBean> locationTrackBeanArrayList) {
        this.locationTrackBeanArrayList = locationTrackBeanArrayList;
    }

}
