package com.lotview.app.model.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * Created by ankurrawal on 12/9/18.
 */

@Entity
public class LocationTrackBean implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    @Id(autoincrement = true)
    @Index(unique = true)
    @SerializedName("track_id")
    @Expose
    public Long empTrackId;

    @SerializedName("emp_lat")
    @Expose
    public double employeeLatitue;

    @SerializedName("emp_long")
    @Expose
    public double employeeLongitude;

    @SerializedName("speed")
    @Expose
    public float employeeSpeed;

    @SerializedName("local_date_time")
    @Expose
    public String employeeTimeStampLocal;

    @SerializedName("utc_date_time")
    @Expose
    public String employeeTimeStampLocalUTC;

    @SerializedName("asset_id")
    @Expose
    public String employee_key_ids;

    @SerializedName("emp_data_issync")
    @Expose
    public boolean employeeDataIsSync;

    /*Put Test Drive ID if testdrive is ON otherwise 0 for tracking*/
    @SerializedName("asset_employee_test_drive_id")
    @Expose
    private int asset_employee_test_drive_id;

    /*0 for tracking and if tesdrive is on Id of asset for which testdrive is on*/
    @SerializedName("test_drive_asset_id")
    @Expose
    private int testDriveAssetId;


    public LocationTrackBean() {

    }


    @Generated(hash = 920016775)
    public LocationTrackBean(Long empTrackId, double employeeLatitue,
                             double employeeLongitude, float employeeSpeed,
                             String employeeTimeStampLocal, String employeeTimeStampLocalUTC,
                             String employee_key_ids, boolean employeeDataIsSync,
                             int asset_employee_test_drive_id, int testDriveAssetId) {
        this.empTrackId = empTrackId;
        this.employeeLatitue = employeeLatitue;
        this.employeeLongitude = employeeLongitude;
        this.employeeSpeed = employeeSpeed;
        this.employeeTimeStampLocal = employeeTimeStampLocal;
        this.employeeTimeStampLocalUTC = employeeTimeStampLocalUTC;
        this.employee_key_ids = employee_key_ids;
        this.employeeDataIsSync = employeeDataIsSync;
        this.asset_employee_test_drive_id = asset_employee_test_drive_id;
        this.testDriveAssetId = testDriveAssetId;
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

    public boolean isEmployeeDataIsSync() {
        return employeeDataIsSync;
    }

    public void setEmployeeDataIsSync(boolean employeeDataIsSync) {
        this.employeeDataIsSync = employeeDataIsSync;
    }

    public boolean getEmployeeDataIsSync() {
        return this.employeeDataIsSync;
    }


    public Long getEmpTrackId() {
        return empTrackId;
    }

    public void setEmpTrackId(Long empTrackId) {
        this.empTrackId = empTrackId;
    }

    public int getAsset_employee_test_drive_id() {
        return asset_employee_test_drive_id;
    }

    public void setAsset_employee_test_drive_id(int asset_employee_test_drive_id) {
        this.asset_employee_test_drive_id = asset_employee_test_drive_id;
    }

    public int getTestDriveAssetId() {
        return testDriveAssetId;
    }

    public void setTestDriveAssetId(int testDriveAssetId) {
        this.testDriveAssetId = testDriveAssetId;
    }
}
