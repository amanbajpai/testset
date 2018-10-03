package com.keykeeper.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nazimakauser on 19/9/18.
 */

public class HistoryResponseBean extends BaseResponse {
    @SerializedName("result")
    @Expose
    private ArrayList<ResponseResult> resultArray;

    public ArrayList<ResponseResult> getResultArray() {
        return resultArray;
    }

    public void setResultArray(ArrayList<ResponseResult> resultArray) {
        this.resultArray = resultArray;
    }

    public class ResponseResult implements Serializable {

        @SerializedName("asset_transaction_log_id")
        @Expose
        private int asset_transaction_log_id;

        @SerializedName("transaction_description")
        @Expose
        private String transaction_description;

        @SerializedName("created_at")
        @Expose
        private String created_at;

        public int getAsset_transaction_log_id() {
            return asset_transaction_log_id;
        }

        public void setAsset_transaction_log_id(int asset_transaction_log_id) {
            this.asset_transaction_log_id = asset_transaction_log_id;
        }

        public String getTransaction_description() {
            return transaction_description;
        }

        public void setTransaction_description(String transaction_description) {
            this.transaction_description = transaction_description;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
