package com.keykeeper.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ankurrawal on 18/9/18.
 */

public class AssetLocationResponseBean extends BaseResponse {


    @SerializedName("result")
    @Expose
    private ResponseResult result;

    public ResponseResult getResult() {
        return result;
    }

    public void setResult(ResponseResult result) {
        this.result = result;
    }

    public class ResponseResult implements Serializable {

        @SerializedName("emp_lat")
        @Expose
        private double emp_lat;

        @SerializedName("emp_long")
        @Expose
        private double emp_long;

        @SerializedName("location")
        @Expose
        private String location;

        public double getEmp_lat() {
            return emp_lat;
        }

        public void setEmp_lat(double emp_lat) {
            this.emp_lat = emp_lat;
        }

        public double getEmp_long() {
            return emp_long;
        }

        public void setEmp_long(double emp_long) {
            this.emp_long = emp_long;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

}
