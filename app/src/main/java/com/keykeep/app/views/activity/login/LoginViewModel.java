package com.keykeep.app.views.activity.login;

import android.arch.lifecycle.MutableLiveData;

import com.keykeep.app.interfaces.DialogClickListener;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.base.BaseViewMadel;

/**
 * Created by akshaydashore on 24/8/18
 */
public class LoginViewModel extends BaseViewMadel implements DialogClickListener{

    public final MutableLiveData<String> validator = new MutableLiveData<>();

    public boolean checkEmail(String text) {

        if (Utils.isStringsEmpty(text)) {
            validator.setValue("empty string");
        }
        return false;
    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }

}
