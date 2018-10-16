package com.keykeeper.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.keykeeper.app.model.location.LocationTrackBean;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 16/10/18
 */

public class ReturnKeyBeanList extends BaseRequestEntity{

    @SerializedName("submit_data")
    @Expose
    public ArrayList<ReturnKeyBean> returnKeyBeans;


    public ArrayList<ReturnKeyBean> getReturnKeyBeans() {
        return returnKeyBeans;
    }

    public void setReturnKeyBeans(ArrayList<ReturnKeyBean> returnKeyBeans) {
        this.returnKeyBeans = returnKeyBeans;
    }
}
