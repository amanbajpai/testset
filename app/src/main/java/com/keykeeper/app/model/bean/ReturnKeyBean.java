package com.keykeeper.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akshaydashore on 16/10/18
 */
public class ReturnKeyBean {

    @SerializedName("submit_user_type")
    @Expose
    String submitUserType;

    @SerializedName("qr_code_number")
    @Expose
    String qrCodeNumber;

    @SerializedName("keybox_qr_code_number")
    @Expose
    String keyboxQrCodeNumber;

    public String getSubmitUserType() {
        return submitUserType;
    }

    public void setSubmitUserType(String submitUserType) {
        this.submitUserType = submitUserType;
    }

    public String getQrCodeNumber() {
        return qrCodeNumber;
    }

    public void setQrCodeNumber(String qrCodeNumber) {
        this.qrCodeNumber = qrCodeNumber;
    }

    public String getKeyboxQrCodeNumber() {
        return keyboxQrCodeNumber;
    }

    public void setKeyboxQrCodeNumber(String keyboxQrCodeNumber) {
        this.keyboxQrCodeNumber = keyboxQrCodeNumber;
    }

}
