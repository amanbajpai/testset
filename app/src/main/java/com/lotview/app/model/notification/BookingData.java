package com.lotview.app.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingData implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("event_type")
    @Expose
    private Integer eventType;
    @SerializedName("booking_requests_status")
    @Expose
    private Integer bookingRequestsStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getBookingRequestsStatus() {
        return bookingRequestsStatus;
    }

    public void setBookingRequestsStatus(Integer bookingRequestsStatus) {
        this.bookingRequestsStatus = bookingRequestsStatus;
    }

}
