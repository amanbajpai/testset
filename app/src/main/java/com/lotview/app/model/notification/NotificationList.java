package com.lotview.app.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by priyankatiwari on 11/10/17.
 */
public class NotificationList {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("msg_type")
    @Expose
    private String msgType;
    @SerializedName("is_read")
    @Expose
    private Integer isRead;
    @SerializedName("is_archive")
    @Expose
    private Integer isArchive;
    @SerializedName("app_type")
    @Expose
    private Integer appType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("push_type")
    @Expose
    private Integer pushType;

    @SerializedName("push_data")
    @Expose
    private PushData pushData;

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

//    public JSONObject getPushData() {
//        return pushData;
//    }
//
//    public void setPushData(JSONObject pushData) {
//        this.pushData = pushData;
//    }

    public PushData getPushData() {
        return pushData;
    }

    public void setPushData(PushData pushData) {
        this.pushData = pushData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Integer isArchive) {
        this.isArchive = isArchive;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
