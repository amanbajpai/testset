package com.lotview.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ankurrawal on 6/9/18.
 */

public class NotificationsResponseBean extends BaseResponse {

    @SerializedName("result")
    @Expose
    ArrayList<NotificationsResponseBean.Result> result;

    public ArrayList<NotificationsResponseBean.Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<NotificationsResponseBean.Result> result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("notification_id")
        @Expose
        private String notificationId;

        @SerializedName("notification_title")
        @Expose
        private String notificationTitle;

        @SerializedName("notification_description")
        @Expose
        private String notificationDescription;

        @SerializedName("notification_type_id")
        @Expose
        private String notificationTypeId;

        @SerializedName("title")
        @Expose
        private String title;

        public String getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(String notificationId) {
            this.notificationId = notificationId;
        }

        public String getNotificationTitle() {
            return notificationTitle;
        }

        public void setNotificationTitle(String notificationTitle) {
            this.notificationTitle = notificationTitle;
        }

        public String getNotificationDescription() {
            return notificationDescription;
        }

        public void setNotificationDescription(String notificationDescription) {
            this.notificationDescription = notificationDescription;
        }

        public String getNotificationTypeId() {
            return notificationTypeId;
        }

        public void setNotificationTypeId(String notificationTypeId) {
            this.notificationTypeId = notificationTypeId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
