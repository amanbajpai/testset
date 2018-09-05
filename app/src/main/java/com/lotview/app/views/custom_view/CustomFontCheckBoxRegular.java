package com.lotview.app.views.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.lotview.app.R;

/**
 * Created by ankurrawal on 31/8/18.
 */

public class CustomFontCheckBoxRegular extends AppCompatCheckBox {
    private final String DEFAULT_FONT = "Poppins-Regular.ttf";

    public CustomFontCheckBoxRegular(Context context) {
        super(context);
        init();
    }

    public CustomFontCheckBoxRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomFontCheckBoxRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void init() {
        //set your typeface here.
//        setTypeface("");
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

            if (hasUnderline(context, attrs)) {
                this.setPaintFlags(this.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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


    private boolean hasUnderline(Context context, AttributeSet attrs) {
        TypedArray ta = null;

        try {
            ta = context.obtainStyledAttributes(attrs, R.styleable.TextElement, 0, 0);
            boolean hasUnderline = ta.getBoolean(R.styleable.TextElement_underline, false);
            return hasUnderline;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ta.recycle();
        }
        return false;
    }

}

