package com.keykeep.app.views.custom_view;

/**
 * Created by ankurrawal on 9/10/17.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.keykeep.app.R;
import com.keykeep.app.views.custom_view.StyledTextViewLight;


/**
 * Created by Ankur Rawal on 21/11/16.
 * <p>
 * CustomProgressDialog displays 'ProgressDialog' widget with given custom view.
 */
public class CustomProgressDialog extends Dialog {

    private Context mContext;

    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mContext = context;
    }


    public void show(String message) {

        if (!((Activity) mContext).isFinishing()) {
            super.show();
            View view = LayoutInflater.from(mContext).inflate(R.layout.loader_layout, null);
            StyledTextViewLight loaderMsg = (StyledTextViewLight) view.findViewById(R.id.loader_msg);
            loaderMsg.setText(message);

            setContentView(view);
        }
    }

//    @Override
//    public void show() {
//        if (!((Activity) mContext).isFinishing()) {
//            super.show();
//            View view = LayoutInflater.from(mContext).inflate(R.layout.loader_layout, null);
//            setContentView(view);
//        }
//    }
}
