package com.keykeeper.app.model.bean;

/**
 * Created by nazimakauser on 20/9/18.
 */

public class TrackLocationRequestEntity extends BaseRequestEntity {

    private String emp_current_lat;
    private String emp_current_long;
    private String emp_current_speed;


    public String getEmp_current_lat() {
        return emp_current_lat;
    }

    public void setEmp_current_lat(String emp_current_lat) {
        this.emp_current_lat = emp_current_lat;
    }

    public String getEmp_current_long() {
        return emp_current_long;
    }

    public void setEmp_current_long(String emp_current_long) {
        this.emp_current_long = emp_current_long;
    }

    public String getEmp_current_speed() {
        return emp_current_speed;
    }

    public void setEmp_current_speed(String emp_current_speed) {
        this.emp_current_speed = emp_current_speed;
    }
}
