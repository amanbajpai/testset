package com.lotview.app.model.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ankurrawal on 12/9/18.
 */

@Entity
public class LocationTrackBean implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;


    @SerializedName("emp_lat")
    @Expose
    public double employeeLatitue;

    @SerializedName("emp_long")
    @Expose
    public double employeeLongitude;

    @SerializedName("emp_id")
    @Expose
    public int employeeId;

    @SerializedName("emp_speed")
    @Expose
    public float employeeSpeed;

    @SerializedName("emp_timestamp")
    @Expose
    public long employeeTimeStamp;

    @Generated(hash = 1329653950)
    public LocationTrackBean(double employeeLatitue, double employeeLongitude,
            int employeeId, float employeeSpeed, long employeeTimeStamp) {
        this.employeeLatitue = employeeLatitue;
        this.employeeLongitude = employeeLongitude;
        this.employeeId = employeeId;
        this.employeeSpeed = employeeSpeed;
        this.employeeTimeStamp = employeeTimeStamp;
    }

    @Generated(hash = 27758410)
    public LocationTrackBean() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public double getEmployeeLatitue() {
        return employeeLatitue;
    }

    public void setEmployeeLatitue(double employeeLatitue) {
        this.employeeLatitue = employeeLatitue;
    }

    public double getEmployeeLongitude() {
        return employeeLongitude;
    }

    public void setEmployeeLongitude(double employeeLongitude) {
        this.employeeLongitude = employeeLongitude;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public float getEmployeeSpeed() {
        return employeeSpeed;
    }

    public void setEmployeeSpeed(float employeeSpeed) {
        this.employeeSpeed = employeeSpeed;
    }

    public long getEmployeeTimeStamp() {
        return employeeTimeStamp;
    }

    public void setEmployeeTimeStamp(long employeeTimeStamp) {
        this.employeeTimeStamp = employeeTimeStamp;
    }
}
