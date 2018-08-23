package com.keykeep.app.views.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

abstract public class BaseFragment extends Fragment implements View.OnClickListener {

    /**
     * this method is responsible to initialize the views
     *
     * @param rootView rootView
     */
    abstract public void initializeViews(View rootView);


}
