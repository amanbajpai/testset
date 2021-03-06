package com.keykeeper.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by akshaydashore on 28/8/18
 */
public class AssetsListResponseBean extends BaseResponse {

    @SerializedName("result")
    @Expose
    ArrayList<Result> result;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("asset_id")
        @Expose
        private String assetId;
        @SerializedName("asset_name")
        @Expose
        private String assetName;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("version_number")
        @Expose
        private String versionNumber;
        @SerializedName("model_number")
        @Expose
        private String modelNumber;
        @SerializedName("vin")
        @Expose
        private String vin;
        @SerializedName("asset_type")
        @Expose
        private Integer assetType;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("customer_mobile_number")
        @Expose
        private String customerMobileNumber;
        @SerializedName("customer_address")
        @Expose
        private String customerAddress;
        @SerializedName("asset_assgined_status")
        @Expose
        private String assetAssginedStatus;
        @SerializedName("qr_code_number")
        @Expose
        private String qrCodeNumber;
        @SerializedName("qr_img_name")
        @Expose
        private String qrImgName;
        @SerializedName("employee_name")
        @Expose
        private String employeeName;
        @SerializedName("employee_id")
        @Expose
        private String employeeId;

        @SerializedName("employee_chat_url")
        @Expose
        private String employeeChatUrl;

        public  boolean isSelected;


        public String getEmployeeChatUrl() {
            return employeeChatUrl;
        }

        public void setEmployeeChatUrl(String employeeChatUrl) {
            this.employeeChatUrl = employeeChatUrl;
        }

        @SerializedName("assigned_approved_or_decline_at")
        @Expose
        private String assigned_approved_or_decline_at;

        @SerializedName("assets_hold_remain_datetime")
        @Expose
        private String assetsHoldRemainDatetime;

        @SerializedName("assets_hold_remain_time")
        @Expose
        private String assets_hold_remain_time;

        /**
         * below two tags for asset list
         */
        @SerializedName("asset_employee_assigned_log_id")
        @Expose
        private Integer assetEmployeeAssignedLogId;

        @SerializedName("requested_by_employee_name")
        @Expose
        private String requestedByEmployeeName;
        /**
         * below tag for send received request
         *
         * @return
         */
        @SerializedName("assigned_request_at")
        @Expose
        private String assigned_request_at;


        public String getAssetsHoldRemainDatetime() {
            return assetsHoldRemainDatetime;
        }

        public void setAssetsHoldRemainDatetime(String assetsHoldRemainDatetime) {
            this.assetsHoldRemainDatetime = assetsHoldRemainDatetime;
        }

        public String getAssigned_approved_or_decline_at() {
            return assigned_approved_or_decline_at;
        }

        public void setAssigned_approved_or_decline_at(String assigned_approved_or_decline_at) {
            this.assigned_approved_or_decline_at = assigned_approved_or_decline_at;
        }

        public String getAssets_hold_remain_time() {
            return assets_hold_remain_time;
        }

        public void setAssets_hold_remain_time(String assets_hold_remain_time) {
            this.assets_hold_remain_time = assets_hold_remain_time;
        }

        public Integer getAssetType() {
            return assetType;
        }

        public void setAssetType(Integer assetType) {
            this.assetType = assetType;
        }



        public String getAssigned_request_at() {
            return assigned_request_at;
        }

        public void setAssigned_request_at(String assigned_request_at) {
            this.assigned_request_at = assigned_request_at;
        }

        public Integer getAssetEmployeeAssignedLogId() {
            return assetEmployeeAssignedLogId;
        }

        public void setAssetEmployeeAssignedLogId(Integer assetEmployeeAssignedLogId) {
            this.assetEmployeeAssignedLogId = assetEmployeeAssignedLogId;
        }

        public String getAssetId() {
            return assetId;
        }

        public void setAssetId(String assetId) {
            this.assetId = assetId;
        }

        public String getAssetName() {
            return assetName;
        }

        public void setAssetName(String assetName) {
            this.assetName = assetName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(String versionNumber) {
            this.versionNumber = versionNumber;
        }

        public String getModelNumber() {
            return modelNumber;
        }

        public void setModelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }


        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerMobileNumber() {
            return customerMobileNumber;
        }

        public void setCustomerMobileNumber(String customerMobileNumber) {
            this.customerMobileNumber = customerMobileNumber;
        }

        public String getCustomerAddress() {
            return customerAddress;
        }

        public void setCustomerAddress(String customerAddress) {
            this.customerAddress = customerAddress;
        }

        public String getAssetAssginedStatus() {
            return assetAssginedStatus;
        }

        public void setAssetAssginedStatus(String assetAssginedStatus) {
            this.assetAssginedStatus = assetAssginedStatus;
        }

        public String getQrCodeNumber() {
            return qrCodeNumber;
        }

        public void setQrCodeNumber(String qrCodeNumber) {
            this.qrCodeNumber = qrCodeNumber;
        }

        public String getQrImgName() {
            return qrImgName;
        }

        public void setQrImgName(String qrImgName) {
            this.qrImgName = qrImgName;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getRequestedByEmployeeName() {
            return requestedByEmployeeName;
        }

        public void setRequestedByEmployeeName(String requestedByEmployeeName) {
            this.requestedByEmployeeName = requestedByEmployeeName;
        }


    }

}
