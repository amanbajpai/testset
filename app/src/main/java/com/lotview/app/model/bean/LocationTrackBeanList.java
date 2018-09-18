package com.lotview.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lotview.app.model.location.LocationTrackBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazimakauser on 17/9/18.
 */

public class LocationTrackBeanList extends BaseRequestEntity{

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
