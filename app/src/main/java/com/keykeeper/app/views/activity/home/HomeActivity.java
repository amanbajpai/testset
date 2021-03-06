
package com.keykeeper.app.views.activity.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeeper.app.R;
import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.databinding.HomeLayoutBinding;
import com.keykeeper.app.firebase.firebase.DeleteTokenService;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.LeftMenuDrawerItems;
import com.keykeeper.app.model.bean.BaseResponse;
import com.keykeeper.app.model.bean.EmployeeOwnedAssetsListResponse;
import com.keykeeper.app.model.notification.PushData;
import com.keykeeper.app.netcom.Keys;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.AssetListActivity;
import com.keykeeper.app.views.activity.chat.ChatActivity;
import com.keykeeper.app.views.activity.login.LoginActivity;
import com.keykeeper.app.views.activity.testdrive.TestDriveStuckActivity;
import com.keykeeper.app.views.adapter.LeftDrawerListAdapter;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.fragment.asset_request_fragment.AssetRequestFragment;
import com.keykeeper.app.views.fragment.home.HomeFragment;
import com.keykeeper.app.views.fragment.notifications.NotificationFragment;
import com.keykeeper.app.views.fragment.setting.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;


/**
 * Created by ankurrawal
 */
public class HomeActivity extends BaseActivity implements LeftDrawerListAdapter.OnItemClickListener, DialogClickListener {

    private Context context;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<LeftMenuDrawerItems> leftMenuDrawerItemses = new ArrayList<>();
    private XRecyclerView recyclerView;
    private LeftDrawerListAdapter leftDrawerListAdapter;
    View drawerView;
    private String[] menuItemNames;
    int previousPosition = -1;
    Toolbar toolbar;
    private Fragment fragment;
    public TextView title_tv;
    private static boolean activityVisible;
    private TextView tvProfileUserName;
    private TextView icon_right;

