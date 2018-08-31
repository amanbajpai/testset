package com.keykeep.app.views.custom_view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

/**
 * Created by akshaydashore on 27/8/18
 */

public class CustomTextInputLayoutTextMedium extends TextInputLayout {

    private final String DEFAULT_FONT = "Poppins-Medium.ttf";

    public CustomTextInputLayoutTextMedium(Context context) {
        super(context);
        setCustomFont(context);
    }

    public CustomTextInputLayoutTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public CustomTextInputLayoutTextMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context);
    }

    public void setCustomFont(Context context) {
        try {
            Typeface face = Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT);
            this.setTypeface(face);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
