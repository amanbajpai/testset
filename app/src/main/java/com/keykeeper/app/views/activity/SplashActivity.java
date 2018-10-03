package com.keykeeper.app.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.keykeeper.app.R;
import com.keykeeper.app.preferences.AppSharedPrefs;
import com.keykeeper.app.views.activity.home.HomeActivity;
import com.keykeeper.app.views.activity.login.LoginActivity;
import com.keykeeper.app.views.activity.testdrive.TestDriveStuckActivity;


/**
 * Created by ankurrawal on 22/8/18.
 */
public class SplashActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.splash_activity);


    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (AppSharedPrefs.getInstance(context).isTestDriveRunning()) {

                    Intent intent = new Intent(context, TestDriveStuckActivity.class);
                    startActivity(intent);

                } else if (AppSharedPrefs.getInstance(context).isLogin()) {
                    startActivity(new Intent(context, HomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(context, LoginActivity.class));
                }

            }
        }, 3000);
    }

}
