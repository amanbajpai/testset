
package com.keykeep.app.views.activity;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keykeep.app.R;
import com.keykeep.app.model.LeftMenuDrawerItems;
import com.keykeep.app.views.adapter.LeftDrawerListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by ankurrawal
 */
public class HomeActivity extends BaseActivity {

    Context context;
    RelativeLayout searchRelativelayout;
    LinearLayout seachlayout;
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<LeftMenuDrawerItems> leftMenuDrawerItemses = new ArrayList<>();
    private XRecyclerView recyclerView;
    private LeftDrawerListAdapter leftDrawerListAdapter;
    View drawerView;
    private String[] menuItemNames;
    int previousPosition = -1;
    Toolbar toolbar;
    CircleImageView profilePic;
    StyledTextView userNameTv, contactNoTv, anywhwreTv, antyTimeTv, guestTv, cusineTv;
    private List<MultiSelectListItem> selectedListCusineItems = new ArrayList<>();
    List<MultiSelectListItem> cuisineChecked = new ArrayList<>();
    private Fragment fragment;
    public ImageView notificationBellIv;
    StyledTextView notificationCountTv;
    private static boolean activityVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        isActivityRunning = true;

        initializeViews();
        setupDrawer();
        setView();
    }


    private void setView() {


    }

    @Override
    public void initializeViews() {

        try {

            context = HomeActivity.this;
            drawerView = findViewById(R.id.left_drawer);
//          scrollView = (ScrollView) findViewById(R.id.left_drawer_scroll_layout);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            toolbar = (Toolbar) findViewById(R.id.home_layout_toolbar);
//          toolbar.setContentInsetsAbsolute(0, 0);
//          toolbar.setPadding(0, 0, 0, 0);
            title_tv = (TextView) findViewById(R.id.title_tv);
            anywhwreTv = (StyledTextView) findViewById(R.id.anywhere_tv);
            antyTimeTv = (StyledTextView) findViewById(R.id.anytime_tv);
            guestTv = (StyledTextView) findViewById(R.id.guest_tv);
            cusineTv = (StyledTextView) findViewById(R.id.cuisine_tv);
            newTicketTv = (TextView) findViewById(R.id.create_new_support);
            recyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLoadingMoreEnabled(false);
            recyclerView.setPullRefreshEnabled(false);

            View leftDrawerHeader = View.inflate(context, R.layout.left_drawer_header, null);
            recyclerView.addHeaderView(leftDrawerHeader);
            profilePic = (CircleImageView) leftDrawerHeader.findViewById(R.id.leftDrawer_profile_imageView);
            userNameTv = (StyledTextView) leftDrawerHeader.findViewById(R.id.leftDrawer_profileName_text);
            contactNoTv = (StyledTextView) leftDrawerHeader.findViewById(R.id.left_drawerNumber_text);
            notificationBellIv = (ImageView) findViewById(R.id.notification_bell);
            notificationBellIv.setOnClickListener(this);

            searchRelativelayout = (RelativeLayout) findViewById(R.id.searchmainlayout);
            notificationCountTv = (StyledTextView) findViewById(R.id.notifcation_count);
            searchRelativelayout.setOnClickListener(this);
            seachlayout = (LinearLayout) findViewById(R.id.search_layout);
            seachlayout.setOnClickListener(this);
            leftDrawerListAdapter = new LeftDrawerListAdapter(HomeActivity.this, leftMenuDrawerItemses);
            leftDrawerListAdapter.setOnItemClickListener(this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(leftDrawerListAdapter);
            prepareMenuItemList();
            setDrawerHover(0);

            newTicketTv.setOnClickListener(this);


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
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.STATE_CHANGE");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            LocalBroadcastManager.getInstance(this).registerReceiver(networBroadcastReceiver, intentFilter);

            activityResumed();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    @Override
    protected void onPause() {
        super.onPause();

        activityPaused();
        try {
            context.unregisterReceiver(notificationCountUpdateReceiver);
            context.unregisterReceiver(updateReceiver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private final int[] menuItemIcons = new int[]{R.drawable.hovme,
            R.drawable.boking, R.drawable.payment, R.drawable.my_account,
            R.drawable.how_it, R.drawable.abou_us, R.drawable.feedback,
            R.drawable.suport, R.drawable.term, R.drawable.safty, R.drawable.share,
            R.drawable.rate, R.drawable.transparrent_shape_drawer, R.drawable.logout};


    private final int[] menuItemIconsSelected = new int[]{

            R.drawable.home_active, R.drawable.boking_hover, R.drawable.payment_hover,
            R.drawable.my_account_activ, R.drawable.how_it_active, R.drawable.abou_us_active,
            R.drawable.feedback_active,
            R.drawable.supor_active, R.drawable.term_white, R.drawable.safty_hover, R.drawable.share_active, R.drawable.rate_active, R.drawable.transparrent_shape_drawer, R.drawable.logout_hover

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
            /*mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.string.drawer_open, R.string.drawer_close)*/
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
        isActivityRunning = false;
    }

    @Override
    protected void onDestroy() {
        try {

            clearAllFilters();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(networBroadcastReceiver);
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


    public void clearNotificationFromStatusBar() {
        try {
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(DiningInApplication.NOTIFICATION_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
    }


    @Override
    public void onItemClick(int position) {
        if (!Connectivity.isConnected(context)) {
            Utils.showToast(context, context.getResources().getString(R.string.no_internet));
            return;
        }

        mDrawerLayout.closeDrawer(drawerView);

        switch (position) {

            case 0: // Home
                setDrawerHover(position);
                title_tv.setText(getString(R.string.host_dinig_title));
                Utils.replaceFragment(HomeActivity.this, new HomeFragment());
                break;
            case 1://My Bookings
                openBookingScreen();
                break;
            case 2://My Payments
                openPaymentScreen();
                break;
            case 3://My Account
                setDrawerHover(position);
                title_tv.setText(menuItemNames[position]);
                Utils.replaceFragment(HomeActivity.this, new MyAccountFragment());
                break;
            case 4://How It Works
                openWebActivityTerms(menuItemNames[position], UrlConstants.HOW_IT_WORK_URL, ConstantsLib.HOWITWORKS);
                break;
            case 5: //About Us
                openWebActivityTerms(menuItemNames[position], UrlConstants.ABOUT_US_URL, ConstantsLib.ABOUT);
                break;
            case 6://Feedback
                openEmail();
                break;
            case 7://Support
                setDrawerHover(position);
                title_tv.setText(menuItemNames[position]);
                Utils.replaceFragment(HomeActivity.this, new SupportFragment());
                break;
            case 8: //Legals
                openWebActivityTerms(menuItemNames[position], UrlConstants.TERMS_AND_CON_URL, ConstantsLib.LEGALS);
                break;
            case 9: //Trust&safet
                openWebActivityTerms(menuItemNames[position], UrlConstants.TRUST_AND_SAFETY, ConstantsLib.SAFETYANFTRUST);
                break;
            case 10: //Share
                Utils.shareApp(getString(R.string.app_name), getString(R.string.tagline), this);
                break;
            case 11://Rate Us
                if (!Connectivity.isConnected(context)) {
                    Utils.showToast(context, context.getResources().getString(R.string.no_internet));
                    return;
                } else {
                    logScreen(getString(R.string.screen_rateus));
                    Utils.openPlayStore(context);
                }
                break;
            case 13: //Logout
                Utils.showAlert(context, getResources().getString(R.string.app_name)
                        , getResources().getString(R.string.logout_message)
                        , getResources().getString(R.string.dialog_yes)
                        , getResources().getString(R.string.dialog_cancel)
                        , ConstantsLib.DIALOG_LOGOUT_REQUEST, this);
                break;

        }
    }

    private void openBookingScreen() {
        Intent intent = new Intent(HomeActivity.this, MyBookingsActivity.class);
        startActivity(intent);
    }

    private void openPaymentScreen() {
        Intent intent = new Intent(HomeActivity.this, MyPaymentsListActivity.class);
        startActivity(intent);
    }

    private void setMyDrawer(Fragment fragment) {

        try {
            if (fragment instanceof HomeFragment) {
                setDrawerHover(0);
            } else if (fragment instanceof MyAccountFragment) {
                setDrawerHover(3);
            } else if (fragment instanceof SupportFragment) {
                setDrawerHover(7);
            } else if (fragment instanceof HomeFragment) {
                setDrawerHover(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void openEmail() {
        String email = "";

        if (AppSharedPrefs.getInstance(context).getFeedbackEmail().equalsIgnoreCase("")) {
            email = getResources().getString(R.string.feedback_email);
        } else {
            email = AppSharedPrefs.getInstance(context).getFeedbackEmail();
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }


    private void openWebActivityTerms(String title, String url, String callfrom) {

        Intent intent = new Intent(HomeActivity.this, TermsAndConditionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("callfrom", callfrom);
        startActivity(intent);
    }


    private void setDrawerHover(int position) {

        try {
            if (!leftMenuDrawerItemses.get(position).isMenuIsSelected()) {
                leftMenuDrawerItemses.get(position).setMenuIsSelected(true);
            }

            if (previousPosition != -1 && previousPosition != position) {
                leftMenuDrawerItemses.get(previousPosition).setMenuIsSelected(false);
                leftDrawerListAdapter.notifyDataSetChanged();
            } else {
                leftDrawerListAdapter.notifyDataSetChanged();
            }
            previousPosition = position;
//            leftDrawerListAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void logoutFromServer() {
        try {
            try {
                HashMap<String, String> stringParams = new HashMap<>();
                stringParams.put("access_token", AppSharedPrefs.getInstance(context).getAccessToken());
                stringParams.put("device_id", Utils.getDeviceId(context));

                NetworkController networkController = new NetworkController(context);
                networkController.call(UrlConstants.LOGOUT_URL_REQUEST_CODE, stringParams, this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setMyActionBar(Fragment fragment) {
        try {
            if (fragment instanceof MyAccountFragment) {
                searchRelativelayout.setVisibility(View.GONE);
                title_tv.setVisibility(View.VISIBLE);
                setDrawerHover(3);
                newTicketTv.setVisibility(View.GONE);
                title_tv.setText(getString(R.string.my_account));
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
            }

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
}
