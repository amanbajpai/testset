package com.keykeep.app.views.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.keykeep.app.R;
import com.keykeep.app.views.custom_view.CustomActionBar;

/**
 * Created by akshaydashore on 22/8/18
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setCustomActionBar();
    }

    /**
     * set Custom tool bar
     */
    private void setCustomActionBar() {
        CustomActionBar customActionBar = new CustomActionBar(this);
        customActionBar.setActionbar("Login", false, false, this);
    }

    @Override
    public void onClick(View view) {

    }


}
