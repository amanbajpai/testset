package com.lotview.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by akshaydashore on 28/8/18
 */

public class TrackLocationBaseResponse extends BaseResponse{

    @SerializedName("result")
    @Expose
    private ArrayList<ResponseResult>resultArray;

    public ArrayList<ResponseResult> getResultArray() {
        return resultArray;
    }

    public void setResultArray(ArrayList<ResponseResult> resultArray) {
        this.resultArray = resultArray;
    }

    public class ResponseResult implements Serializable {

        @SerializedName("asset_id")
        @Expose
        private int asset_id;
        @SerializedName("asset_name")
        @Expose
        private String asset_name;
        @SerializedName("asset_type")
        @Expose
        private String asset_type;

        public int getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(int asset_id) {
            this.asset_id = asset_id;
        }

        public String getAsset_name() {
            return asset_name;
        }

        public void setAsset_name(String asset_name) {
            this.asset_name = asset_name;
        }

        public String getAsset_type() {
            return asset_type;
        }

        public void setAsset_type(String asset_type) {
            this.asset_type = asset_type;
        }
    }

}
