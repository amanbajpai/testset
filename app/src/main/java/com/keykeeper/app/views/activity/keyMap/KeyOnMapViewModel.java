package com.keykeeper.app.views.activity.keyMap;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Address;
import android.location.Location;
import android.os.Handler;

import com.keykeeper.app.databinding.ActivityKeyOnMapBinding;
import com.keykeeper.app.model.bean.AssetLocationResponseBean;
import com.keykeeper.app.model.bean.TrackLocationRequestEntity;
import com.keykeeper.app.netcom.retrofit.RetrofitHolder;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Connectivity;
import com.keykeeper.app.utils.Utils;

import java.util.List;

import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nazimakauser on 19/9/18.
 */

public class KeyOnMapViewModel extends ViewModel {
    MutableLiveData<Integer> validator = new MutableLiveData<>();
    MutableLiveData<AssetLocationResponseBean> response_validator = new MutableLiveData<>();
    String address = "";

    public void getLatLong(int asset_id, ActivityKeyOnMapBinding binding, TrackLocationRequestEntity trackLocationRequestEntity) {

        if (!Connectivity.isConnected()) {
            validator.setValue(AppUtils.NO_INTERNET);
//            Utils.hideProgressDialog();
            return;
        }

        Call<AssetLocationResponseBean> call = RetrofitHolder.getService().getAssetCurrentLocation(trackLocationRequestEntity, asset_id);

        call.enqueue(new Callback<AssetLocationResponseBean>() {

            @Override
            public void onResponse(Call<AssetLocationResponseBean> call, Response<AssetLocationResponseBean> response) {
                AssetLocationResponseBean bean = response.body();

                if (bean.getResult().getLocation().equalsIgnoreCase("")) {

                    getAddress(bean.getResult().getEmp_lat(), bean.getResult().getEmp_long(), binding);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bean.getResult().setLocation(address);
                            response_validator.setValue(bean);
                            KeyOnMapActivity.isDataLoading = false;
                        }
                    }, 2000);

                } else {
                    bean.getResult().setLocation(bean.getResult().getLocation());
                    response_validator.setValue(bean);
                }

            }

            @Override
            public void onFailure(Call<AssetLocationResponseBean> call, Throwable t) {
//                Utils.hideProgressDialog();
                validator.setValue(AppUtils.SERVER_ERROR);
                KeyOnMapActivity.isDataLoading = false;
            }

        });

    }

    public void getAddress(double lat, double lng, ActivityKeyOnMapBinding binding) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);

        SmartLocation.with(binding.getRoot().getContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
            @Override
            public void onAddressResolved(Location location, List<Address> results) {
                if (results.size() > 0) {
                    address = results.get(0).getAddressLine(0) + "\n" + results.get(0).getAddressLine(1);
                }
            }
        });
    }
}
