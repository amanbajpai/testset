package com.keykeep.app.views.activity.forgot_password;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.keykeep.app.interfaces.DialogClickListener;
import com.keykeep.app.netcom.Keys;
import com.keykeep.app.utils.Utils;

/**
 * Created by akshaydashore on 27/8/18
 */
public class ForgotViewModel extends ViewModel implements DialogClickListener{

    public final MutableLiveData<Integer> validator = new MutableLiveData<>();


    public boolean checkEmail(String text) {

        if (Utils.isStringsEmpty(text)) {
            validator.setValue(Keys.empty_id);
            return false;
        } else if (!Utils.isValideEmail(text)) {
            validator.setValue(Keys.invalid_mail);
            return false;
        }
        return true;
    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }
}
