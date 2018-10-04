package com.keykeeper.app.views.activity.keyMap;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.keykeeper.app.R;
import com.keykeeper.app.views.custom_view.StyledEditTextViewRegular;

/**
 * Created by akshaydashore on 4/10/18
 */
public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    String title;

    public CustomInfoWindowGoogleMap(Context ctx, String title) {
        context = ctx;
        this.title = title;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.info_window_layout, null);

        TextView map_title_tv = view.findViewById(R.id.map_title_tv);

        map_title_tv.setText(title);

        return view;

    }

}
