package com.keykeep.app.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keykeep.app.R;
import com.keykeep.app.model.bean.AssetsListResponseBean;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.views.activity.assetDetail.AssetDetailActivity;

/**
 * Created by ashishthakur on 29/8/18.
 */

public class AssetRequestAdapter extends RecyclerView.Adapter<AssetRequestAdapter.Holder> {

    Context context;
    private AssetsListResponseBean assetLists;
    private int typeRequest;

    public AssetRequestAdapter(Context context,
                               AssetsListResponseBean resultAssetList, int typeRequest) {
        this.context = context;
        this.assetLists = resultAssetList;
        this.typeRequest = typeRequest;

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

        if (typeRequest == AppUtils.STATUS_ASSET_SEND_REQUEST) {
            holder.requestText.setText(String.format("%s%s%s%s", context.getString(R.string.txt_your_request_for), assetLists.getResult().get(position).getAssetName(), context.getString(R.string.txt_is_send_to), assetLists.getResult().get(position).getCustomerName()));
        } else {
            holder.requestText.setText(String.format("%s%s%s%s", assetLists.getResult().get(position).getCustomerName(), context.getString(R.string.txt_want_to), assetLists.getResult().get(position).getAssetName(), context.getString(R.string.txt_from_you)));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typeRequest == AppUtils.STATUS_ASSET_SEND_REQUEST) {
                    AssetsListResponseBean.Result bean = assetLists.getResult().get(position);
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_ASSET_RECEIVE_REQUEST);
                    intent.putExtra(AppUtils.ASSET_REQUEST_ID,bean.getAssetEmployeeAssignedLogId());
                    intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
                    context.startActivity(intent);
                }else {
                    AssetsListResponseBean.Result bean = assetLists.getResult().get(position);
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_ASSET_SEND_REQUEST);
                    intent.putExtra(AppUtils.ASSET_REQUEST_ID,bean.getAssetEmployeeAssignedLogId());
                    intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());
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

        private AppCompatTextView requestText;

        public Holder(View itemView) {
            super(itemView);
            requestText = itemView.findViewById(R.id.tv_request);

        }
    }

}
