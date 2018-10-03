package com.keykeeper.app.views.activity.keyMap;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.keykeeper.app.model.bean.AssetLocationResponseBean;
import com.keykeeper.app.model.bean.TrackLocationRequestEntity;
import com.keykeeper.app.netcom.Keys;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.custom_view.CustomActionBar;

public class KeyOnMapActivity extends BaseActivity implements DialogClickListener {

    SupportMapFragment mapFragment;
    ActivityKeyOnMapBinding keyOnMapBinding;
    KeyOnMapViewModel keyOnMapViewModel;
    int assetId = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        setCustomActionBar();
        initializeViews();

    }

    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.asset_detail), true, false, false, true, this);
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

        assetId = Integer.parseInt(getIntent().getStringExtra(AppUtils.ASSET_ID));

        getLatLon();

    }

    private void getLatLon() {
        Utils.showProgressDialog(context, getString(R.string.loading));

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
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(emp_lat, emp_long))
                        .title(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

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

                getLatLon();
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
            if (bean.getCode().equals(AppUtils.STATUS_SUCCESS)) {
                showMarker(bean.getResult().getEmp_lat(), bean.getResult().getEmp_long(), bean.getResult().getLocation());
            }

        }
    };

    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}
