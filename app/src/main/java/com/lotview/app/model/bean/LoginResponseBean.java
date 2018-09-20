package com.lotview.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akshaydashore on 28/8/18
 */
public class LoginResponseBean extends BaseResponse {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("employee_id")
        @Expose
        private Integer employeeId;
        @SerializedName("company_id")
        @Expose
        private Integer companyId;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("chat_url")
        @Expose
        private String chatUrl;

        @SerializedName("qr_code_number")
        @Expose
        private String qrCodeNumber;

        @SerializedName("enable_notification")
        @Expose
        private Integer enableNotification;

        @SerializedName("running_test_drive")
        @Expose
        private RunningTestDriveResponse runningTestDriveResponse;

        public String getQrCodeNumber() {
            return qrCodeNumber;
        }

        public void setQrCodeNumber(String qrCodeNumber) {
            this.qrCodeNumber = qrCodeNumber;
        }
        public RunningTestDriveResponse getRunningTestDriveResponse() {
            return runningTestDriveResponse;
        }

        public void setRunningTestDriveResponse(RunningTestDriveResponse runningTestDriveResponse) {
            this.runningTestDriveResponse = runningTestDriveResponse;
        }



        public void setEnableNotification(Integer enableNotification) {
            this.enableNotification = enableNotification;
        }

        public Integer getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Integer employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getChatUrl() {
            return chatUrl;
        }

        public void setChatUrl(String chatUrl) {
            this.chatUrl = chatUrl;
        }

        public Integer getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        public class RunningTestDriveResponse {

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

}
