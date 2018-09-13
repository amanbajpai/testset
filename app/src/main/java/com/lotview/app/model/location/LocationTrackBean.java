package com.lotview.app.model.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

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

    @SerializedName("emp_timestamp_local")
    @Expose
    public String employeeTimeStampLocal;

    @SerializedName("emp_timestamp_utc")
    @Expose
    public String employeeTimeStampLocalUTC;

    @SerializedName("emp_key_id")
    @Expose
    public String employee_key_ids;


    @Generated(hash = 27758410)
    public LocationTrackBean() {
    }

    @Generated(hash = 1240560938)
    public LocationTrackBean(double employeeLatitue, double employeeLongitude,
            int employeeId, float employeeSpeed, String employeeTimeStampLocal,
            String employeeTimeStampLocalUTC, String employee_key_ids) {
        this.employeeLatitue = employeeLatitue;
        this.employeeLongitude = employeeLongitude;
        this.employeeId = employeeId;
        this.employeeSpeed = employeeSpeed;
        this.employeeTimeStampLocal = employeeTimeStampLocal;
        this.employeeTimeStampLocalUTC = employeeTimeStampLocalUTC;
        this.employee_key_ids = employee_key_ids;
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


    public String getEmployee_key_ids() {
        return employee_key_ids;
    }

    public void setEmployee_key_ids(String employee_key_ids) {
        this.employee_key_ids = employee_key_ids;
    }

    public String getEmployeeTimeStampLocalUTC() {
        return employeeTimeStampLocalUTC;
    }

    public void setEmployeeTimeStampLocalUTC(String employeeTimeStampLocalUTC) {
        this.employeeTimeStampLocalUTC = employeeTimeStampLocalUTC;
    }

    public String getEmployeeTimeStampLocal() {
        return employeeTimeStampLocal;
    }

    public void setEmployeeTimeStampLocal(String employeeTimeStampLocal) {
        this.employeeTimeStampLocal = employeeTimeStampLocal;
    }
}
