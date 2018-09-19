package com.lotview.app.views.custom_view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lotview.app.R;

/**
 * Created by akshaydashore on 22/8/18
 * Using this class customize toolbar view on runtime
 */
public class CustomActionBar {

    private AppCompatActivity activity;
    private View.OnClickListener clickListener;
    ImageView left_iv;
    ImageView right_iv,map_iv,refresh_iv;
    TextView center_tv;

    public CustomActionBar(AppCompatActivity activity) {
        this.activity = activity;
    }


    /**
     * Design and Set back button,forward button ,title with click action
     *
     * @param titleString
     * @param show_right
     * @param show_left
     * @param show_map
     * @param show_refresh
     * @param onClickListener
     */
    public void setActionbar(String titleString, boolean show_left, boolean show_right,boolean show_map,boolean show_refresh, View.OnClickListener onClickListener) {

        this.clickListener = onClickListener;
        ActionBar actionBar = activity.getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);

        View actionbarView = actionBar.getCustomView();

        Toolbar toolbar = (Toolbar) actionbarView.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setPadding(0, 0, 0, 0);

        left_iv = actionbarView.findViewById(R.id.left_iv);
        right_iv = actionbarView.findViewById(R.id.right_iv);
        map_iv = actionbarView.findViewById(R.id.map_iv);
        refresh_iv = actionbarView.findViewById(R.id.refresh_iv);
        center_tv = (TextView) actionbarView.findViewById(R.id.center_tv);


        if (titleString != null && !titleString.equalsIgnoreCase("")) {
            center_tv.setText(titleString);
            center_tv.setVisibility(View.VISIBLE);
        }

        if (show_right) {
            right_iv.setVisibility(View.VISIBLE);
            right_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClick(v);
                    }

                }
            });
        }

        if (show_left) {
            left_iv.setVisibility(View.VISIBLE);
            left_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickListener != null) {
                        clickListener.onClick(v);
                    } else {
                        activity.onBackPressed();
                    }
                }
            });
        }

        if (show_map) {
            map_iv.setVisibility(View.VISIBLE);
            map_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClick(v);
                    }

                }
            });
        }
        if (show_refresh) {
            refresh_iv.setVisibility(View.VISIBLE);
            refresh_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClick(v);
                    }

                }
            });
        }
    }


    public void setLeftIcon(int id) {
        left_iv.setImageResource(id);
    }

    public void setRightIcon(int id) {
        right_iv.setImageResource(id);
    }

}
