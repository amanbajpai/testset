package com.keykeep.app.views.activity.login;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.keykeep.app.R;
import com.keykeep.app.interfaces.DialogClickListener;
import com.keykeep.app.netcom.Keys;
import com.keykeep.app.utils.Utils;
import com.keykeep.app.views.base.BaseViewMadel;

/**
 * Created by akshaydashore on 24/8/18
 */
public class LoginViewModel extends BaseViewMadel implements DialogClickListener {


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

    public boolean checkPassword(String text) {

        if (Utils.isStringsEmpty(text)) {
            validator.setValue(Keys.empty_password);
            return false;
        }
        return true;
    }

    @Override
    public void onDialogClick(int which, int requestCode) {

    }

}
