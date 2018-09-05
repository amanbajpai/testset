package com.lotview.app.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PushEventData implements Serializable {

    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("event_type")
    @Expose
    private Integer eventType;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
