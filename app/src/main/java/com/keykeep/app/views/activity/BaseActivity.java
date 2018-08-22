package com.keykeep.app.views.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.keykeep.app.R;
import com.keykeep.app.application.KeyKeepApplication;


/**
 * Created by ankurrawal
 */
abstract public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * this method is responsible to initialize the views
     */
    public abstract void initializeViews();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(this.getResources().getColor(R.color.dark_gray_color));
            window.setStatusBarColor(this.getResources().getColor(R.color.colorRed));
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyKeepApplication.activityPaused();
    }


}
