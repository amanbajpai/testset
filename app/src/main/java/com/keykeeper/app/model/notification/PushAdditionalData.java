package com.keykeeper.app.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ankurrawal on 5/9/18.
 */

public class PushAdditionalData implements Serializable {

    @SerializedName("company_id")
    @Expose
    private int companyId;

    @SerializedName("employee_id")
    @Expose
    private int employeeId;

    @SerializedName("asset_id")
    @Expose
    private int assetId;

    @SerializedName("chat_user_url")
    @Expose
    private String chatUserUrl;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getChatUserUrl() {
        return chatUserUrl;
    }

    public void setChatUserUrl(String chatUserUrl) {
        this.chatUserUrl = chatUserUrl;
    }
}
