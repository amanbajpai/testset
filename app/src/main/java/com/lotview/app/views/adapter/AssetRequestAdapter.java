package com.lotview.app.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lotview.app.R;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.views.activity.assetDetail.AssetDetailActivity;

/**
 * Created by ashishthakur on 29/8/18.
 */
public class AssetRequestAdapter extends RecyclerView.Adapter<AssetRequestAdapter.Holder> {

    Context context;
    private AssetsListResponseBean assetLists;
    private int typeRequest;
    private ActivityForResult listener;

    public AssetRequestAdapter(Context context,
                               AssetsListResponseBean resultAssetList, int typeRequest) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.typeRequest = typeRequest;

    }

    public void setListener(ActivityForResult listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.asset_request_adapter, null);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        if (typeRequest == AppUtils.STATUS_ASSET_SEND_REQUEST1) {
            holder.assetName.setText(assetLists.getResult().get(position).getAssetName());
            holder.modelNumber.setText(assetLists.getResult().get(position).getModelNumber());

        } else {
            holder.assetName.setText(assetLists.getResult().get(position).getAssetName());
            holder.modelNumber.setText(assetLists.getResult().get(position).getModelNumber());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typeRequest == AppUtils.STATUS_ASSET_SEND_REQUEST1) {
                    AssetsListResponseBean.Result bean = assetLists.getResult().get(position);
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_ASSET_SEND_REQUEST1);
                    intent.putExtra(AppUtils.ASSET_REQUEST_ID, bean.getAssetEmployeeAssignedLogId());
                    intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());


                    if (listener != null)
                        listener.onCallActivityResult(intent);

                } else {
                    AssetsListResponseBean.Result bean = assetLists.getResult().get(position);
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_ASSET_SEND_REQUEST);
                    intent.putExtra(AppUtils.ASSET_REQUEST_ID, bean.getAssetEmployeeAssignedLogId());
                    intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
                    intent.putExtra(AppUtils.ASSET_REQUESTED_BY_EMP_NAME, bean.getRequestedByEmployeeName());
                    context.startActivity(intent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return assetLists.getResult().size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private AppCompatTextView assetName, modelNumber;

        public Holder(View itemView) {
            super(itemView);
            assetName = itemView.findViewById(R.id.tv_asset_name);
            modelNumber = itemView.findViewById(R.id.tv_model_number);

        }
    }


    public interface ActivityForResult {
        void onCallActivityResult(Intent intent);
    }

}
