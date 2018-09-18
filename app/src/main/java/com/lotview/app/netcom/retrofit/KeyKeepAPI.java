package com.lotview.app.netcom.retrofit;

import com.lotview.app.model.bean.AssetDetailBean;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.model.bean.BaseRequestEntity;
import com.lotview.app.model.bean.BaseResponse;
import com.lotview.app.model.bean.ChangePasswordBean;
import com.lotview.app.model.bean.ForgotPasswordResponseBean;
import com.lotview.app.model.bean.LoginResponseBean;
import com.lotview.app.model.bean.NotificationsResponseBean;
import com.lotview.app.netcom.Keys;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ankurrawal on 22/8/18.
 */

public interface KeyKeepAPI {

    @POST(Config.LOGIN_URL)
    public Call<LoginResponseBean> doLogin(@Body BaseRequestEntity baseRequestEntity, @Query(Keys.EMAIL) String email,
                                           @Query(Keys.PASSWORD) String password);

    @POST(Config.FORGOT_PASSWORD_URL)
    public Call<ForgotPasswordResponseBean> forgotPassword(@Body BaseRequestEntity baseRequestEntity, @Query(Keys.EMAIL) String email);

    @POST(Config.ASSET_LIST_URL)
    public Call<AssetsListResponseBean> getAssetsList(@Body BaseRequestEntity baseRequestEntity, @Query(Keys.EMPLOYEE_ID) String employeeId, @Query(Keys.IS_MY_ASSETS) String isMyAsset,
                                                      @Query(Keys.TEXT_TO_SEARCH) String textToSearch);

    @POST(Config.CHANGE_PASSWORD_URL)
    Call<ChangePasswordBean> doChangePassword(
            @Body BaseRequestEntity baseRequestEntity,
            @Query(Keys.OLD_PASSWORD) String oldPassword,
            @Query(Keys.NEW_PASSWORD) String newPassword,
            @Query(Keys.CONFIRM_NEW_PASSWORD) String confirmPassword,
            @Query(Keys.EMPLOYEE_ID) String employeeId
    );

    @POST(Config.ASSET_DETAIL)
    Call<AssetDetailBean> getAssetDetail(
            @Body BaseRequestEntity baseRequestEntity,
            @Query(Keys.EMPLOYEE_ID) String empliyeeId,
            @Query(Keys.QR_CODE_NUMBER) String qr_code_number
    );


    @POST(Config.KEEP_ASSET_REQUEST)
    Call<AssetDetailBean> keepAssetRequest(
            @Body BaseRequestEntity baseRequestEntity,
            @Query(Keys.EMPLOYEE_ID) String employeeId,
            @Query(Keys.QR_CODE_NUMBER) String qr_code_number,
            @Query(Keys.EMPLOYEE_LATITUDE) String emp_latitude,
            @Query(Keys.EMPLOYEE_LONGITUDE) String emp_longtitude

    );


    @POST(Config.ASSET_TRANSFER_REQUEST)
    Call<AssetDetailBean> sendTransferRequest(
            @Body BaseRequestEntity baseRequestEntity,
            @Query(Keys.EMPLOYEE_ID) String employeeId,
            @Query(Keys.QR_CODE_NUMBER) String qr_code_number
    );


    @POST(Config.ASSET_HANDOVER_REQUEST)
    Call<AssetDetailBean> sendHandoverRequest(
            @Body BaseRequestEntity baseRequestEntity,
            @Query(Keys.EMPLOYEE_ID) String employeeId,
            @Query(Keys.QR_CODE_NUMBER) String qr_code_number,
            @Query(Keys.SUBMIT_USER_TYPE) String submit_user_type,
            @Query(Keys.EMPLOYEE_LATITUDE) String emp_latitude,
            @Query(Keys.EMPLOYEE_LONGITUDE) String emp_longtitude
    );


    @POST(Config.ASSET_PENDING_SEND_REQUEST)
    Call<AssetsListResponseBean> getAssetPendingSendRequest(@Body BaseRequestEntity baseRequestEntity,
                                                            @Query(Keys.EMPLOYEE_ID) String employeeId
    );


