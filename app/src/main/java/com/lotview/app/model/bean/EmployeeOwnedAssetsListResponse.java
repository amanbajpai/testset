package com.lotview.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ankurrawal on 19/9/18.
 */

public class EmployeeOwnedAssetsListResponse extends BaseResponse implements Serializable {

    @SerializedName("result")
    @Expose
    public ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public class Result {
        private String asset_id;
        private String asset_name;
        private String asset_type;

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
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

