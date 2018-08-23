package com.keykeep.app.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keykeep.app.R;

public class HomeFragment extends BaseFragment {


    public static final String EVENT_LIST = "event_list";
    View rootView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        return rootView;
    }

    @Override
    public void initializeViews(View rootView) {

        context = getActivity();


    }


    @Override
    public void onClick(View v) {

    }
}