package com.lotview.app.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lotview.app.R;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.assetDetail.AssetDetailActivity;

/**
 * Created by akshaydashore on 23/8/18
 */
public class MyAssetsAdapter extends RecyclerView.Adapter<MyAssetsAdapter.Holder> {

    Context context;
    private AssetsListResponseBean assetLists;

    public MyAssetsAdapter(Context context, AssetsListResponseBean resultAssetList) {
        this.context = context;
        this.assetLists = resultAssetList;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.key_list_item, null);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssetsListResponseBean.Result bean = assetLists.getResult().get(position);
                Intent intent = new Intent(context, AssetDetailActivity.class);
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_ALL_ASSET_LIST);
                intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
                context.startActivity(intent);
            }
        });
//        holder.assetName.setText(assetLists.getResult().get(position).getAssetName());
//        holder.modelNumber.setText(assetLists.getResult().get(position).getModelNumber());
//        holder.vinNumber.setText(assetLists.getResult().get(position).getVin());
//        holder.versionNumber.setText(assetLists.getResult().get(position).getVersionNumber());
//        if (assetLists.getResult().get(position).getAssetAssginedStatus().equalsIgnoreCase("1")) {
//            holder.availableStatus.setText(context.getString(R.string.txt_status_available));
//        } else {
//            holder.availableStatus.setText(context.getString(R.string.txt_status_unavailable));
//        }

        AssetsListResponseBean.Result bean = assetLists.getResult().get(position);

        Log.e(bean.getAssetType() + "<<", "onBindViewHolder: " + AppUtils.ASSET_NEW);

        if (Utils.validateIntValue(bean.getAssetType()).equals(AppUtils.ASSET_CUSTOMER)) {
            holder.vinNumber.setText(Utils.validateStringToValue(bean.getCustomerName()));
            holder.vinNumber.setVisibility(View.VISIBLE);
        } else {
            Log.e("onBindViewHolder: ", "true");
            holder.vinNumber.setText(assetLists.getResult().get(position).getVin());
            holder.vinNumber.setVisibility(View.VISIBLE);
        }

        // stock number
        holder.tv_stock_number.setText(assetLists.getResult().get(position).getAssetName());

        if (bean.getAssetAssginedStatus().equals("1")) {
            String mEmp_id = AppSharedPrefs.getEmployeeID();
            if (Utils.validateStringToValue(bean.getEmployeeId()).equals(mEmp_id)) {
                holder.availability_tv.setText(context.getString(R.string.owner_you));

            } else {
                holder.availability_tv.setText(context.getString(R.string.owner) + " "+bean.getEmployeeName());
            }
        } else {

            holder.availability_tv.setText(context.getString(R.string.txt_status_available));

        }

        //currently use to show owner
        String type = Utils.getAssetType(bean.getAssetType());
        holder.asset_type.setText(type);


    }


    @Override
    public int getItemCount() {
        return assetLists.getResult().size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private AppCompatTextView tv_stock_number, asset_type, vinNumber, availability_tv;

        public Holder(View itemView) {
            super(itemView);
//            assetName = itemView.findViewById(R.id.tv_asset_name);
//            modelNumber = itemView.findViewById(R.id.tv_model_number);
//            vinNumber = itemView.findViewById(R.id.tv_vin_number);
//            versionNumber = itemView.findViewById(R.id.tv_version_number);
//            availableStatus = itemView.findViewById(R.id.tv_available_status);
            tv_stock_number = itemView.findViewById(R.id.tv_stock_number);
//            assetName = itemView.findViewById(R.id.tv_asset_name);
            asset_type = itemView.findViewById(R.id.asset_type);
            vinNumber = itemView.findViewById(R.id.tv_vin_number);
            availability_tv = itemView.findViewById(R.id.availability_tv);

        }
    }

}
