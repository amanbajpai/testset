
package com.keykeep.app.views.activity.home;


import android.content.Context;
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
import com.keykeep.app.R;
import com.keykeep.app.model.LeftMenuDrawerItems;
import com.keykeep.app.utils.Connectivity;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.activity.AssetListActivity;
import com.keykeep.app.views.adapter.LeftDrawerListAdapter;
import com.keykeep.app.views.base.BaseActivity;
import com.keykeep.app.views.fragment.asset_request_fragment.AssetRequestFragment;
import com.keykeep.app.views.fragment.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ankurrawal
 */
public class HomeActivity extends BaseActivity implements LeftDrawerListAdapter.OnItemClickListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        initializeViews();
        setupDrawer();
        setView();
        Utils.replaceFragment(HomeActivity.this, new HomeFragment());

    }


    private void setView() {

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
            recyclerView.setLoadingMoreEnabled(false);
            recyclerView.setPullRefreshEnabled(false);
            View leftDrawerHeader = View.inflate(context, R.layout.left_drawer_header, null);
            recyclerView.addHeaderView(leftDrawerHeader);
            leftDrawerListAdapter = new LeftDrawerListAdapter(HomeActivity.this, leftMenuDrawerItemses);
            leftDrawerListAdapter.setOnItemClickListener(this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(leftDrawerListAdapter);
            prepareMenuItemList();
            setDrawerHover(0);

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
            R.drawable.hovme,
            R.drawable.hovme, R.drawable.slider_pending_req,
            R.drawable.slider_notification, R.drawable.slider_profile, R.drawable.slider_settings,
            R.drawable.slider_logout
    };

    private final int[] menuItemIconsSelected = new int[]{

            R.drawable.hovme,
            R.drawable.hovme, R.drawable.slider_pending_req,
            R.drawable.slider_notification, R.drawable.slider_profile, R.drawable.slider_settings,
            R.drawable.slider_logout
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
            Utils.showToast(context, getString(R.string.internet_connection));
            return;
        }

        mDrawerLayout.closeDrawer(drawerView);

        switch (position) {

            case 0://Home
                setDrawerHover(position);
                title_tv.setText("Home");
                Utils.replaceFragment(HomeActivity.this, new HomeFragment());
                break;
            case 1://Assets
                Intent asset_intent = new Intent(HomeActivity.this, AssetListActivity.class);
                startActivity(asset_intent);
                break;
            case 2://Pending Request
                title_tv.setText(getString(R.string.txt_title_screen_asset_request));
                Utils.replaceFragment(HomeActivity.this, new AssetRequestFragment());

                break;
            case 3: //Notification
                setDrawerHover(position);

                break;
            case 4://Profile
                setDrawerHover(position);
                break;
            case 5://Setting
                setDrawerHover(position);

                break;
            case 6: //Logout
                setDrawerHover(position);
                break;
        }
    }


    private void setMyDrawer(Fragment fragment) {

        try {
            if (fragment instanceof HomeFragment) {
                //setDrawerHover(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void setDrawerHover(int position) {

        try {
           /* if (!leftMenuDrawerItemses.get(position).isMenuIsSelected()) {
                leftMenuDrawerItemses.get(position).setMenuIsSelected(true);
            }

            if (previousPosition != -1 && previousPosition != position) {
                leftMenuDrawerItemses.get(previousPosition).setMenuIsSelected(false);
                leftDrawerListAdapter.notifyDataSetChanged();
            } else {
                leftDrawerListAdapter.notifyDataSetChanged();
            }
            previousPosition = position;*/
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
                //setDrawerHover(3);
                title_tv.setText(("Home"));
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