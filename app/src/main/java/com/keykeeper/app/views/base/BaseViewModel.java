package com.keykeeper.app.views.base;

import android.arch.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

/**
 * Created by akshaydashore on 24/8/18
 */

public class BaseViewModel<N> extends ViewModel {



    private WeakReference<N> mNavigator;

    public BaseViewModel() {

    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    public WeakReference<N> getmNavigator() {
        return mNavigator;
    }
}
