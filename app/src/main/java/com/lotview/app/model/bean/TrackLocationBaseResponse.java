package com.lotview.app.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
}
