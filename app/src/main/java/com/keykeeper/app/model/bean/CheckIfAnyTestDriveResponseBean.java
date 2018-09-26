package com.keykeeper.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankurrawal on 19/9/18.
 */

public class CheckIfAnyTestDriveResponseBean extends BaseResponse {


    @SerializedName("result")
    @Expose
    CheckIfAnyTestDriveResponseBean.Result result;

    public CheckIfAnyTestDriveResponseBean.Result getResult() {
        return result;
    }

    public void setResult(CheckIfAnyTestDriveResponseBean.Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("asset_employee_test_drive_id")
        @Expose
        private String asset_employee_test_drive_id;

        @SerializedName("employee_id")
        @Expose
        private String employee_id;

        @SerializedName("company_id")
        @Expose
        private String company_id;

        @SerializedName("test_drive_start_status")
        @Expose
        private String test_drive_start_status;

        @SerializedName("asset_id")
        @Expose
        private String asset_id;

        @SerializedName("asset_name")
        @Expose
        private String asset_name;

        @SerializedName("start_date_time")
        @Expose
        private String start_date_time;

        @SerializedName("end_date_time")
        @Expose
        private String end_date_time;

        @SerializedName("start_date_time_utc")
        @Expose
        private String start_date_time_utc;

        @SerializedName("start_latitude")
        @Expose
        private String start_latitude;

        @SerializedName("start_longitude")
        @Expose
        private String start_longitude;

        @SerializedName("end_latitude")
        @Expose
        private String end_latitude;

        @SerializedName("end_longitude")
        @Expose
        private String end_longitude;

        public String getAsset_employee_test_drive_id() {
            return asset_employee_test_drive_id;
        }

        public void setAsset_employee_test_drive_id(String asset_employee_test_drive_id) {
            this.asset_employee_test_drive_id = asset_employee_test_drive_id;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getTest_drive_start_status() {
            return test_drive_start_status;
        }

        public void setTest_drive_start_status(String test_drive_start_status) {
            this.test_drive_start_status = test_drive_start_status;
        }

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
        }

        public String getAsset_name() {
            return asset_name;
        }

        public void setAsset_name(String asset_name) {
            this.asset_name = asset_name;
        }

        public String getStart_date_time() {
            return start_date_time;
        }

        public void setStart_date_time(String start_date_time) {
            this.start_date_time = start_date_time;
        }

        public String getEnd_date_time() {
            return end_date_time;
        }

        public void setEnd_date_time(String end_date_time) {
            this.end_date_time = end_date_time;
        }

        public String getStart_date_time_utc() {
            return start_date_time_utc;
        }

        public void setStart_date_time_utc(String start_date_time_utc) {
            this.start_date_time_utc = start_date_time_utc;
        }

        public String getStart_latitude() {
            return start_latitude;
        }

        public void setStart_latitude(String start_latitude) {
            this.start_latitude = start_latitude;
        }

        public String getStart_longitude() {
            return start_longitude;
        }

        public void setStart_longitude(String start_longitude) {
            this.start_longitude = start_longitude;
        }

        public String getEnd_latitude() {
            return end_latitude;
        }

        public void setEnd_latitude(String end_latitude) {
            this.end_latitude = end_latitude;
        }

        public String getEnd_longitude() {
            return end_longitude;
        }

        public void setEnd_longitude(String end_longitude) {
            this.end_longitude = end_longitude;
        }
    }
}