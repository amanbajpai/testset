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

        public int company_id;


       @SerializedName("enable_notification")
       @Expose
       private Integer enableNotification;



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

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }
    }

}
