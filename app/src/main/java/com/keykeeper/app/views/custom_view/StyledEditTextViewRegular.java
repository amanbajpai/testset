package com.keykeeper.app.views.custom_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.keykeeper.app.R;

/**
 * Created by akshaydashore on 22/8/18
 */

public class StyledEditTextViewRegular extends EditText {

    private final String DEFAULT_FONT = "Poppins-Regular.ttf";

    public StyledEditTextViewRegular(Context context) {
        super(context);
    }

    public StyledEditTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        setCustomFont(context, attrs);
    }

    public StyledEditTextViewRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setCustomFont(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StyledEditTextViewRegular(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setCustomFont(context, attrs);
    }


    public void setCustomFont(Context context, AttributeSet attrs) {
        try {
            String customFont = getCustomFont(context, attrs);
            if (customFont != null) {
                Typeface face = Typeface.createFromAsset(context.getAssets(), customFont);
                this.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT);
                this.setTypeface(face);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getCustomFont(Context context, AttributeSet attrs) {
        TypedArray ta = null;

        try {
            ta = context.obtainStyledAttributes(attrs, R.styleable.TextElement, 0, 0);
            String fontName = ta.getString(R.styleable.TextElement_fontName);
            return fontName;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ta.recycle();
        }
        return null;
    }
}
