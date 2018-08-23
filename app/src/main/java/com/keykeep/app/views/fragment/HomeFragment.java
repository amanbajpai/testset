package com.keykeep.app.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.keykeep.app.R;
import com.keykeep.app.views.activity.AssetListActivity;

public class HomeFragment extends BaseFragment {

    View rootView;
    private Context context;
    private RelativeLayout asset_rl, scan_rl, history_rl, hand_over_rl, takeout_rl, chat_rl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        initializeViews(rootView);
        return rootView;
    }

    @Override
    public void initializeViews(View rootView) {
        context = getActivity();

        asset_rl = rootView.findViewById(R.id.asset_rl);
        scan_rl = rootView.findViewById(R.id.scan_rl);
        history_rl = rootView.findViewById(R.id.history_rl);
        hand_over_rl = rootView.findViewById(R.id.hand_over_rl);
        takeout_rl = rootView.findViewById(R.id.takeout_rl);
        chat_rl = rootView.findViewById(R.id.chat_rl);

        asset_rl.setOnClickListener(this);
        scan_rl.setOnClickListener(this);
        history_rl.setOnClickListener(this);
        hand_over_rl.setOnClickListener(this);
        takeout_rl.setOnClickListener(this);
        chat_rl.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.asset_rl:
                startActivity(new Intent(context, AssetListActivity.class));
                break;

            case R.id.scan_rl:
                break;

            case R.id.history_rl:
                break;

            case R.id.hand_over_rl:
                break;

            case R.id.takeout_rl:
                break;

            case R.id.chat_rl:
                break;
        }
    }

}