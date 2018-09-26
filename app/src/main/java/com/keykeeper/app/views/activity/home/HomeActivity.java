
package com.keykeeper.app.views.activity.home;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.keykeeper.app.R;
import com.keykeeper.app.application.KeyKeepApplication;
import com.keykeeper.app.firebase.firebase.DeleteTokenService;
import com.keykeeper.app.interfaces.DialogClickListener;
import com.keykeeper.app.model.LeftMenuDrawerItems;
import com.keykeeper.app.model.bean.BaseResponse;
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
import com.keykeeper.app.views.adapter.LeftDrawerListAdapter;
import com.keykeeper.app.views.base.BaseActivity;
import com.keykeeper.app.views.fragment.asset_request_fragment.AssetRequestFragment;
import com.keykeeper.app.views.fragment.home.HomeFragment;
import com.keykeeper.app.views.fragment.notifications.NotificationFragment;
import com.keykeeper.app.views.fragment.setting.SettingFragment;
import com.keykeeper.app.views.services.LocationListenerService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        initializeViews();
        setupDrawer();
        setView();
        Utils.replaceFragment(HomeActivity.this, new HomeFragment());
        onNewIntent(getIntent());
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


    @Override
    protected void onPause() {
        super.onPause();

        activityPaused();
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
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            case AppUtils.dialog_logout_app:
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        doLogout();
                        AppSharedPrefs.getInstance(context).setLogin(false);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
        }
    }

    private void handlePushCall(PushData pushData) {

        switch (pushData.getPushType()) {
            case Keys.NOTIFICATION_ASSET_REQUEST_APPROVE:
                startActivity(new Intent(context, AssetListActivity.class));
                break;
            case Keys.NOTIFICATION_ASSET_TRANSFER_REQUEST:
                onItemClick(1);
                break;
            case Keys.NOTIFICATION_ASSET_TRANSFER_APPROVE:
                onItemClick(1);
                break;
            case Keys.NOTIFICATION_ASSET_TRANSFER_DECLINE:
                onItemClick(1);
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
                startActivity(new Intent(context, ChatActivity.class));
                break;

        }
    }


    @Override
    protected void onNewIntent(Intent intent) {

        if (intent != null && intent.hasExtra(Keys.NOTIFICATION_DATA)) {
            if (!AppSharedPrefs.isTestDriveRunning()) {
                PushData pushData = (PushData) this.getIntent().getSerializableExtra(Keys.NOTIFICATION_DATA);
                handlePushCall(pushData);
            }
        }
    }

    public void doLogout() {

        if (!Connectivity.isConnected()) {
            Utils.hideProgressDialog();
            doLogoutwork();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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

            Intent serviceIntent = new Intent(context, LocationListenerService.class);
            stopService(serviceIntent);

            AppSharedPrefs.getInstance(HomeActivity.this).clearPref();


            Intent intent1 = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
