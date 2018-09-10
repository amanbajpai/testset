package com.lotview.app.views.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.lotview.app.R;

abstract public class BaseFragment extends Fragment implements View.OnClickListener {

    /**
     * this method is responsible to initialize the views
     *
     * @param rootView rootView
     */
    abstract public void initializeViews(View rootView);



    public void replaceFragment(Fragment fragment ,int container,boolean isBackStack){

        FragmentManager manager  = getActivity().getSupportFragmentManager();
        FragmentTransaction  transaction  = manager.beginTransaction();
        transaction.replace(container,fragment);
        if (isBackStack){
            String stack_name  = fragment.getClass().getName();
            transaction.addToBackStack(stack_name);
        }
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment,boolean isBackStack){

        FragmentManager manager  = getActivity().getSupportFragmentManager();
        FragmentTransaction  transaction  = manager.beginTransaction();
        transaction.replace(R.id.home_layout_container,fragment);
        if (isBackStack){
            String stack_name  = fragment.getClass().getName();
            transaction.addToBackStack(stack_name);
        }
        transaction.commit();
    }


}