    HomeViewModel viewModel;
    HomeLayoutBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.home_layout);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.validator.observe(this, observer);
        viewModel.response_allassets_owned.observe(this, responseAssetsOwnedCurrently);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        initializeViews();
        setupDrawer();
        setView();
        Utils.replaceFragment(HomeActivity.this, new HomeFragment());
        onNewIntent(getIntent());
        if (Utils.checkPermissions(this, AppUtils.LOCATION_PERMISSIONS)) {
            if (!Utils.isGpsEnable(context)) {
                displayLocationSettingsRequest();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(AppUtils.LOCATION_PERMISSIONS, AppUtils.REQUEST_CODE_LOCATION);
            }
        }

    }


    private void setView() {
    }


    public void setRightButtonEnable(String title, boolean isEnable, View.OnClickListener listener) {
        if (isEnable) {
            icon_right.setText(title);
            icon_right.setVisibility(View.VISIBLE);
            icon_right.setOnClickListener(listener);

        } else {
            icon_right.setVisibility(View.GONE);
            icon_right.setOnClickListener(null);
        }
    }


    @Override
    public void initializeViews() {
        try {

            context = HomeActivity.this;
            drawerView = findViewById(R.id.left_drawer);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            toolbar = (Toolbar) findViewById(R.id.home_layout_toolbar);
            title_tv = (TextView) findViewById(R.id.title_tv);
            recyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
            icon_right = findViewById(R.id.icon_right);
            recyclerView.setLoadingMoreEnabled(false);
            recyclerView.setPullRefreshEnabled(false);
            View leftDrawerHeader = View.inflate(context, R.layout.left_drawer_header, null);
            tvProfileUserName = (TextView) leftDrawerHeader.findViewById(R.id.leftDrawer_profileName_text);
            recyclerView.addHeaderView(leftDrawerHeader);
            leftDrawerListAdapter = new LeftDrawerListAdapter(HomeActivity.this, leftMenuDrawerItemses);
            leftDrawerListAdapter.setOnItemClickListener(this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(leftDrawerListAdapter);
            prepareMenuItemList();
            setDrawerHover(0);
            tvProfileUserName.setText(AppSharedPrefs.getInstance(context).getEmployeeName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtils.REQUEST_CODE_LOCATION) {
            if (Utils.onRequestPermissionsResult(permissions, grantResults)) {
                if (!Utils.isGpsEnable(context)) {
//                    displayLocationSettingsRequest();
                }
            } else {
                Utils.showToast(context, getString(R.string.allow_location_permission));
//                finishAffinity();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            fragment = getSupportFragmentManager().findFragmentById(R.id.home_layout_container);
            if (fragment != null)
                fragment.onResume();

            setMyDrawer(fragment);

            activityResumed();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final int[] menuItemIcons = new int[]{
            R.drawable.home,
            R.drawable.slider_pending_req,
            R.drawable.slider_notification, R.drawable.slider_settings,
            R.drawable.slider_logout
            /*R.drawable.slider_profile,*/
    };

    private final int[] menuItemIconsSelected = new int[]{

            R.drawable.home_hover,
            R.drawable.pending_req_hover,
            R.drawable.notification_hover, R.drawable.settings_hover,
            R.drawable.logout_hover
            /*R.drawable.profile_hover,*/
    };

    private void prepareMenuItemList() {

        menuItemNames = this.getResources().getStringArray(R.array.drawer_menu_titles);
        for (int i = 0; i < menuItemNames.length; i++) {

            LeftMenuDrawerItems leftMenuDrawerItems = new LeftMenuDrawerItems();
            leftMenuDrawerItems.setTitle(menuItemNames[i]);
            leftMenuDrawerItems.setSelectedIcon(menuItemIconsSelected[i]);
            leftMenuDrawerItems.setDeSelectedIcon(menuItemIcons[i]);
            leftMenuDrawerItemses.add(leftMenuDrawerItems);

        }

        leftDrawerListAdapter.notifyDataSetChanged();
    }

    private void setupDrawer() {

        try {

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                    R.string.drawer_open, R.string.drawer_close) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };


            // Set the drawer toggle as the DrawerListener
            mDrawerLayout.addDrawerListener(mDrawerToggle);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
    }


    @Override
    public void onItemClick(int position) {
        if (!Connectivity.isConnected()) {
            Utils.showSnackBar(HomeActivity.this, drawerView, getString(R.string.internet_connection));
            return;
        }

        if (!AppSharedPrefs.getInstance(getApplicationContext()).isLogin()) {
            return;
        }

        if (AppSharedPrefs.getInstance(getApplicationContext()).isTestDriveRunning()) {
            return;
        }

        /**
         * disable right button
         */
        setRightButtonEnable("", false, null);

        mDrawerLayout.closeDrawer(drawerView);

        switch (position) {

            case 0://Home
                setDrawerHover(position);
                title_tv.setText(R.string.welcome_to_link);
                Utils.replaceFragment(HomeActivity.this, new HomeFragment());
                break;
            case 1://Assets Request
                setDrawerHover(position);
                title_tv.setText(getString(R.string.txt_title_screen_asset_request));
                Utils.replaceFragment(HomeActivity.this, new AssetRequestFragment());
                break;
            case 2: //Notification
                setDrawerHover(position);
                title_tv.setText(getString(R.string.tittle_notifications));
                Utils.replaceFragment(HomeActivity.this, new NotificationFragment());

                break;
          /*  case 3://Profile
                setDrawerHover(position);
                break;*/
            case 3://Setting
                setDrawerHover(position);
                title_tv.setText(getString(R.string.setting));
                Utils.replaceFragment(HomeActivity.this, new SettingFragment());
                break;
            case 4: //Logout
                setDrawerHover(position);
                Utils.showAlert(context, getResources().getString(R.string.app_name)
                        , getResources().getString(R.string.logout_message)
                        , getResources().getString(R.string.dialog_yes)
                        , getResources().getString(R.string.dialog_cancel)
                        , AppUtils.dialog_logout_app, this);
                break;
        }
    }


    private void setMyDrawer(Fragment fragment) {

        try {
            if (fragment instanceof HomeFragment) {
                setDrawerHover(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void setDrawerHover(int position) {

        try {
            if (!leftMenuDrawerItemses.get(position).isMenuIsSelected()) {
                for (int i = 0; i < leftMenuDrawerItemses.size(); i++) {
                    leftMenuDrawerItemses.get(i).setMenuIsSelected(false);
                }
                leftMenuDrawerItemses.get(position).setMenuIsSelected(true);
                leftDrawerListAdapter.notifyDataSetChanged();

            }
//            leftDrawerListAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void setMyActionBar(Fragment fragment) {
        try {
            if (fragment instanceof HomeFragment) {
                title_tv.setVisibility(View.VISIBLE);
//                setDrawerHover(0);
                title_tv.setText(getResources().getString(R.string.welcome_to_link));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        try {
            if (mDrawerLayout.isDrawerOpen(drawerView)) {
                mDrawerLayout.closeDrawer(drawerView);
            } else {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_layout_container);
                if (fragment instanceof HomeFragment || fragment instanceof HomeFragment) {
                    Utils.showAlert(context, "", getString(R.string.quit_app_msg), "Yes", "No", AppUtils.dialog_yes_no_to_finish_app, this);
                } else {
                    callHome();
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void callHome() {
        try {
            title_tv.setVisibility(View.GONE);
            setDrawerHover(0);
            title_tv.setText(getString(R.string.welcome_to_link));
            Utils.replaceFragment(HomeActivity.this, new HomeFragment());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDialogClick(int which, int requestCode) {
        switch (requestCode) {
            case AppUtils.dialog_yes_no_to_finish_app:
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finishAffinity();
                    case DialogInterface.BUTTON_NEGATIVE:
                }
                break;
            case AppUtils.dialog_logout_app:
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        doLogout();
                        AppSharedPrefs.getInstance(context).setLogin(false);
                    case DialogInterface.BUTTON_NEGATIVE:

                }
                break;
        }
    }

    private void handlePushCall(PushData pushData) {

        if (AppSharedPrefs.isTestDriveRunning()) {
            Intent intent = new Intent(HomeActivity.this, TestDriveStuckActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        switch (pushData.getPushType()) {
            case Keys.NOTIFICATION_ASSET_REQUEST:
                break;
            case Keys.NOTIFICATION_ASSET_REQUEST_APPROVE:
                startActivity(new Intent(context, AssetListActivity.class));
                break;
            case Keys.NOTIFICATION_ASSET_REQUEST_DECLINE:
                break;
            case Keys.NOTIFICATION_ASSET_TRANSFER_REQUEST:
                onItemClick(1);
                break;
            case Keys.NOTIFICATION_ASSET_TRANSFER_APPROVE:
                viewModel.getCurrentAssetsOwned();
                onItemClick(1);
                break;
            case Keys.NOTIFICATION_ASSET_TRANSFER_DECLINE:
                onItemClick(1);
                break;
            case Keys.NOTIFICATION_ASSET_SUBMIT_REQUEST:
                break;
            case Keys.NOTIFICATION_ASSET_SUBMIT_APPROVE:
                startActivity(new Intent(context, AssetListActivity.class));
                break;
            case Keys.NOTIFICATION_ASSET_SUBMIT_DECLINE:
                startActivity(new Intent(context, AssetListActivity.class));
                break;
            case Keys.NOTIFICATION_ASSET_HOLD_TIME_EXCEED:
                startActivity(new Intent(context, AssetListActivity.class));
                break;
            case Keys.NOTIFICATION_SUPER_ADMIN_NOTIFICATION_TO_COMPANY:
                setDrawerHover(2);
                title_tv.setText(getString(R.string.tittle_notifications));
                Utils.replaceFragment(HomeActivity.this, new NotificationFragment());
                break;
            case Keys.NOTIFICATION_COMPANY_ADMIN_NOTIFICATION_TO_EMPLOYEE:
                setDrawerHover(2);
                title_tv.setText(getString(R.string.tittle_notifications));
                Utils.replaceFragment(HomeActivity.this, new NotificationFragment());
                break;
            case Keys.NOTIFICATION_CHAT_COMMUNICATION_BETWEEN_EMPLOYEE_TO_EMPLOYEE_AN:
                Intent intent = new Intent(context, ChatActivity.class);
                String emp_chat_url = pushData.getAdditionalData().getChatUserUrl();
                if (!TextUtils.isEmpty(emp_chat_url)) {
                    intent.putExtra(AppUtils.CHAT_EMP_URL, emp_chat_url);
                    context.startActivity(intent);
                } else {
                    context.startActivity(intent);
                }
                break;
            case Keys.NOTIFICATION_HIGH_SPEED_ALERT:

                break;

        }
    }


    @Override
    protected void onNewIntent(Intent intent) {

        if (intent != null && intent.hasExtra(Keys.NOTIFICATION_DATA)) {
            if (!AppSharedPrefs.isTestDriveRunning()) {
                PushData pushData = (PushData) this.getIntent().getSerializableExtra(Keys.NOTIFICATION_DATA);
                handlePushCall(pushData);
            } else {
                return;
            }
        }
    }

    public void doLogout() {

        if (!Connectivity.isConnected()) {
            Utils.hideProgressDialog();
            doLogoutwork();
            return;
        }

        try {
            Call<BaseResponse> call = RetrofitHolder.getService().doLogout(KeyKeepApplication.getInstance().getBaseEntity(false));

            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    Utils.hideProgressDialog();
                    doLogoutwork();
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Utils.hideProgressDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLogoutwork() {
        try {
            Intent intent = new Intent(context, DeleteTokenService.class);
            startService(intent);

            Utils.stopLocationStorage(context);


            AppSharedPrefs.getInstance(HomeActivity.this).clearPref();

            Utils.clearNotification(context);

            Intent logOutIntent = new Intent(HomeActivity.this, LoginActivity.class);
            logOutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logOutIntent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    Observer<Integer> observer = new Observer<Integer>() {

        @Override
        public void onChanged(@Nullable Integer value) {
            switch (value) {

                case AppUtils.NO_INTERNET:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.internet_connection));
                    break;

                case AppUtils.SERVER_ERROR:
                    Utils.hideProgressDialog();
                    Utils.showSnackBar(binding, getString(R.string.server_error));
                    break;
            }
        }
    };


    Observer<EmployeeOwnedAssetsListResponse> responseAssetsOwnedCurrently = new Observer<EmployeeOwnedAssetsListResponse>() {

        @Override
        public void onChanged(@Nullable EmployeeOwnedAssetsListResponse employeeOwnedAssetsListResponse) {

            Utils.hideProgressDialog();
            if (employeeOwnedAssetsListResponse != null && employeeOwnedAssetsListResponse.getResults() != null && employeeOwnedAssetsListResponse.getResults().size() > 0) {
                ArrayList<EmployeeOwnedAssetsListResponse.Result> resultArrayList = employeeOwnedAssetsListResponse.getResults();
                if (resultArrayList.size() > 0) {
                    storeOwnedKeyIdsPreferences(employeeOwnedAssetsListResponse);
                    Utils.startLocationStorage(context);
                } else {
                    Utils.stopLocationStorage(context);
                }
            }
        }
    };

    private void storeOwnedKeyIdsPreferences(EmployeeOwnedAssetsListResponse employeeOwnedAssetsListResponse) {
        String ownedKeys = null;
        if (employeeOwnedAssetsListResponse != null && employeeOwnedAssetsListResponse.getResults().size() > 0) {
            for (int i = 0; i < employeeOwnedAssetsListResponse.getResults().size(); i++) {
                if (!TextUtils.isEmpty(employeeOwnedAssetsListResponse.getResults().get(i).getAsset_id())) {
                    if (ownedKeys != null) {
                        ownedKeys = ownedKeys + "," + employeeOwnedAssetsListResponse.getResults().get(i).getAsset_id();
                    } else {
                        ownedKeys = employeeOwnedAssetsListResponse.getResults().get(i).getAsset_id();
                    }
                }
            }
            AppSharedPrefs.getInstance(context).setOwnedKeyIds(ownedKeys);
        }
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
                        if (Utils.checkPermissions(HomeActivity.this, AppUtils.LOCATION_PERMISSIONS)) {

                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(AppUtils.LOCATION_PERMISSIONS, AppUtils.REQUEST_CODE_LOCATION);
                            }
                        }
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("tag", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
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
}
