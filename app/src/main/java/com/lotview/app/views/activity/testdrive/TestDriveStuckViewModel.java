package com.lotview.app.views.activity.testdrive;

import android.arch.lifecycle.MutableLiveData;

import com.lotview.app.application.KeyKeepApplication;
import com.lotview.app.model.bean.CheckIfAnyTestDriveResponseBean;
import com.lotview.app.model.bean.TestDriveResponseBean;
import com.lotview.app.netcom.retrofit.RetrofitHolder;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Connectivity;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankurrawal on 19/9/18.
 */

public class TestDriveStuckViewModel extends BaseViewModel {

    MutableLiveData<Integer> validator = new MutableLiveData<>();
    MutableLiveData<TestDriveResponseBean> response_testdrive_stop = new MutableLiveData<>();
    MutableLiveData<CheckIfAnyTestDriveResponseBean> response_check_ifany_testdrive = new MutableLiveData<>();


    public void doStopTestDrive(String emp_id, int asset_id, String start_latitude, String start_logitude,
                                String start_date_time, String start_date_time_utc, String test_drive_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<TestDriveResponseBean> call = RetrofitHolder.getService().doStopTestDrive(
                KeyKeepApplication.getBaseEntity(true),
                asset_id, start_latitude, start_logitude, start_date_time, start_date_time_utc, test_drive_id);

        call.enqueue(new Callback<TestDriveResponseBean>() {

            @Override
            public void onResponse(Call<TestDriveResponseBean> call, Response<TestDriveResponseBean> response) {
                Utils.hideProgressDialog();
                TestDriveResponseBean bean = response.body();
                response_testdrive_stop.setValue(bean);
            }

            @Override
            public void onFailure(Call<TestDriveResponseBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }


    public void doCheckIfTestDriveIsRuning(String emp_id) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
            return;
        }

        Call<CheckIfAnyTestDriveResponseBean> call = RetrofitHolder.getService().doCheckifAnyTestDriveRunning(
                KeyKeepApplication.getBaseEntity(true), emp_id);

        call.enqueue(new Callback<CheckIfAnyTestDriveResponseBean>() {

            @Override
            public void onResponse(Call<CheckIfAnyTestDriveResponseBean> call, Response<CheckIfAnyTestDriveResponseBean> response) {
                Utils.hideProgressDialog();
                CheckIfAnyTestDriveResponseBean bean = response.body();
                response_check_ifany_testdrive.setValue(bean);
            }

            @Override
            public void onFailure(Call<CheckIfAnyTestDriveResponseBean> call, Throwable t) {
                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
            }
        });

    }


}
