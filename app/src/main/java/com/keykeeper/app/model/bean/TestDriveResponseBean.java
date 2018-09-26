package com.keykeeper.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankurrawal on 18/9/18.
 */

public class TestDriveResponseBean extends BaseResponse {

    @SerializedName("result")
    @Expose
    TestDriveResponseBean.Result result;

    public TestDriveResponseBean.Result getResult() {
        return result;
    }

    public void setResult(TestDriveResponseBean.Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("asset_employee_test_drive_id")
        @Expose
        private String assetEmployeeTestDriveId;

        public String getAssetEmployeeTestDriveId() {
            return assetEmployeeTestDriveId;
        }

        public void setAssetEmployeeTestDriveId(String assetEmployeeTestDriveId) {
            this.assetEmployeeTestDriveId = assetEmployeeTestDriveId;
        }
    }
}
