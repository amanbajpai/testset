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
import android.widget.TextView;

import com.lotview.app.R;
import com.lotview.app.model.bean.AssetsListResponseBean;
import com.lotview.app.preferences.AppSharedPrefs;
import com.lotview.app.utils.AppUtils;
import com.lotview.app.utils.Utils;
import com.lotview.app.views.activity.assetDetail.AssetDetailActivity;
import com.lotview.app.views.custom_view.StyledTextViewBold;

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
        View view = inflater.inflate(R.layout.key_list_my_item, null);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        AssetsListResponseBean.Result bean = assetLists.getResult().get(position);

        Log.e(bean.getAssetType() + "<<", "onBindViewHolder: " + AppUtils.ASSET_NEW);

        if (Utils.validateIntValue(bean.getAssetType()).equals(AppUtils.ASSET_CUSTOMER)) {
            holder.vinNumber.setText(Utils.validateStringToValue(bean.getCustomerName()));
        } else {
            holder.vinNumber.setText("Vin Number: " + assetLists.getResult().get(position).getVin());
        }

        // stock number
        holder.tv_stock_number.setText(assetLists.getResult().get(position).getAssetName());

        if (bean.getAssetAssginedStatus().equals("1")) {
            String mEmp_id = AppSharedPrefs.getEmployeeID();
            if (Utils.validateStringToValue(bean.getEmployeeId()).equals(mEmp_id)) {
                holder.availability_tv.setText(context.getString(R.string.owner_you));

            } else {
                holder.availability_tv.setText(context.getString(R.string.owner) + " " + bean.getEmployeeName());
            }
        } else {
            holder.availability_tv.setText(context.getString(R.string.txt_status_available));
        }

        String date = Utils.formattedDateFromString(Utils.INPUT_DATE_TIME_FORMATE, Utils.OUTPUT_DATE_TIME_FORMATE, bean.getAssigned_approved_or_decline_at());
        String time = Utils.formattedDateFromString(Utils.INPUT_DATE_TIME_FORMATE, Utils.OUTPUT_TIME_FORMATE, bean.getAssigned_approved_or_decline_at());

        //currently use to show owner
        holder.assigned_at_tv.setText("Assigned At: " + date);
        holder.remaining_time.setText("Remaining Time: " + time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typeRequest == AppUtils.STATUS_ASSET_SEND_REQUEST1) {
                    AssetsListResponseBean.Result bean = assetLists.getResult().get(position);
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_ASSET_SEND_REQUEST);
                    intent.putExtra(AppUtils.ASSET_REQUEST_ID, bean.getAssetEmployeeAssignedLogId());
                    intent.putExtra(AppUtils.SCANED_QR_CODE, bean.getQrCodeNumber());

                    if (listener != null)
                        listener.onCallActivityResult(intent);

                } else {
                    AssetsListResponseBean.Result bean = assetLists.getResult().get(position);
                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra(AppUtils.ASSET_STATUS_CODE, AppUtils.STATUS_ASSET_SEND_REQUEST1);
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

        private TextView vinNumber, assigned_at_tv, availability_tv, remaining_time;
        StyledTextViewBold tv_stock_number;

        public Holder(View itemView) {
            super(itemView);
            tv_stock_number = itemView.findViewById(R.id.tv_stock_number);
            assigned_at_tv = itemView.findViewById(R.id.assigned_at_tv);
            vinNumber = itemView.findViewById(R.id.tv_vin_number);
            availability_tv = itemView.findViewById(R.id.availability_tv);
            remaining_time = itemView.findViewById(R.id.remaining_time);
        }

    }


    public interface ActivityForResult {
        void onCallActivityResult(Intent intent);
    }

}
