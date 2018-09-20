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


        public String getQrCodeNumber() {
            return qrCodeNumber;
        }

        public void setQrCodeNumber(String qrCodeNumber) {
            this.qrCodeNumber = qrCodeNumber;
        }

        public Integer getEnableNotification() {
           return enableNotification;
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
    }

}
