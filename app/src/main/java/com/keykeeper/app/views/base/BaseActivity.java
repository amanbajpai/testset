package com.keykeeper.app.views.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.login.LoginActivity;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;


/**
 * Created by ankurrawal
 */
abstract public class BaseActivity extends AppCompatActivity implements View.OnClickListener ,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Context context;
    protected boolean isFirstCheckForLogin = true;
    private GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    private String TAG = getClass().getName();

    /**
     * this method is responsible to initialize the views
     */
    public abstract void initializeViews();


    /**
     * init custom action bar override it on need
     */
    public void setCustomActionBar() {

    }

    private SmartLocation.LocationControl location_control;
    private Location mlocation = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

//        initLocationClient();


    }

    private void initLocationClient() {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes

        // mLocationRequest.setInterval(trackLocationInterval);
//        mLocationRequest.setFastestInterval(trackLocationInterval);
        mLocationRequest.setPriority(priority);
        mLocationRequest.setSmallestDisplacement(3);
        mLocationRequest.setInterval(10000);
//        mLocationRequest.setInterval(5000L);
        mLocationClient.connect();


    }

    protected void getLocation() {
        try {

            if (Utils.isGooglePlayServicesAvailable(context)){
                Utils.showToast(context,"google play service not available");
                return;
            }

            if (Utils.isGpsEnable(context)) {
                if (Utils.checkPermissions(this, AppUtils.LOCATION_PERMISSIONS)) {
                    fetchLocation();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(AppUtils.LOCATION_PERMISSIONS, AppUtils.REQUEST_CODE_LOCATION);
                    }
                }
            } else {

                if (!(((Activity) context) instanceof LoginActivity) || isFirstCheckForLogin) {
                    isFirstCheckForLogin = false;
                    displayLocationSettingsRequest();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(boolean addBackStack, Fragment fragment, int container) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        String stack = fragment.getClass().getName();

        if (addBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

        //Scroll Fragment
    }


    @Override
    protected void onResume() {
        super.onResume();
        KeyKeepApplication.activityResumed();

//        if (Utils.isMockLocationEnabled(this, mlocation)) {
//            isMockLocationEnabled = true;
//            Utils.showAlert(context, "", getString(R.string.turn_of_mock_location_dialog_text), "OK", "", AppUtils.dialog_ok_mock_location, this);
//        } else {
//            isMockLocationEnabled = false;
//        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        KeyKeepApplication.activityPaused();
    }

    protected void fetchLocation() {

        // Utils.showProgressDialog(context, "Fetching location...");
        LocationParams.Builder builder = new LocationParams.Builder();
        builder.setAccuracy(LocationAccuracy.HIGH);
        builder.setDistance(5); // in Meteres
        builder.setInterval(1000);
        LocationParams params = builder.build();

        location_control = SmartLocation.with(context).location().config(params);

        location_control.start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                mlocation = location;
                if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                    // Utils.hideProgressDialog();
                    String lat = location.getLatitude() + "";
                    String lng = location.getLongitude() + "";
                    Log.e(" onLocationUpdated: ", lat + " " + lng);
                    AppSharedPrefs.setLatitude(lat);
                    AppSharedPrefs.setLongitude(lng);
                    AppSharedPrefs.setSpeed(location.getSpeed() + "");

                } else {
                    fetchLocation();
                }
            }
        });

    }

    private void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("tag", "All location settings are satisfied.");
                        if (Utils.checkPermissions(((Activity) context), AppUtils.LOCATION_PERMISSIONS)) {
                            fetchLocation();
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(AppUtils.LOCATION_PERMISSIONS, AppUtils.REQUEST_CODE_LOCATION);
                            }
                        }
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("tag", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        if (!(((Activity) context) instanceof LoginActivity) || isFirstCheckForLogin) {
                            isFirstCheckForLogin = false;
                            try {
                                status.startResolutionForResult(((Activity) context), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }

                        }


                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(((Activity) context), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("tag", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("tag", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    protected void stopLocatonUpdates() {
        try {
            if (location_control != null) {
                location_control.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (location_control != null) {
                location_control.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mLocationClient != null){
//            mLocationClient.disconnect();
//        }
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppUtils.REQUEST_CODE_LOCATION && Utils.onRequestPermissionsResult(permissions, grantResults)) {
            fetchLocation();
        } else {

            // Utils.showSnackBar(context, null, getString(R.string.allow_location_permission));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(BaseActivity.class.getCanonicalName(), "User agreed to make required location settings changes.");
                        if (Utils.checkPermissions(((Activity) context), AppUtils.LOCATION_PERMISSIONS)) {
                            fetchLocation();
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(AppUtils.LOCATION_PERMISSIONS, AppUtils.REQUEST_CODE_LOCATION);
                            }
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(BaseActivity.class.getCanonicalName(), "User chose not to make required location settings changes.");
                        break;
                }
                break;

        }

    }


    /*
   * LOCATION CALLBACKS
   */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d(TAG, "== location != null");
            handleUpdatedLocation(location);
        }
    }


    /**
     * Migrate smart location service
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("", "Failed to connect to Google API");
    }


    private void handleUpdatedLocation(Location location) {
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            String lat = location.getLatitude() + "";
            String lng = location.getLongitude() + "";
            String speed = location.getSpeed() + "";
            Log.e("Accuracy: ", "" + location.getAccuracy());
                    Log.e(lat + " onLocationUpdated: ", lng + " <<" + " Speed: " + speed);

                    AppSharedPrefs.setLatitude(lat);
                    AppSharedPrefs.setLongitude(lng);
                    AppSharedPrefs.setSpeed(location.getSpeed() + "");

            try {
                AppSharedPrefs.setIsLocationFromMock(location.isFromMockProvider());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
