package com.keykeep.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.keykeep.app.views.base.BaseFragment;

/**
 * Created by akshaydashore on 29/8/18
 */

public class AssetDetailBean extends BaseResponse {


    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("asset_id")
        @Expose
        private Integer assetId;
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
        private Integer assetAssginedStatus;
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
        private Integer employeeId;

        public Integer getAssetId() {
            return assetId;
        }

        public void setAssetId(Integer assetId) {
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

        public Integer getAssetType() {
            return assetType;
        }

        public void setAssetType(Integer assetType) {
            this.assetType = assetType;
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

        public Integer getAssetAssginedStatus() {
            return assetAssginedStatus;
        }

        public void setAssetAssginedStatus(Integer assetAssginedStatus) {
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

        public Integer getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Integer employeeId) {
            this.employeeId = employeeId;
        }
    }
}
