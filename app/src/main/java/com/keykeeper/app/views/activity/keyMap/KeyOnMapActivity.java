package com.keykeeper.app.views.activity.keyMap;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keykeeper.app.R;
import com.keykeeper.app.databinding.ActivityKeyOnMapBinding;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.bean.AssetDetailBean;
import com.keykeeper.app.model.bean.AssetLocationResponseBean;
import com.keykeeper.app.model.bean.TrackLocationRequestEntity;
import com.keykeeper.app.netcom.Keys;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

public class KeyOnMapActivity extends BaseActivity implements DialogClickListener {

    private static final long NODE_CONNECTION_CHECK = 30000;
    SupportMapFragment mapFragment;
    ActivityKeyOnMapBinding keyOnMapBinding;
    KeyOnMapViewModel keyOnMapViewModel;
    int assetId = 0;
    private Context context;
    private CountDownTimer countDownTimer;
    public static boolean isDataLoading;

    AssetDetailBean.Result assetBean;
    private GoogleMap googleMap;
    public int MAP_TYPE = GoogleMap.MAP_TYPE_SATELLITE;
    boolean isSatelite = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setCustomActionBar();
        initializeViews();

    }

    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.asset_map), true, false, false, true, this);
    }


    @Override
    public void initializeViews() {
        keyOnMapBinding = DataBindingUtil.setContentView(this, R.layout.activity_key_on_map);
        keyOnMapViewModel = ViewModelProviders.of(this).get(KeyOnMapViewModel.class);
        keyOnMapBinding.setViewModel(keyOnMapViewModel);

        keyOnMapViewModel.validator.observe(this, observer);
        keyOnMapViewModel.response_validator.observe(this, response_observer);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assetId = Utils.validateStringToInt(getIntent().getStringExtra(AppUtils.ASSET_ID));

        getLatLon();
        countdownForConnection();
        assetBean = (AssetDetailBean.Result) getIntent().getSerializableExtra(AppUtils.ASSET_BEAN);
        keyOnMapBinding.satelliteTv.setOnClickListener(this);
        keyOnMapBinding.standardTv.setOnClickListener(this);

    }

    private void getLatLon() {

        isDataLoading = true;

//      Utils.showProgressDialog(context, getString(R.string.loading));

        TrackLocationRequestEntity trackLocationRequestEntity = new TrackLocationRequestEntity();
        trackLocationRequestEntity.setEmp_current_lat(AppSharedPrefs.getLatitude());
        trackLocationRequestEntity.setEmp_current_long(AppSharedPrefs.getLongitude());

        trackLocationRequestEntity.setApi_key(Keys.API_KEY);
        trackLocationRequestEntity.setDevice_id(Utils.getDeviceID());
        trackLocationRequestEntity.setDevice_type(Keys.TYPE_ANDROID);
        trackLocationRequestEntity.setEmployee_id(Integer.valueOf(AppSharedPrefs.getInstance(this).getEmployeeID()));
        trackLocationRequestEntity.setCompany_id(Integer.valueOf(AppSharedPrefs.getInstance(this).getCompanyID()));

        if (AppSharedPrefs.getInstance(this).getPushDeviceToken() != null && AppSharedPrefs.getInstance(this).getPushDeviceToken().trim().length() > 0) {
            trackLocationRequestEntity.setDevice_token(AppSharedPrefs.getInstance(this).getPushDeviceToken());
        } else {
            trackLocationRequestEntity.setDevice_token("");
        }
        trackLocationRequestEntity.setToken_type(Keys.TOKEN_TYPE);
        trackLocationRequestEntity.setAccess_token(AppSharedPrefs.getInstance(this).getAccessToken());

        keyOnMapViewModel.getLatLong(assetId, keyOnMapBinding, trackLocationRequestEntity);
    }


    private void showMarker(double emp_lat, double emp_long, String location) {

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {

                googleMap = gMap;
                googleMap.clear();
                googleMap.setMapType(MAP_TYPE);
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(emp_lat, emp_long))
                        .title(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                CustomInfoWindowGoogleMap adapter = new CustomInfoWindowGoogleMap(context, location);
                googleMap.setInfoWindowAdapter(adapter);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(emp_lat, emp_long), 14));

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.left_iv:
                finish();
                break;

            case R.id.refresh_iv:
                if (!isDataLoading) {
                    getLatLon();
                }
                break;


            case R.id.satellite_tv:
                if (googleMap == null) {
                    return;
                }
                MAP_TYPE = GoogleMap.MAP_TYPE_SATELLITE;
                googleMap.setMapType(MAP_TYPE);
                keyOnMapBinding.satelliteTv.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.send_request_selector));
                keyOnMapBinding.standardTv.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.received_request_deselector));
                keyOnMapBinding.standardTv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                keyOnMapBinding.satelliteTv.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;


            case R.id.standard_tv:
                if (googleMap == null) {
                    return;
                }
                MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
                googleMap.setMapType(MAP_TYPE);
                keyOnMapBinding.satelliteTv.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.send_request_deselector));
                keyOnMapBinding.standardTv.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.received_request_selector));
                keyOnMapBinding.satelliteTv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                keyOnMapBinding.standardTv.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;

        }

    }

    Observer<Integer> observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {

            Utils.hideProgressDialog();
            switch (value) {
                case AppUtils.NO_INTERNET:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(keyOnMapBinding, getString(R.string.internet_connection));
                    break;

                case AppUtils.SERVER_ERROR:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(keyOnMapBinding, getString(R.string.server_error));
                    break;
            }
        }
    };

    Observer<AssetLocationResponseBean> response_observer = new Observer<AssetLocationResponseBean>() {
        @Override
        public void onChanged(@Nullable AssetLocationResponseBean bean) {
            Utils.hideProgressDialog();
            if (bean == null) {
                Utils.showAlert(context, "", getString(R.string.server_error), getString(R.string.ok), "", AppUtils.dialog_ok_click, KeyOnMapActivity.this);
                return;
            }

            if (bean.getCode().equals(AppUtils.STATUS_FAIL)) {
                Utils.showAlert(context, "", bean.getMessage(), getString(R.string.ok), "", AppUtils.dialog_ok_click, KeyOnMapActivity.this);
                return;
            }

            String marker_data = "";
            if (assetBean.getAssetType() == Integer.valueOf(AppUtils.ASSET_CUSTOMER)) {
                marker_data += "Tag number: " + Utils.validateValue(assetBean.getAssetName()) + "\n";
            } else {
                marker_data += "Stock number: " + Utils.validateValue(assetBean.getAssetName()) + "\n";
            }

            marker_data += "Location: " + Utils.validateValue(bean.getResult().getLocation()) + "\n";


            if (Utils.validateInteger(assetBean.getAssetAssginedStatus()).equals("1")) {

                String mEmp_id = AppSharedPrefs.getEmployeeID();
                if (Utils.validateStringValue(assetBean.getEmployeeName()).equals("") ||
                        Utils.validateInteger(assetBean.getEmployeeId()).equals(mEmp_id)) {

                    marker_data += "Owner: You";

                } else {
                    marker_data += "Owner: " + Utils.validateValue(assetBean.getEmployeeName());
                }

            } else {
                marker_data += "Availability: Available";
            }
            marker_data = marker_data.replace("null", "");

            if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                showMarker(bean.getResult().getEmp_lat(), bean.getResult().getEmp_long(), marker_data);
            }

        }
    };

    @Override
    public void onDialogClick(int which, int requestCode) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void countdownForConnection() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(30000, NODE_CONNECTION_CHECK) {

            public void onTick(long millisUntilFinished) {
                if (!isDataLoading) {
                    getLatLon();
                }
            }

            public void onFinish() {
                countDownTimer.start();
            }

        }.start();

    }


}
