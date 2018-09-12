package com.lotview.app.model.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ankurrawal on 12/9/18.
 */

public class LocationTrackBean implements Serializable {

    @SerializedName("emp_lat")
    @Expose
    public int employeeLatitue;

    @SerializedName("emp_long")
    @Expose
    public int employeeLongitude;

    @SerializedName("emp_id")
    @Expose
    public int employeeId;

    @SerializedName("emp_speed")
    @Expose
    public int employeeSpeed;

    @SerializedName("emp_timestamp")
    @Expose
    public long employeeTimeStamp;

    public int getEmployeeLatitue() {
        return employeeLatitue;
    }

    public void setEmployeeLatitue(int employeeLatitue) {
        this.employeeLatitue = employeeLatitue;
    }

    public int getEmployeeLongitude() {
        return employeeLongitude;
    }

    public void setEmployeeLongitude(int employeeLongitude) {
        this.employeeLongitude = employeeLongitude;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getEmployeeSpeed() {
        return employeeSpeed;
    }

    public void setEmployeeSpeed(int employeeSpeed) {
        this.employeeSpeed = employeeSpeed;
    }

    public long getEmployeeTimeStamp() {
        return employeeTimeStamp;
    }

    public void setEmployeeTimeStamp(long employeeTimeStamp) {
        this.employeeTimeStamp = employeeTimeStamp;
    }
}