    @POST(Config.ASSET_PENDING_RECIEVE_REQUEST)
    Call<AssetsListResponseBean> getAssetPendingRecieveRequest(@Body BaseRequestEntity baseRequestEntity,
                                                               @Query(Keys.EMPLOYEE_ID) String employeeId
    );


    @POST(Config.CANCEL_ASSET_REQ_URL)
    Call<BaseResponse> cancelAssetRequest(@Body BaseRequestEntity baseEntity,
                                          @Query(Keys.EMPLOYEE_ID) String emp_id,
                                          @Query(Keys.REQ_ID) int req_id,
                                          @Query(Keys.EMPLOYEE_LATITUDE) String emp_latitude,
                                          @Query(Keys.EMPLOYEE_LONGITUDE) String emp_longtitude
    );


    @POST(Config.APPROVE_ASSET_REQ_URL)
    Call<BaseResponse> approveAssetRequest(@Body BaseRequestEntity baseEntity,
                                           @Query(Keys.EMPLOYEE_ID) String emp_id,
                                           @Query(Keys.REQ_ID) int req_id,
                                           @Query(Keys.EMPLOYEE_LATITUDE) String emp_latitude,
                                           @Query(Keys.EMPLOYEE_LONGITUDE) String emp_longtitude
    );


    @POST(Config.NOTIFICATIONS_REQ_URL)
    Call<NotificationsResponseBean> getNotificationsRequest(@Body BaseRequestEntity baseEntity,
                                                            @Query(Keys.EMPLOYEE_ID) String emp_id);

    @POST(Config.NOTIFICATION_ARCHICVE)
    Call<BaseResponse> clearAllNotification(@Body BaseRequestEntity baseEntity,
                                            @Query(Keys.EMPLOYEE_ID) String emp_id);


    @POST(Config.NOTIFICATION_ENABLE_URL)
    Call<BaseResponse> enableNotifications(@Body BaseRequestEntity baseEntity,
                                           @Query(Keys.EMPLOYEE_ID) String emp_id,
                                           @Query(Keys.STATUS) String status

    );


    @POST(Config.LOGOUT_REQ_URL)
    Call<BaseResponse> doLogout(@Body BaseRequestEntity baseEntity,
                                @Query(Keys.EMPLOYEE_ID) String emp_id);

    @POST(Config.START_TEST_DRIVE_URL)
    Call<BaseResponse> doStartTestDrive(@Body BaseRequestEntity baseEntity,
                                        @Query(Keys.EMPLOYEE_ID) String emp_id,
                                        @Query(Keys.ASSET_ID) int assetId,
                                        @Query(Keys.TEST_DRIVE_START_LATITUDE) String start_latitude,
                                        @Query(Keys.TEST_DRIVE_START_LONGITUDE) String start_longitude,
                                        @Query(Keys.TEST_DRIVE_START_DATETIME) String start_date_time,
                                        @Query(Keys.TEST_DRIVE_START_DATETIME_UTC) String start_date_time_utc
    );

//    asset_employee_test_drive_id:1
//    end_date_time:2018-09-18 14:30:30
//    end_latitude:28.25486
//    end_longitude:76.32569

    @POST(Config.START_TEST_DRIVE_URL)
    Call<BaseResponse> doStopTestDrive(@Body BaseRequestEntity baseEntity,
                                       @Query(Keys.EMPLOYEE_ID) String emp_id,
                                       @Query(Keys.ASSET_ID) int assetId,
                                       @Query(Keys.TEST_DRIVE_START_LATITUDE) String start_latitude,
                                       @Query(Keys.TEST_DRIVE_START_LONGITUDE) String start_longitude,
                                       @Query(Keys.TEST_DRIVE_START_DATETIME) String start_date_time,
                                       @Query(Keys.TEST_DRIVE_START_DATETIME_UTC) String start_date_time_utc
    );


    @POST(Config.EMPLOYEE_TRACKER_URL)
    Call<BaseResponse> trackeEmployee(@Body BaseRequestEntity baseEntity,
                                      @Query(Keys.EMPLOYEE_ID) String emp_id
    );


}
