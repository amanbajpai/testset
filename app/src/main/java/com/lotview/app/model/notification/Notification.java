package com.lotview.app.model.notification;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by priyankatiwari on 11/10/17.
 */

public class Notification {

    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("responseData")
    @Expose
    private List<NotificationList> responseData = null;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<NotificationList> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<NotificationList> responseData) {
        this.responseData = responseData;
    }

}

