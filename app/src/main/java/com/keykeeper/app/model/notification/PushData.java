package com.keykeeper.app.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PushData implements Serializable {

    /*"push_type": "1",
  "additional_data": "{\"company_id\":6,\"employee_id\":3,\"asset_id\":15}",
  "body": "BMW CAR key request decline by dealership.",
  "icon": "myicon",
  "sound": "mySound",
  "title": "BMW CAR key request decline."*/


    @SerializedName("push_type")
    @Expose
    public int pushType;

    @SerializedName("additional_data")
    @Expose
    public PushAdditionalData additionalData;

    @SerializedName("body")
    @Expose
    public String body;

    @SerializedName("icon")
    @Expose
    public String icon;

    @SerializedName("sound")
    @Expose
    public String sound;

    @SerializedName("title")
    @Expose
    private String title;

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public PushAdditionalData getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(PushAdditionalData additionalData) {
        this.additionalData = additionalData;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}