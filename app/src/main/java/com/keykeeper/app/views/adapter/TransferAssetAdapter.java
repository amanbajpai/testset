package com.keykeeper.app.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keykeeper.app.R;
import com.keykeeper.app.model.bean.AssetsListResponseBean;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.assetDetail.AssetDetailActivity;

/**
 * Created by akshaydashore on 23/8/18
 */

public class TransferAssetAdapter extends RecyclerView.Adapter<TransferAssetAdapter.Holder> {

    Context context;
    private AssetsListResponseBean assetLists;
    ActivityForResult listener;

    public TransferAssetAdapter(Context context, AssetsListResponseBean resultAssetList,ActivityForResult listener) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.listener = listener;

    }



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.asset_recycler_item, null);
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
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_TRANSFER_ASSET_LIST);
                intent.putExtra(AppUtils.ASSET_ID,bean.getAssetId());
                intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
                listener.onCallActivityResult(intent);
            }
        });
        holder.assetName.setText(assetLists.getResult().get(position).getAssetName());
        holder.vinNumber.setText("VIN Number: "+assetLists.getResult().get(position).getVin());

        holder.assignedAt.setText("Assigned At: "+Utils.formattedDateFromString(Utils.INPUT_DATE_TIME_FORMATE, Utils.OUTPUT_DATE_TIME_FORMATE, assetLists.getResult().get(position).getAssigned_approved_or_decline_at()));
        holder.remainingTime.setText("Remaining Time: "+assetLists.getResult().get(position).getAssets_hold_remain_time());


       /* holder.versionNumber.setText(assetLists.getResult().get(position).getVersionNumber());
        if (assetLists.getResult().get(position).getAssetAssginedStatus().equalsIgnoreCase("1")) {
            holder.availableStatus.setText(context.getString(R.string.txt_status_available));
        } else {
            holder.availableStatus.setText(context.getString(R.string.txt_status_unavailable));
        }*/
    }


    @Override
    public int getItemCount() {
        return assetLists.getResult().size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private AppCompatTextView assetName, vinNumber, assignedAt,remainingTime;

        public Holder(View itemView) {
            super(itemView);
            assetName = itemView.findViewById(R.id.tv_asset_name);
            assignedAt = itemView.findViewById(R.id.tv_assigned_at);
            remainingTime = itemView.findViewById(R.id.tv_remaining_time);
            vinNumber = itemView.findViewById(R.id.tv_vin_number);
        }
    }

    public interface ActivityForResult {
        void onCallActivityResult(Intent intent);
    }

}
