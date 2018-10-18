package com.keykeeper.app.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.keykeeper.app.R;
import com.keykeeper.app.model.bean.AssetsListResponseBean;
import com.keykeeper.app.utils.AppUtils;
import com.keykeeper.app.utils.Utils;
import com.keykeeper.app.views.activity.assetDetail.AssetDetailActivity;
import com.keykeeper.app.views.activity.transfer.TransferActivity;

/**
 * Created by akshaydashore on 23/8/18
 */

public class TransferAssetAdapter extends RecyclerView.Adapter<TransferAssetAdapter.Holder> {

    Context context;
    private AssetsListResponseBean assetLists;
    ActivityForResult listener;
    private boolean isMultiSelectionMode;
    View.OnLongClickListener longClickListener;
    public static int selectCount = 0;

    public TransferAssetAdapter(Context context, AssetsListResponseBean resultAssetList, ActivityForResult listener, View.OnLongClickListener longClickListener) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.listener = listener;
        this.longClickListener = longClickListener;
        selectCount = 0;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.asset_recycler_item, null);
        Holder holder = new Holder(view);

        return holder;
    }

    public void enableMultiSelectionMode(boolean isEnable) {
        isMultiSelectionMode = isEnable;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AssetsListResponseBean.Result bean = assetLists.getResult().get(position);
                Intent intent = new Intent(context, AssetDetailActivity.class);
                intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_TRANSFER_ASSET_LIST);
                intent.putExtra(AppUtils.ASSET_ID, bean.getAssetId());
                intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
                listener.onCallActivityResult(intent);
            }
        });

        holder.itemView.setOnLongClickListener(longClickListener);

        if (isMultiSelectionMode) {
            holder.selectionCB.setVisibility(View.VISIBLE);
        } else {
            holder.selectionCB.setVisibility(View.GONE);
        }

        if (assetLists.getResult().get(position).isSelected) {
            holder.selectionCB.setChecked(true);
        } else {
            holder.selectionCB.setChecked(false);
        }


        holder.assetName.setText(assetLists.getResult().get(position).getAssetName());
        holder.vinNumber.setText("VIN Number: " + assetLists.getResult().get(position).getVin());

        holder.assignedAt.setText("Assigned At: " + Utils.formattedDateFromString(Utils.INPUT_DATE_TIME_FORMATE, Utils.OUTPUT_DATE_TIME_FORMATE, assetLists.getResult().get(position).getAssigned_approved_or_decline_at()));
        holder.remainingTime.setText("Remaining Time: " + assetLists.getResult().get(position).getAssets_hold_remain_time());

        holder.selectionCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    selectCount =  selectCount + 1;
                    ((TransferActivity)context).updateCounter(selectCount);
                }else {
                       if (selectCount >0){
                           selectCount = selectCount - 1;
                       }
                    ((TransferActivity)context).updateCounter(selectCount);
                }
                assetLists.getResult().get(position).isSelected = b;
            }
        });


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

        private AppCompatTextView assetName, vinNumber, assignedAt, remainingTime;
        CheckBox selectionCB;

        public Holder(View itemView) {
            super(itemView);
            assetName = itemView.findViewById(R.id.tv_asset_name);
            assignedAt = itemView.findViewById(R.id.tv_assigned_at);
            remainingTime = itemView.findViewById(R.id.tv_remaining_time);
            vinNumber = itemView.findViewById(R.id.tv_vin_number);
            selectionCB = itemView.findViewById(R.id.selectionCB);
        }
    }

    public interface ActivityForResult {
        void onCallActivityResult(Intent intent);
    }

}
