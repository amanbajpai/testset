package com.lotview.app.views.activity.keyMap;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lotview.app.R;
import com.lotview.app.databinding.ActivityKeyOnMapBinding;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.assetDetail.AssetDetailActivity;
import com.lotview.app.views.activity.assetDetail.AssetDetailViewModel;
import com.lotview.app.views.base.BaseActivity;
import com.lotview.app.views.custom_view.CustomActionBar;

public class KeyOnMapActivity extends BaseActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    ActivityKeyOnMapBinding keyOnMapBinding;
    KeyOnMapViewModel keyOnMapViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCustomActionBar();
        initializeViews();

    }

    @Override
    public void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar(getString(R.string.asset_detail), true, false, this);
    }


    @Override
    public void initializeViews() {
        keyOnMapBinding = DataBindingUtil.setContentView(this, R.layout.activity_key_on_map);
        keyOnMapViewModel = ViewModelProviders.of(this).get(KeyOnMapViewModel.class);
        keyOnMapBinding.setViewModel(keyOnMapViewModel);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        showMarker();

    }


    private void showMarker() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.4233438, -122.0728817))
                        .title("LinkedIn")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 10));
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.4233438, -122.0728817))
                .title("LinkedIn")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 10));
    }

    @Override
    public void onClick(View v) {

    }
}
